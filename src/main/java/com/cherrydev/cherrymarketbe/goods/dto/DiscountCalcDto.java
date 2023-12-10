package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DiscountCalcDto {

    Long goodsId;

    String goodsName;

    String description;

    int price;

    Integer discountRate;

    Integer discountedPrice;
}