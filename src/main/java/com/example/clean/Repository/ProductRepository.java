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

  //상품진열(카테고리 유형별)
  @Query("SELECT p FROM ProductEntity p WHERE p.categoryTypeRole = :categoryTypeRole AND p.sellStateRole = :sellStateRole")
  Page<ProductEntity> findProductEntityByCategoryTypeRoleAndSellState (@Param("categoryTypeRole") CategoryTypeRole categoryTypeRole, @Param("sellStateRole") SellStateRole sellStateRole, Pageable pageable);

  // "MEMBERSALE"이 아니면서 판매 중인 상품 조회
  @Query("SELECT p FROM ProductEntity p WHERE p.categoryTypeRole != :MEMBERSALE AND p.sellStateRole = :SELL")
  Page<ProductEntity> findByCategoryTypeRoleNotAndSellStateRole(
      @Param("MEMBERSALE") CategoryTypeRole MEMBERSALE, @Param("SELL") SellStateRole SELL, Pageable pageable);

  //상품진열(판매상태) - SELL(판매중)
  @Query("SELECT p FROM ProductEntity p WHERE p.sellStateRole = :SELL")
  Page<ProductEntity> findAllByStateRole (@Param("SELL") SellStateRole SELL, Pageable pageable);

  //상품진열(카테고리) - MEMBERSALE(회원전용)
  @Query("SELECT p FROM ProductEntity p WHERE p.categoryTypeRole = :MEMBERSALE")
  Page<ProductEntity> findByCategoryTypeRole(@Param("MEMBERSALE") CategoryTypeRole MEMBERSALE, Pageable pageable);

  //아이디와 카테고리값
  @Query("SELECT p FROM ProductEntity p WHERE p.productId <> :productId AND p.categoryTypeRole = :categoryTypeRole")
  List<ProductEntity> findRelatedProductsByCategoryTypeRole(@Param("productId") Integer productId, @Param("categoryTypeRole") CategoryTypeRole categoryTypeRole);



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
