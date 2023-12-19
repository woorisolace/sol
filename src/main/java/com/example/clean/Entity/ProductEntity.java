package com.example.clean.Entity;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
@SequenceGenerator(
    name = "product_SEQ",
    sequenceName = "product_SEQ",
    initialValue = 1,
    allocationSize = 1
)
public class ProductEntity extends BaseEntity {

  //기본키
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_SEQ")
  private Integer productId;

  //외래키 - 이미지테이블 (하나의 상품에 이미지 여러개)
  //cascade = CascadeType.ALL : 엔터티의 상태 변화가 연관된 엔터티에 전파되는 방식
  @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
  private List<ImageEntity> productImages = new ArrayList<>();

  //외래키 - 주문테이블 (하나의 상품은 여러 주문 테이블에)
  //cascade = CascadeType.DETACH : 주문은 삭제되지 않으면서 상품만 삭제
  @OneToMany(mappedBy = "productEntity", cascade = CascadeType.DETACH)
  private List<OrderEntity> orderEntityList = new ArrayList<>();

  //카테고리유형 (전체, 생활용품, 욕실용품, 주방용품, 회원전용) - 데이터베이스에 Enum 값을 문자열로 저장하도록 지정
  @Enumerated(EnumType.STRING)
  private CategoryTypeRole categoryTypeRole;

  //상품판매상태 (SELL(판매중), STOP(판매중지), LACK(재고없음)) - 데이터베이스에 Enum 값을 문자열로 저장하도록 지정
  @Enumerated(EnumType.STRING)
  private SellStateRole sellStateRole;

  //상품이름
  @Column(name ="productName", length = 50, nullable = false)
  private String productName;

  //상품설명
  @Column(name ="productContent", length = 200, nullable = false)
  private String productContent;

  //상품상세정보
  @Lob
  @Column(name ="productDetail", length = 500)
  private String productDetail;

  //소비자가(정가)
  @Column(name ="productCost", nullable = false)
  private Integer productCost;

  //판매가
  @Column(name ="productPrice", nullable = false)
  private Integer productPrice;

  //할인율
  @Column(name ="productDis")
  private Integer productDis;

  //상품옵션
  @Column(name ="productOpt")
  private Integer productOpt;

  //상품재고수량
  @Column(name ="productCnt", nullable = false)
  private Integer productCnt;

  //상품좋아요(관심,찜)
  @Column(name ="productLike")
  private Integer productLike;

  //상품조회수
  @ColumnDefault("0")
  @Column(name ="productViewcnt")
  private Integer productViewcnt;


}

