package com.cherrydev.cherrymarketbe.common.scheduler;

import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountTaskScheduler {

    private final AccountMapper accountMapper;

    @Scheduled(cron = "0 0 1 * * ?") // 매일 01시 00분 00초
    @Transactional
    public void releaseRestrictedAccounts() {
        log.info("===== 정지 계정 해제 시작 =====");
        accountMapper.releaseRestrictedAccount();
        log.info("===== 정지 계정 해제 종료 =====");
    }

    @Scheduled(cron = "0 0 2 * * ?") // 매일 02시 00분 00초
    @Transactional
    public void deleteInactiveAccounts() {
        log.info("===== 보관 계정 삭제 시작 =====");
        accountMapper.deleteInactiveAccount();
        log.info("===== 보관 계정 삭제 종료 =====");
    }

}
