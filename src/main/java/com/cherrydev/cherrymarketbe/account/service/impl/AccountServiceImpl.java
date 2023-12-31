package com.cherrydev.cherrymarketbe.account.service.impl;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.dto.AccountInfoDto;
import com.cherrydev.cherrymarketbe.account.dto.ModifyAccountInfoRequestDto;
import com.cherrydev.cherrymarketbe.account.dto.SignUpRequestDto;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.entity.Agreement;
import com.cherrydev.cherrymarketbe.account.enums.ForbiddenUserName;
import com.cherrydev.cherrymarketbe.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.account.repository.AgreementMapper;
import com.cherrydev.cherrymarketbe.account.service.AccountService;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthAccountInfoDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthAccountInfoDto2;
import com.cherrydev.cherrymarketbe.common.event.AccountRegistrationEvent;
import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

import static com.cherrydev.cherrymarketbe.account.enums.UserRole.ROLE_CUSTOMER;
import static com.cherrydev.cherrymarketbe.account.enums.UserStatus.ACTIVE;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.utils.CodeGenerator.generateRandomCode;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AgreementMapper agreementMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    @Transactional
    public void createAccount(final SignUpRequestDto signUpRequestDto) {
        String requestedEmail = signUpRequestDto.getEmail();
        String requestedUsername = signUpRequestDto.getName();

        checkUsernameIsProhibited(requestedUsername);
        checkEmailIsDuplicated(requestedEmail);

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        Account account = signUpRequestDto.toEntity(encodedPassword);
        accountMapper.save(account);

        Agreement agreement = signUpRequestDto.toAgreementEntity(account);
        agreementMapper.save(agreement);

        publishWelcomeEvent(account);
    }

    @Override
    @Transactional
    public void createAccountByOAuth(final OAuthAccountInfoDto oAuthAccountInfoDto, final String provider) {
        String email = oAuthAccountInfoDto.getEmail();
        String name = oAuthAccountInfoDto.getName();
        String encodedPassword = passwordEncoder.encode(generateRandomCode(10));

        checkUsernameIsProhibited(name);
        checkEmailIsDuplicated(email);

        Account account = Account.builder()
                .oauthId(oAuthAccountInfoDto.getId())
                .email(email)
                .name(name)
                .password(encodedPassword)
                .contact(oAuthAccountInfoDto.getContact())
                .userStatus(ACTIVE)
                .userRole(ROLE_CUSTOMER)
                .registerType(RegisterType.valueOf(provider.toUpperCase()))
                .build();

        accountMapper.save(account);
        publishWelcomeEvent(account);
    }

    @Override
    @Transactional
    public void createAccountByOAuth(final OAuthAccountInfoDto2 oAuthAccountInfoDto, final String provider) {
        String email = oAuthAccountInfoDto.getId();
        String name = oAuthAccountInfoDto.getName();
        String encodedPassword = passwordEncoder.encode(generateRandomCode(10));

        checkUsernameIsProhibited(name);
        checkEmailIsDuplicated(email);

        Account account = Account.builder()
                .oauthId(oAuthAccountInfoDto.getId())
                .email(email)
                .name(name)
                .password(encodedPassword)
                .contact("010-0000-0000")
                .userStatus(ACTIVE)
                .userRole(ROLE_CUSTOMER)
                .registerType(RegisterType.valueOf(provider.toUpperCase()))
                .build();

        accountMapper.save(account);
        publishWelcomeEvent(account);
    }



    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AccountInfoDto> getAccountInfo(final AccountDetails accountDetails) {
        Account account = accountDetails.getAccount();
        return ResponseEntity
                .ok()
                .body(
                        new AccountInfoDto(account)
                );
    }

    @Override
    @Transactional
    public ResponseEntity<AccountInfoDto> modifyAccount(
            final AccountDetails accountDetails,
            final ModifyAccountInfoRequestDto requestDto
    ) {
        Account account = accountDetails.getAccount();

        if (requestDto.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
            account.updatePassword(encodedPassword);
        }

        if (requestDto.getContact() != null) {
            account.updateContact(requestDto.getContact());
        }

        if (requestDto.getBirthdate() != null) {
            LocalDate birthdate = LocalDate.parse(requestDto.getBirthdate());
            account.updateBirthdate(birthdate);
        }

        accountMapper.updateAccountInfo(account);

        return ResponseEntity
                .ok()
                .body(
                        new AccountInfoDto(account)
                );
    }

    @Override
    @Transactional
    public void deleteAccount(final AccountDetails accountDetails) {
        accountMapper.delete(accountDetails.getAccount());
    }

    /**
     * 이메일로 사용자를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 조회된 사용자
     */
    @Override
    public Account findAccountByEmail(final String email) {
        return accountMapper.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
    }

    @Override
    public void checkDuplicateEmail(String email) {
        checkEmailIsDuplicated(email);
    }

    // =============== PRIVATE METHODS =============== //

    /**
     * 회원 가입 시 이메일이 중복되는지 확인한다.
     */
    private void checkEmailIsDuplicated(String email) {
        if (accountMapper.existByEmail(email)) {
            throw new DuplicatedException(CONFLICT_ACCOUNT);
        }
    }

    /**
     * 회원 가입 시 이름에 금지어가 포함되어 있는지 확인한다.
     *
     * @see ForbiddenUserName 금지어 목록
     */
    private void checkUsernameIsProhibited(String username) {
        boolean isProhibited = Arrays.stream(ForbiddenUserName.values())
                .anyMatch(forbiddenUserName -> username.contains(forbiddenUserName.getName()));

        if (isProhibited) {
            throw new AuthException(PROHIBITED_USERNAME);
        }
    }

    private void publishWelcomeEvent(Account account) {
        AccountRegistrationEvent event = new AccountRegistrationEvent(this, account);
        eventPublisher.publishEvent(event);
    }

}