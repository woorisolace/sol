package com.example.clean.DTO;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

  private Integer imageId;          // 이미지 고유번호

  private String imageFile;         // 이미지 파일이름

  private Integer imageType;         // 이미지 종류 (대표이미지=0, 서브이미지=1, 상세이미지=2)

  private Integer productid;        // 상품번호(외래키)

}