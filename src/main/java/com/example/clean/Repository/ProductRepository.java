package com.example.clean.Repository;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

  ProductEntity findByProductId(Integer productId);

  //검색부분
  // 상품명으로 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:keyword%")
  Page<ProductEntity> findProductName(@Param("keyword") String keyword, Pageable pageable);

  // 상품명 또는 상품설명으로 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:keyword% OR p.productContent LIKE %:keyword%")
  Page<ProductEntity> findProNameContent(@Param("keyword") String keyword, Pageable pageable);

  // 카테고리 유형으로 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.categoryTypeRole = :categoryType")
  Page<ProductEntity> findByCategoryType(@Param("categoryType") CategoryTypeRole categoryTypeRole, Pageable pageable);

  // 판매 상태로 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.sellStateRole = :sellStateRole")
  Page<ProductEntity> findBySellStateRole(@Param("sellStateRole") SellStateRole sellStateRole, Pageable pageable);

  // 검색 쿼리
  @Query("SELECT u FROM ProductEntity u WHERE " +
      "(:type IS NULL OR " +
      "(:type = 'n' AND u.productName LIKE %:keyword%) OR " +
      "(:type = 'nc' AND (u.productName LIKE %:keyword% OR u.productContent LIKE %:keyword%)) OR " +
      "(:type = 's' AND u.sellStateRole = :sellState) OR " +
      "(:type = 'ca' AND u.categoryTypeRole = :categoryType))")
  Page<ProductEntity> findBySearch(
      @Param("type") String type,
      @Param("keyword") String keyword,
      @Param("sellState") SellStateRole sellState,
      @Param("categoryType") CategoryTypeRole categoryType,
      Pageable pageable);

}











/*

  // 이미지들을 조회하는 메서드
  @Query("SELECT i FROM ImageEntity i JOIN i.productEntity p WHERE p.productId = :productId")
  List<ImageEntity> findImagesByProductId (@Param("productId") Integer productId);

 */