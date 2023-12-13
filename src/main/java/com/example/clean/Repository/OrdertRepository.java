package com.example.clean.Repository;

import com.example.clean.Entity.OrderEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
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

 //@Query("SELECT o FROM OrderEntity o WHERE o.userEntity = :userEntity AND o.productEntity = :productEntity")
 //List<OrderEntity> findByUserEntityAndProductEntity(@Param("userEntity") UserEntity userEntity, @Param("productEntity") ProductEntity productEntity);


}
