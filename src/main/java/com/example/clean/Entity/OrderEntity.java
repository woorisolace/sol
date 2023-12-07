package com.example.clean.Entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//회원별 장바구니 관리
@Entity
@Getter
@Setter
//@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orderlist")
@SequenceGenerator(name = "orderlist_SEQ",
    sequenceName = "orderlist_SEQ",
    initialValue = 1,
    allocationSize = 1)

public class OrderEntity extends BaseEntity {

  //기본키 - 주문 번호
  @Id
  @Column(name = "orderId")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderlist_SEQ")
  private Integer orderId;

  //외래키 - 회원
  //주문은 여러 명이 할 수 있음
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="user_id")
  private UserEntity userEntity;

  //외래키 - 상품
  //하나의 주문에는 여러 개의 상품이 담김
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="productId")
  private ProductEntity productEntity;

  //상품+배송비(주문 최종금액)
  private Integer totalPrice;

  //결제방법
  private String payment_method;

  private Integer product_num;

}