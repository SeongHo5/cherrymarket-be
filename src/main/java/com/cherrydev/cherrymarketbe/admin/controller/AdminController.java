package com.cherrydev.cherrymarketbe.admin.controller;

import com.cherrydev.cherrymarketbe.admin.dto.*;
import com.cherrydev.cherrymarketbe.admin.service.AdminService;
import com.cherrydev.cherrymarketbe.admin.service.CouponService;
import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.customer.dto.reward.AddRewardRequestDto;
import com.cherrydev.cherrymarketbe.customer.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final CouponService couponService;
    private final RewardService rewardService;

    /**
     * 계정 목록 전체 조회
     * @param pageable 페이징 정보(page, size)
     */
    @GetMapping("/account-list")
    public ResponseEntity<MyPage<AdminUserInfoDto>> searchAccounts(
            final Pageable pageable
    ) {
        return adminService.getAllAcounts(pageable);
    }

    /**
     * 계정 권한 변경
     */
    @PatchMapping("/modify/account/role")
    @ResponseStatus(HttpStatus.OK)
    public void modifyAccountRole(
            final @RequestBody ModifyUserRoleDto roleRequestDto
    ) {
        adminService.modifyAccountRole(roleRequestDto);
    }

    /**
     * 계정 상태 변경
     */
    @PatchMapping("/modify/account/status")
    @ResponseStatus(HttpStatus.OK)
    public void modifyAccountStatus(
            final @RequestBody ModifyUserStatusDto statusRequestDto
    ) {
        adminService.modifyAccountStatus(statusRequestDto);
    }

    /**
     * 관리자에 의해 포인트 지급
     * @param addRewardRequestDto 지급할 포인트 정보
     */
    @PostMapping("/grant-reward")
    @ResponseStatus(HttpStatus.OK)
    public void grantReward(
            final @RequestBody AddRewardRequestDto addRewardRequestDto
    ) {
        rewardService.grantReward(addRewardRequestDto);
    }

    /**
     * 관리자에 의해 쿠폰 발행
     * @param issueCouponDto 발행할 쿠폰 정보
     */
    @PostMapping("/issue-coupon")
    @ResponseStatus(HttpStatus.OK)
    public void issueCoupon(
            final @RequestBody IssueCouponDto issueCouponDto
    ) {
        couponService.issueCoupon(issueCouponDto);
    }

    /**
     * 쿠폰 목록 전체 조회
     * @param pageable 페이징 정보(page, size)
     * @return 쿠폰 목록
     */
    @GetMapping("/coupon-list")
    public ResponseEntity<MyPage<CouponInfoDto>> searchCoupons(
            final Pageable pageable
    ) {
        return couponService.getAllCoupons(pageable);
    }

}
