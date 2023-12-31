package com.cherrydev.cherrymarketbe.account.controller;

import com.cherrydev.cherrymarketbe.account.dto.*;
import com.cherrydev.cherrymarketbe.account.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountServiceImpl accountService;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(final @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        accountService.createAccount(signUpRequestDto);
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/my-info")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<AccountInfoDto> getAccountInfo(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return accountService.getAccountInfo(accountDetails);
    }

    /**
     * 내 정보 수정
     */
    @PatchMapping("/my-info/modify")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    public ResponseEntity<AccountInfoDto> modifyAccount(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody ModifyAccountInfoRequestDto requestDto
    ) {
        return accountService.modifyAccount(accountDetails, requestDto);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/drop-out")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    public void dropOut(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        accountService.deleteAccount(accountDetails);
    }

    /**
     * 이메일 중복체크 확인
     */
    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.CREATED)
    public void checkDuplicateEmail(final @Valid @RequestParam String email) {
        accountService.checkDuplicateEmail(email);
    }
}
