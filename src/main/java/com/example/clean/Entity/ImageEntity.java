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

  public String getImageFile() {
    // 파일 이름을 포함한 완전한 파일 경로를 반환하도록 수정
    return this.imageFile; // 실제 필드에 따라 수정
  }

}
