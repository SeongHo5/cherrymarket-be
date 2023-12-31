package com.cherrydev.cherrymarketbe.goodsReview.entity;

import com.cherrydev.cherrymarketbe.goodsReview.enums.GoodsReviewBest;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class GoodsReview {

    private Long reviewId;
    private Long ordersId;
    private Long goodsId;
    private Long userId;
    private String code;
    private GoodsReviewBest isBest;
    private String subject;
    private String content;
    private DisplayStatus status;
    private Timestamp createDate;
    private Timestamp deleteDate;

    @Builder
    public GoodsReview(Long reviewId, Long ordersId, Long goodsId, Long userId,
                       String code, GoodsReviewBest isBest, String subject,
                       String content, DisplayStatus status, Timestamp createDate,
                       Timestamp deleteDate) {
        this.reviewId = reviewId;
        this.goodsId = goodsId;
        this.ordersId = ordersId;
        this.userId = userId;
        this.code = code;
        this.isBest = isBest;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.createDate = createDate;
        this.deleteDate = deleteDate;
    }

    public void updateStatus(DisplayStatus status) {
        this.status = status;
    }
}
