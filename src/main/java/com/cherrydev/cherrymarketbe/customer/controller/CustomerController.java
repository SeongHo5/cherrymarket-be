package com.cherrydev.cherrymarketbe.customer.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.admin.dto.CouponInfoDto;
import com.cherrydev.cherrymarketbe.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 쿠폰 코드로 쿠폰 등록하기
     *
     * @param couponCode 쿠폰 코드
     */
    @PostMapping("/register-coupon")
    @ResponseStatus(HttpStatus.OK)
    public void registerCoupon(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestParam("couponCode") String couponCode
    ) {
        customerService.registerCoupon(accountDetails, couponCode);
    }

    /**
     * 내 쿠폰 목록 조회
     */
    @GetMapping("/coupon/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CouponInfoDto>> getCouponList(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return customerService.getCouponList(accountDetails);
    }
}
