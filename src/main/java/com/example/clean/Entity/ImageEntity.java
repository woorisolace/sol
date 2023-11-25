package com.example.clean.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "image")
@SequenceGenerator(
    name = "image_SEQ",
    sequenceName = "image_SEQ",
    initialValue = 1,
    allocationSize = 1
)

public class ImageEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="image_SEQ")
  private Integer imageId;

  @Column(name = "imageFile")
  private String imageFile;

  //대표이미지=0, 서브이미지=1, 상세이미지=2
  @Column(name = "imageType")
  private Integer imageType;

  //외래키 (productId) - 상품테이블과 조인 (여러 개의 이미지가 하나의 상품에 매핑)
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "productId")
  private ProductEntity productEntity;

}


/*
enum을 사용하지 않고 이미지타입 분류 하는 방법
ImageEntity imageEntity = new ImageEntity();
imageEntity.setImageType(0); // 대표이미지


변수명에 "-" 가능한 사용하지 말 것!

 */

