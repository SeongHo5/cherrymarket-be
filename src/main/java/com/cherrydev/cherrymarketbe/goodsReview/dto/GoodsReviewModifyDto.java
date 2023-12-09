package com.cherrydev.cherrymarketbe.goodsReview.dto;

import lombok.Getter;

@Getter
public class GoodsReviewModifyDto {

    Long reviewId;
    Long ordersId;
    Long goodsId;
    String code;
    String subject;
    String content;

    public GoodsReviewModifyDto(Long reviewId, Long ordersId, Long goodsId,
                                String code, String subject,
                                String content) {
        this.reviewId = reviewId;
        this.ordersId = ordersId;
        this.goodsId = goodsId;
        this.code = code;
        this.subject = subject;
        this.content = content;
    }
}