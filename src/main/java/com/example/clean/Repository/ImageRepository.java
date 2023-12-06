package com.example.clean.Repository;


import com.example.clean.Entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

  //상품번호에 해당하는 이미지 조회
  @Query("SELECT a FROM ImageEntity a WHERE a.productEntity.productId = :productId")
  List<ImageEntity> findAllByProductId(@Param("productId") Integer productId);

}

/*
상품 등록 시 이미지 정보를 받아오기

상품 등록 시에 이미지의 정보(파일명, 경로 등)를 함께 받아와서,
이미지 정보를 활용하여 이미지를 등록하고 이미지 번호를 얻어옵니다.
이미지 등록은 상품 등록과 함께 이루어지므로,
이미지 정보를 받아올 때 이미지 번호를 얻어오는 추가적인 호출이 필요하지 않습니다.
이미지 정보를 포함한 상품 등록 요청을 받을 수 있는 API 또는 서비스를 구성
 */