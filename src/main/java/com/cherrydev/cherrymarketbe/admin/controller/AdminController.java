package com.cherrydev.cherrymarketbe.admin.controller;

import com.cherrydev.cherrymarketbe.admin.dto.*;
import com.cherrydev.cherrymarketbe.admin.service.AdminService;
import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.service.EmailService;
import com.cherrydev.cherrymarketbe.common.service.FileService;
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
    private final EmailService emailService;
    private final FileService fileService;

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
            final @RequestBody ModifyUserRoleRequestDto roleRequestDto
    ) {
        adminService.modifyAccountRole(roleRequestDto);
    }

    /**
     * 계정 상태 변경
     */
    @PatchMapping("/modify/account/status")
    @ResponseStatus(HttpStatus.OK)
    public void modifyAccountStatus(
            final @RequestBody ModifyUserStatusByAdminDto statusRequestDto
    ) {
        adminService.modifyAccountStatus(statusRequestDto);
    }

}