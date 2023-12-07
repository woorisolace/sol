package com.example.clean.Service;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.DTO.OrderDTO;
import com.example.clean.DTO.OrderInfoDTO;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Entity.OrderEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Repository.MemberRepository;
import com.example.clean.Repository.OrdertRepository;
import com.example.clean.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

  private final OrdertRepository ordertRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper = new ModelMapper();

  //구매폼 조회
  //userId와 productId를 받아 사용자와 제품 정보 받아오기
  //
  public OrderDTO orderForm(String userId, Integer productId, Integer productNum) throws Exception {

    //로그인한 회원의 이메일로 회원 정보 가져오기
    UserEntity userEntity = memberRepository.findByEmail(userId);
    log.info("Service_아이디 확인완료: {}", userId);
    if (userEntity == null) {
      log.info("Service_이메일 없음. 로그인페이지로 이동 필요");
      return null;
    }
    
    //상품조회
    ProductEntity productEntity = productRepository.findByProductId(productId);
    log.info("Service_상품번호 확인완료: {}", productId);


    /*// 구매 양식 조회
    OrderEntity orderEntity = ordertRepository.findByUserEntityAndProductEntity(userEntity, productEntity);
    log.info("Service_구매양식 조회 확인완료 찍혔으나 오류========================================");
    if (orderEntity == null) {
      log.info("Service_구매양식이 없습니다.");
      return null;
    }*/

    // 구매폼에 회원정보, 상품정보 입력
   // OrderDTO orderDTO = modelMapper.map(orderEntity, OrderDTO.class);
    OrderDTO orderDTO=new OrderDTO();
    orderDTO.setUserEntity(userEntity);
    orderDTO.setProductEntity(productEntity);


    return orderDTO;
  }


  // 주문(구매)생성 -> 완료 버튼 누르기
  // OrderInfoDTO: 회원정보, 상품정보, 결제정보 등을 담아서 반환
  public OrderDTO orderInfo(String userId, Integer productId, Integer product_num) throws Exception {

    OrderDTO orderDTO = orderForm(userId, productId,product_num);

    if (orderDTO == null || orderDTO.getUserEntity() == null || orderDTO.getProductEntity() == null) {
      throw new IllegalArgumentException("주문 정보가 올바르게 구성되지 않았습니다.");
    }

    // OrderEntity에 회원, 제품 + 배송정보, 결제 정보 등 추가
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setUserEntity(orderDTO.getUserEntity());
    orderEntity.setProductEntity(orderDTO.getProductEntity());
    orderEntity.setProduct_num(orderDTO.getProduct_num());


    // 주문 정보 저장
    OrderEntity savedOrderEntity = ordertRepository.save(orderEntity);

    OrderDTO orderDTO1 = modelMapper.map(savedOrderEntity, OrderDTO.class);
    return orderDTO1;

/*
    // OrderInfoDTO 생성 및 설정
    OrderInfoDTO resultOrderInfoDTO = new OrderInfoDTO();
    resultOrderInfoDTO.setOrderInfoId(savedOrderEntity.getOrderId());

    // 주문 정보 저장
    resultOrderInfoDTO.setOrderDTOS(Collections.singletonList(orderDTO));

    // 회원 및 상품 정보 저장
    resultOrderInfoDTO.setMemberDTO(modelMapper.map(orderDTO.getUserEntity(), MemberDTO.class));
    resultOrderInfoDTO.setProductDTO(modelMapper.map(orderDTO.getProductEntity(), ProductDTO.class));

    return resultOrderInfoDTO;*/
  }



  // 구매완료 내용 보기
  //orderInfoId로 주문 정보 가져오기기
 public OrderInfoDTO getOrderSuccess(Integer orderInfoId) throws Exception {

    // 주문 정보 조회
    OrderEntity orderEntity = ordertRepository.findById(orderInfoId).orElse(null);
    log.info("Service_구매완료: {}", orderEntity);

    // 주문 정보가 없으면 예외 처리 또는 null 반환
    if (orderEntity == null) {
      throw new IllegalArgumentException("주문 정보가 없습니다.");
    }

    // OrderInfoDTO가져와서 구매정보 출력
   OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
   orderInfoDTO.setOrderInfoId(orderEntity.getOrderId());
   orderInfoDTO.setOrderInfoId(orderEntity.getProduct_num());
   orderInfoDTO.setMemberDTO(modelMapper.map(orderEntity.getUserEntity(), MemberDTO.class));
   orderInfoDTO.setProductDTO(modelMapper.map(orderEntity.getProductEntity(), ProductDTO.class));

   return orderInfoDTO;
  }
}

