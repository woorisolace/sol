package com.example.clean.Service;

import com.example.clean.DTO.*;
import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.OrderEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Repository.MemberRepository;
import com.example.clean.Repository.OrderRepository;
import com.example.clean.Repository.ProductRepository;
import com.example.clean.Util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

  //파일이 저장될 경로
  @Value("${imgUploadLocation}")
  private String imgUploadLocation;

  //파일 저장을 위한 클래스
  private final S3Uploader s3Uploader;
  private final FileService fileService;
  private final ImageService imageService;

  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper = new ModelMapper();    //DTO(데이터 전송 객체)와 엔터티 간 변환에 사용

  // 구매 양식 조회
  public OrderDTO orderForm(String userId, Integer productId, Integer productNum) throws Exception {

    //로그인한 회원의 이메일에 해당하는 사용자 정보를 데이터베이스에서 조회
    //조회된 사용자 정보는 UserEntity 객체에 저장
    UserEntity userEntity = memberRepository.findByEmail(userId);

    //상품 아이디(productId)에 해당하는 상품 정보를 데이터베이스에서 조회
    //조회된 상품 정보는 ProductEntity 객체에 저장
    ProductEntity productEntity = productRepository.findByProductId(productId);


    //주문테이블 조회 후, 주문에 필요한 내용 추가
    //구매수량, 결제방식은 controller에서 받아와야 함 (controller : 입력 처리 및 유효성 검사는 주로 컨트롤러 레이어의 역할)
    Integer productPrice = productEntity.getProductPrice();         //제품가격
    Integer productTotal = productPrice * productNum;               //제품가격 * 구매수량
    Integer productDelivery = (productTotal >= 50000) ? 0 : 3000;   //배송비 (50,000원 이상 구매시 무료배송)
    Integer orderPrice = productTotal + productDelivery;            //배송비 + 상품금액 = 최종 결제 금액


    //OrderDTO에 값 설정
    //구매 완료 페이지에 필요한 정보만을 담아서 전달하는 데이터 전송 객체(DTO)를 사용
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setUserEntity(userEntity);
    orderDTO.setProductEntity(productEntity);
    orderDTO.setUserId(userEntity.getId());
    orderDTO.setProductId(productEntity.getProductId());
    orderDTO.setProductNum(productNum);
    orderDTO.setProductTotal(productTotal);
    orderDTO.setProductDelivery(productDelivery);
    orderDTO.setOrderPrice(orderPrice);

    return orderDTO;
  }


  //주문(구매)생성 -> 완료 버튼 누르기 (이때 orderId가 생성됨)
  //회원정보, 상품정보, 결제정보 등을 담아서 반환
  public OrderDTO orderInfo(String userId, Integer productId, Integer productNum, String paymentMethod) throws Exception {
    //orderForm을 통해서 주문에 필요한 기본 정보를 가져옴
    OrderDTO orderDTO = orderForm(userId, productId, productNum);

    // 주문 정보가 올바르지 않은 경우
    if (orderDTO == null || orderDTO.getUserEntity() == null || orderDTO.getProductEntity() == null) {
      throw new IllegalArgumentException("주문 정보가 올바르게 구성되지 않았습니다.");
    }
    log.info("Controller_paymentMethod_before: {}", paymentMethod);

    // 쉼표를 제거하여 paymentMethod 값을 설정
    paymentMethod = removeComma(paymentMethod);

    //주문 정보를 newOrderEntity 저장 (활용도를 높이기 위해)
    OrderEntity newOrderEntity = createOrderEntity(orderDTO, paymentMethod);
    OrderEntity savedOrderEntity = orderRepository.save(newOrderEntity);

    // 저장된 OrderEntity를 OrderDTO로 매핑하여 반환
    OrderDTO savedOrderDTO = modelMapper.map(savedOrderEntity, OrderDTO.class);
    savedOrderDTO.setPaymentMethod(paymentMethod); // 결제방법 설정

    return savedOrderDTO;
  }


  // 쉼표를 제거하는 유틸리티 메서드
  private String removeComma(String input) {
    if (input != null) {
      return input.replace(",", "");
    }
    return null; // 혹은 다른 적절한 기본값으로 설정
  }


  //createOrderEntity를 통해 새로운 주문 엔티티를 생성하고 저장
  private OrderEntity createOrderEntity(OrderDTO orderDTO, String paymentMethod) throws Exception {
    OrderEntity newOrderEntity = new OrderEntity();
    newOrderEntity.setUserEntity(orderDTO.getUserEntity());
    newOrderEntity.setProductEntity(orderDTO.getProductEntity());
    newOrderEntity.setProductNum(orderDTO.getProductNum());
    newOrderEntity.setProductTotal(orderDTO.getProductTotal());
    newOrderEntity.setProductDelivery(orderDTO.getProductDelivery());
    newOrderEntity.setOrderPrice(orderDTO.getOrderPrice());
    newOrderEntity.setPaymentMethod(paymentMethod);                   // 직접 받아온 결제 방식 설정
    newOrderEntity.setOrderId(orderDTO.getOrderId());                 //주문번호 생성

    return newOrderEntity;
  }


  //구매완료 내용 보기
  //orderDTO로 주문 정보 가져오기기 (주문 1회에 여러 상품이 담겨도 되기 때문에 굳이 oderInfoDTO사용 필요 없음)
  public OrderDTO getOrderSuccess(Integer orderId) throws Exception {

    // 주문 정보 조회
    OrderEntity orderEntity = orderRepository.findByOrderId(orderId);

    // 주문 정보가 없으면 예외 처리 또는 null 반환
    if (orderEntity == null) {
      throw new IllegalArgumentException("주문 정보가 없습니다.");
    }

    // OrderEntity를 OrderDTO로 매핑하여 반환
    OrderDTO orderDTO = modelMapper.map(orderEntity, OrderDTO.class);

    return modelMapper.map(orderEntity, OrderDTO.class);

  }


  //마이페이지에 구매내역 출력
  public Page<OrderDTO> myOrderList(String email, Pageable page) throws Exception {

    int currentPage = page.getPageNumber() - 1;
    int orderList = 10;
    Pageable pageable = PageRequest.of(currentPage, orderList, Sort.by(Sort.Direction.DESC, "orderId"));

    //주문 목록 페이징 처리하여 조회
    Page<OrderEntity> orderEntityPage = orderRepository.findOrderEntities(email, pageable);

    // OrderDTO 객체들을 담을 리스트
    List<OrderDTO> orderDTOList = new ArrayList<>();

    // 회원 이메일에 해당하는 주문 목록만 가져오기 (역순으로 정렬)
    List<OrderEntity> userOrderEntities = orderEntityPage.getContent()
        .stream()
        .sorted(Comparator.comparing(OrderEntity::getOrderId).reversed())
        .collect(Collectors.toList());

    int orderNum = userOrderEntities.size(); // 구매 순서 번호 초기화


    for (OrderEntity entity : userOrderEntities) {
      OrderDTO orderDTO = OrderDTO.builder()
          .productEntity(entity.getProductEntity())
          .userEntity(entity.getUserEntity())
          .orderId(entity.getOrderId())
          .orderPrice(entity.getOrderPrice())
          .orderNum(orderNum--)  // 역순으로 구매 순서 번호 감소
          .imageDTOs(getImagesForOrderEntity(entity))
          .reDate(entity.getReDate())
          .build();

      orderDTOList.add(orderDTO);
    }

    return new PageImpl<>(orderDTOList, pageable, orderEntityPage.getTotalElements());
  }

  //리스트 출력을 위해 이미지 불러오기
  private List<ImageDTO> getImagesForOrderEntity(OrderEntity orderEntity) throws Exception {
    return orderEntity.getProductEntity().getProductImages().stream()
        .map(imageEntity -> {
          ImageDTO imageDTO = modelMapper.map(imageEntity, ImageDTO.class);

          // S3에서 이미지 URL 가져오기
          String imageUrl = s3Uploader.getImageUrl("static", imageEntity.getImageFile());
          imageDTO.setImageFile(imageEntity.getImageFile());

          System.out.println("Image URL: " + imageUrl);

          return imageDTO;
        })
        .collect(Collectors.toList());
  }

}