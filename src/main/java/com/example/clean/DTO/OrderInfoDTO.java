package com.example.clean.DTO;

import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderInfoDTO {

  private Integer orderInfoId;                       //구매완료번호

  private List<OrderDTO> orderDTOS;                 //주문번호

  private MemberDTO memberDTO;                       //회원정보

  private ProductDTO productDTO;                     //상품정보

}

