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
@Builder
public class OrderDTO {

  private Integer orderId;                 //주문번호

  private UserEntity userEntity;           //회원정보
  private Integer userId;

  private ProductEntity productEntity;     //상품정보
  private ProductDTO productDTO;
  private Integer productId;
  private List<ProductDTO> products;

  private List<ImageDTO> imageDTOs;

  private LocalDateTime reDate;            //주문날짜

  //주문시 추가 되는 부분
  private Integer orderNum;                 //회원별 주문 목록 숫자
  
  private Integer productNum;               //회원 구매수량

  private Integer productTotal;             //합계 = 판매가 * 구매수량

  private Integer productDelivery;          //배송비

  private Integer orderPrice;               //상품+배송비(주문 최종금액)

  private String paymentMethod;             //결제방법

}

//이미지 관리 DTO
//private List<ImageDTO> imageDTOs;

//이미지파일 처리
//private List<MultipartFile> images;