package com.example.clean.Service;

import com.example.clean.DTO.*;
import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.OrderEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Repository.ImageRepository;
import com.example.clean.Repository.MemberRepository;
import com.example.clean.Repository.OrdertRepository;
import com.example.clean.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
  public OrderDTO orderForm(String userId, Integer productId) throws Exception {

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

    // 구매폼에 회원정보, 상품정보 입력
    // OrderDTO orderDTO = modelMapper.map(orderEntity, OrderDTO.class);
    OrderDTO orderDTO=new OrderDTO();
    orderDTO.setUserEntity(userEntity);
    orderDTO.setProductEntity(productEntity);


    return orderDTO;
  }


  // 주문(구매)생성 -> 완료 버튼 누르기
  // OrderInfoDTO: 회원정보, 상품정보, 결제정보 등을 담아서 반환
  public OrderDTO orderInfo(String userId, Integer productId, Integer productNum, Integer totalPrice, String payment_method) throws Exception {

    OrderDTO orderDTO = orderForm(userId, productId);

    if (orderDTO == null || orderDTO.getUserEntity() == null || orderDTO.getProductEntity() == null) {
      throw new IllegalArgumentException("주문 정보가 올바르게 구성되지 않았습니다.");
    }

    // OrderEntity에 회원, 제품 + 배송정보, 결제 정보 등 추가
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setUserEntity(orderDTO.getUserEntity());
    orderEntity.setProductEntity(orderDTO.getProductEntity());
    orderEntity.setTotalPrice(totalPrice);
    orderEntity.setPayment_method(payment_method);
    orderEntity.setProductNum(productNum);

    // 주문 정보 저장
    OrderEntity savedOrderEntity = ordertRepository.save(orderEntity);

    OrderDTO orderDTO1=modelMapper.map(savedOrderEntity, OrderDTO.class);
    return orderDTO1;

  }



  // 구매완료 내용 보기
  //orderInfoId로 주문 정보 가져오기기
  public OrderDTO getOrderSuccess(Integer orderId) throws Exception {

    // 주문 정보 조회
    OrderEntity orderEntity = ordertRepository.findById(orderId).orElse(null);
    log.info("Service_구매완료: {}", orderEntity);

    // 주문 정보가 없으면 예외 처리 또는 null 반환
    if (orderEntity == null) {
      throw new IllegalArgumentException("주문 정보가 없습니다.");
    }

    // OrderDTO가져와서 구매정보 출력
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setOrderId(orderEntity.getOrderId());
    orderDTO.setUserEntity(orderEntity.getUserEntity());
    orderDTO.setProductEntity(orderEntity.getProductEntity());

    return orderDTO;
  }

  //각 상품에 이미지테이블 전달
  private List<ImageDTO> mapOrderImagesToDTOs(List<ImageEntity> imagesEntities) {
    List<ImageDTO> imageDTOs = new ArrayList<>();

    for (ImageEntity imageEntity : imagesEntities) {
      ImageDTO imageDTO = modelMapper.map(imageEntity, ImageDTO.class); //  new ImageDTO(/* Populate with necessary fields */);
      imageDTOs.add(imageDTO);
    }
    return imageDTOs;
  }

  // OrderEntity를 기반으로 OrderDTO 생성
  public OrderDTO fromEntity(OrderEntity orderEntity, Integer orderCount) {
    return OrderDTO.builder()
            .orderId(orderEntity.getOrderId())
            .userEntity(orderEntity.getUserEntity())
            .productEntity(orderEntity.getProductEntity())
            .reDate(orderEntity.getReDate())
            .totalPrice(orderEntity.getTotalPrice())
            .payment_method(orderEntity.getPayment_method())
            .productNum(orderEntity.getProductNum())
            .orderCount(orderCount)
            .orderNumber(orderEntity.getOrderId()) // 이 부분 추가
            .imageDTOs(mapOrderImagesToDTOs(orderEntity.getProductEntity().getProductImages()))
            .build();
  }

  //회원아이디에 맞는 구매내역 확인
  // 해당 유저의 구매 이력을 페이징 정보에 맞게 조회하는 메서드
  @Transactional(readOnly = true)
  public Page<OrderDTO> getOrderList(String email, Pageable page) throws Exception {
    try {
      int currentPage = page.getPageNumber() - 1;
      int pageLimit = 10;

      // 현재 페이지 번호와 페이지 크기를 이용하여 Pageable 객체를 생성
      Pageable pageable = PageRequest.of(currentPage, pageLimit, Sort.by(Sort.Direction.DESC, "orderId"));

      // 주문 목록 페이징 처리하여 조회
      Page<OrderEntity> orderEntities = ordertRepository.findOrderEntities(email, pageable);

      // 주문 개수 조회
      Integer orderCount = ordertRepository.countOrder(email);

      // 주문 목록 번호 역순으로
      List<OrderDTO> orderDTOList = orderEntities.stream()
              .map(orderEntity -> fromEntity(orderEntity, orderCount - orderEntity.getOrderId() + 1))
              .collect(Collectors.toList());

      return new PageImpl<>(orderDTOList, pageable, orderEntities.getTotalElements());


      // OrderDTO로 변환하여 반환
      //return orderEntities.map(orderEntity -> fromEntity(orderEntity, orderCount));
    } catch (Exception e) {
      // 예외 처리
      e.printStackTrace(); // 실제로는 로깅 등으로 변경해야 함
      throw new RuntimeException("주문 내역을 불러오는 중 오류가 발생했습니다.");
    }
  }

  //관리자 페이지에서 모든회원의 모든구매내역 확인
  public Page<OrderDTO> getAllOrders(Pageable page) throws Exception {

    int currentPage = page.getPageNumber() - 1;
    int pageLimit = 10;

    // 현재 페이지 번호와 페이지 크기를 이용하여 Pageable 객체를 생성
    Pageable pageable = PageRequest.of(currentPage, pageLimit, Sort.by(Sort.Direction.DESC, "orderId")); // 주문아이디 생성 역순

    // 주문 내역을 페이징 처리하여 조회
    Page<OrderEntity> orderEntityPage = ordertRepository.findAll(pageable);

    // 조회된 주문 내역을 OrderDTO로 변환하여 반환
    Page<OrderDTO> orderDTOPage = orderEntityPage.map(orderEntity -> OrderDTO.builder()
            .orderId(orderEntity.getOrderId())
            .userEntity(orderEntity.getUserEntity())
            .reDate(orderEntity.getReDate())
            .productEntity(orderEntity.getProductEntity())
            .totalPrice(orderEntity.getTotalPrice())
            .imageDTOs(mapOrderImagesToDTOs(orderEntity.getProductEntity().getProductImages()))
            .build());

    return orderDTOPage;
  }

}
