package com.example.clean.ProductTest;

import com.example.clean.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  /*
  @Test
  public void insert() throws Exception {

    for (int i = 1; i <= 30; i++) {
      ProductEntity productEntity = ProductEntity.builder()
          .productName("상품명" + i)
          .productContent("상품설명")
          .productDetail("상품상세정보")
          .productCost(30000)
          .productPrice(27000)
          .productDis(15)
          .productOpt(1)
          .productCnt(300)
          .build();
      productRepository.save(productEntity);
    }
  }
  */

  @Test
  public void deleteAll() throws Exception {
    productRepository.deleteAll();
  }

}

