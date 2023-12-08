package com.cherrydev.cherrymarketbe.inquiry.dto;


import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import lombok.Getter;
import java.util.List;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.timeStampToString;


@Getter
public class InquiryInfoDto {
    Long inquiryId;
    Long userId;
    String code;
    String type;
    String detailType;
    String subject;
    String content;
    String status;
    String answerStatus;
    String phone;
    String createDate;
    String deleteDate;

    public InquiryInfoDto(Inquiry inquiry) {
        this.inquiryId = inquiry.getInquiryId();
        this.userId = inquiry.getUserId();
        this.code = inquiry.getCode();
        this.type = inquiry.getType().toString();
        this.detailType = inquiry.getDetailType().toString();
        this.subject = inquiry.getSubject();
        this.content = inquiry.getContent();
        this.status = inquiry.getStatus().toString();
        this.answerStatus = inquiry.getAnswerStatus().toString();
        this.phone = inquiry.getPhone();
        this.createDate = timeStampToString(inquiry.getCreateDate());
        this.deleteDate = inquiry.getDeleteDate() != null ? timeStampToString(inquiry.getDeleteDate()) : null;
    }

    public static List<InquiryInfoDto> convertToDtoList(List<Inquiry> inquiryList) {
        return inquiryList.stream()
                .map(InquiryInfoDto::new)
                .toList();
    }
}