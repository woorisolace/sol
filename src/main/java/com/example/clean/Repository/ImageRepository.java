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
