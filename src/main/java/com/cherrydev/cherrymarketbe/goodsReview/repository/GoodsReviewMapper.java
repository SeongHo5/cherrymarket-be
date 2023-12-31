package com.cherrydev.cherrymarketbe.goodsReview.repository;

import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsReviewMapper {
    //저장
    void save(GoodsReview goodsReview);

    //업데이트
    void update(GoodsReview goodsReview);

    //삭제
    void delete(Long ordersId, Long goodsId);

    //조회
    GoodsReview findReivew(Long ordersId, Long goodsId);

    //전체조회
    List<GoodsReviewInfoDto> findAll();

    //전체조회 - 상품별
    List<GoodsReviewInfoDto> findAllByGoodsId(Long goodsId);

    // 전체조회 - 주문별
    List<GoodsReviewInfoDto> findAllByOrderId(Long ordersId);

    // 전체조회 - 유저
    List<GoodsReviewInfoDto> findAllByUserId(Long userId);

    List<GoodsReviewInfoDto> findAllMyList(Long accountId);

    boolean existReview(GoodsReview goodsReview);
    boolean existReview(Long ordersId, Long goodsId);

    boolean checkDeliveryStatus(GoodsReview goodsReview);


    boolean getUserId(Long ordersId, Long goodsId, Long userId);

    boolean getUserIdByCode(String code, Long userId);

}
