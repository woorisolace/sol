package com.example.clean.Service;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Repository.AdminRepository;
import com.example.clean.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

  private final AdminRepository adminRepository;
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

}

