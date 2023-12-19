package com.example.clean.Service;

import com.example.clean.DTO.ImageDTO;
import com.example.clean.DTO.MemberDTO;
import com.example.clean.DTO.OrderDTO;
import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.OrderEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Repository.AdminRepository;
import com.example.clean.Repository.MemberRepository;
import com.example.clean.Repository.OrderRepository;
import com.example.clean.Util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

  //파일이 저장될 경로
  @Value("${imgUploadLocation}")
  private String imgUploadLocation;

  //파일 저장을 위한 클래스
  private final S3Uploader s3Uploader;

  private final FileService fileService;
  private final ImageService imageService;

  private final AdminRepository adminRepository;
  private final OrderRepository orderRepository;
  private final ModelMapper modelMapper = new ModelMapper();


  // 관리자 - 회원 조회(회원 목록 페이지)
  public Page<MemberDTO> memberList(MemberDTO memberDTO, Integer id, String  oauthType, Pageable page) throws Exception {

    int currentPage = page.getPageNumber() - 1;
    int pageLimit = 10;

    Pageable pageable = PageRequest.of(currentPage, pageLimit, Sort.by(Sort.Direction.DESC, "id")); // 회원번호 역순으로 정렬

    Page<UserEntity> userEntityPage;


    //oauthType이 비어있거나 null일 경우와 그렇지 않은 경우에 따라 다른 조회
    if (oauthType == null || oauthType.isEmpty()) {
      userEntityPage = adminRepository.findAll(pageable);
    } else {
      userEntityPage = adminRepository.findByOauthTypeOrderByReDateDesc(oauthType, pageable);
    }

  Page<MemberDTO> memberDTOPage = userEntityPage.map(data -> MemberDTO.builder()
      .id(data.getId())
      .name(data.getNickname())
      .email(data.getEmail())
      .password(data.getPassword())
      .tel(data.getTel())
      .sample6_postcode(data.getSample6_postcode())
      .sample6_address(data.getSample6_address())
      .sample6_detailAddress(data.getSample6_detailAddress())
      .sample6_extraAddress(data.getSample6_extraAddress())
      .createDate(data.getReDate())
      .oauthType(data.getOauthType() == null || data.getOauthType().isEmpty() ? "HOME" : data.getOauthType())
      .build());

        return memberDTOPage;
  }


  //리스트 출력을 위해 이미지 불러오기
  private List<ImageDTO> getImagesForOrderEntity(OrderEntity orderEntity) {
    if(orderEntity != null && orderEntity.getProductEntity() != null && orderEntity.getProductEntity().getProductImages() != null){
      return orderEntity.getProductEntity().getProductImages().stream()
              .map(imageEntity -> modelMapper.map(imageEntity, ImageDTO.class))
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }


  //관리자-회원구매내역
  public Page<OrderDTO> getAllOrders(Pageable page) throws Exception {

    int currentPage = page.getPageNumber()-1;
    int pageLimit = 10;

    // 현재 페이지 번호와 페이지 크기를 이용하여 Pageable 객체를 생성
    Pageable pageable = PageRequest.of(currentPage, pageLimit, Sort.by(Sort.Direction.DESC, "orderId"));

    // 주문 내역을 페이징 처리하여 조회
    Page<OrderEntity> orderEntityPage = orderRepository.findAll(pageable);

    // 조회된 주문 내역을 OrderDTO로 변환하여 반환
    Page<OrderDTO> orderDTOPage = orderEntityPage.map(orderEntity -> OrderDTO.builder()
        .orderId(orderEntity.getOrderId())
        .userEntity(orderEntity.getUserEntity())
        .reDate(orderEntity.getReDate())
        .productEntity(orderEntity.getProductEntity())
        .orderPrice(orderEntity.getOrderPrice())
        .orderNum(orderEntity.getOrderNum())
        .productDelivery(orderEntity.getProductDelivery())
        .imageDTOs(getImagesForOrderEntity(orderEntity))
        .reDate(orderEntity.getReDate())
        .build());

    return orderDTOPage;
  }

}

