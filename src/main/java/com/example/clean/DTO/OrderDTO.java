package com.example.clean.DTO;

import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class OrderDTO {

  private Integer orderId;                 //주문번호

  private UserEntity userEntity;          //회원정보

  private ProductEntity productEntity;    //상품정보

  private LocalDateTime reDate;           //주문날짜

  //private Integer user_id;                // user_id로 변경

  private Integer totalPrice;             //상품+배송비(주문 최종금액)

  private String payment_method;          //결제방법

  private Integer product_num;


}

