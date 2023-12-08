package com.example.clean.Repository;

import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.OrderEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//order테이블이 주인
//user, product는 보조

@Repository
public interface OrdertRepository extends JpaRepository<OrderEntity, Integer> {

 //주문번호 찾기
 OrderEntity findByOrderId(Integer oderId);

 // 회원 찾기
 List<OrderEntity> findAllByUserEntity(UserEntity userEntity);

 // 상품 찾기
 List<OrderEntity> findAllByProductEntity(ProductEntity productEntity);

 // 주문- 회원 및 주문-상품 관계에서 주문 찾기
 OrderEntity findByUserEntityAndProductEntity(UserEntity userEntity, ProductEntity productEntity);

 //해당 유저의 구매 이력을 페이징 정보에 맞게 조회
 @Query("select o from OrderEntity o " + "where o.userEntity.email = :email ")
 Page<OrderEntity> findOrderEntities(@Param("email") String email, Pageable pageable);

 //해당 유저의 주문 개수
 @Query("select count(o) from OrderEntity o " + "where o.userEntity.email = :email ")
 Integer countOrder(@Param("email") String email);

 //모든회원 주문조회
 Page<OrderEntity> findAll(Pageable pageable);
}