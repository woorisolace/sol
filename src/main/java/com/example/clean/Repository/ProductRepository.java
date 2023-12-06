package com.example.clean.Repository;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

  //상품 고유번호
  ProductEntity findByProductId(Integer productId);

  //카테고리별 상품 수
  long countByCategoryTypeRole(CategoryTypeRole categoryTypeRole);

  //조회수가 높은 베스트 상품순 n개 노출
  List<ProductEntity> findTopByOrderByProductViewcntDesc();



  //상품진열(판매상태) - SELL(판매중)
  @Query("SELECT p FROM ProductEntity p WHERE p.sellStateRole = :SELL")
  Page<ProductEntity> findAllByStateRole (@Param("SELL") SellStateRole SELL, Pageable pageable);

  //상품진열(카테고리) - MEMBERSALE(회원전용)제외
  @Query("SELECT p FROM ProductEntity p WHERE p.categoryTypeRole = :MEMBERSALE")
  Page<ProductEntity> findByCategoryTypeRole(@Param("MEMBERSALE") CategoryTypeRole MEMBERSALE, Pageable pageable);



  //검색부분
  //상품명으로 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:keyword%")
  Page<ProductEntity> findProductName(@Param("keyword") String keyword, Pageable pageable);

  // 상품명 또는 상품설명으로 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:keyword% OR p.productContent LIKE %:keyword%")
  Page<ProductEntity> findProNameContent(@Param("keyword") String keyword, Pageable pageable);

  // 카테고리 유형으로 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.categoryTypeRole = :categoryType")
  Page<ProductEntity> findByCategoryType(@Param("categoryType") CategoryTypeRole categoryTypeRole, Pageable pageable);

  // 판매 상태로 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.sellStateRole = :sellState")
  Page<ProductEntity> findBySellStateRole(@Param("sellState") SellStateRole sellStateRole, Pageable pageable);



}
