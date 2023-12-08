package com.example.clean.DTO;

import com.example.clean.Entity.OrderEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderDTO {

  private Integer orderId;                 //주문번호

  private UserEntity userEntity;          //회원정보

  private ProductEntity productEntity;    //상품정보

  private LocalDateTime reDate;           //주문날짜

  //private Integer user_id;                // user_id로 변경

  private Integer totalPrice;             //상품+배송비(주문 최종 결제금액)

  private String payment_method;          //결제방법

  private Integer productNum;             //회원 구매수량

  private Integer orderCount;             // 주문 개수 조회

  private Integer orderNumber;            // 주문 목록 번호


  //이미지 관리 DTO
  private List<ImageDTO> imageDTOs;

  //이미지파일 처리
  private List<MultipartFile> images;


}
