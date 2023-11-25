package com.example.clean.DTO;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import jdk.jfr.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

  //기본키
  private Integer productId;

  //상품이름
  @NotEmpty(message = "상품이름은 필수 입력입니다.")
  private String productName;

  //상품설명
  @NotEmpty(message = "상품설명은 필수 입력입니다.")
  private String productContent;

  //상품상세정보
  private String productDetail;

  //소비자가(정가)
  @NotNull(message = "가격은 필수 입력입니다.")
  private Integer productCost;

  //판매가(할인가)
  @NotNull(message = "가격은 필수 입력입니다.")
  private Integer productPrice;

  //할인율 = (정가 -할인가)/정가
  private Integer productDis;
  
  //상품옵션
  private Integer productOpt;


  @NotNull(message = "상품수량은 필수 입력입니다.")
  @Min(value = 0, message = "상품 수량은 0 이상이어야 합니다.")
  private Integer productCnt;

  //상품좋아요(관심,찜)
  private Integer productLike;

  //상품조회수
  private Integer productViewcnt;

  //카테고리유형 (전체, 생활용품, 욕실용품, 주방용품, 회원전용) - 데이터베이스에 Enum 값을 문자열로 저장하도록 지정
  private CategoryTypeRole categoryTypeRole;

  //상품판매상태 (SELL, STOP, LACK) - 데이터베이스에 Enum 값을 문자열로 저장하도록 지정
  private SellStateRole sellStateRole;

  //이미지 관리 DTO
  private List<ImageDTO> imageDTOs;

  //이미지파일 처리
  private List<MultipartFile> images;

  private LocalDateTime reDate;

  private LocalDateTime modDate;

}


//숫자의 경우 Integer로 작성해야 빈칸에 "0"이 안나옴












/*

  private Integer productId;                         // 상품고유번호

  @Enumerated(EnumType.STRING)
  private CategoryTypeRole CategoryTypeRole;              // 카테고리유형

  private String categoryType;

  public String getCategoryType() {
    return categoryType;
  }

  @NotEmpty(message = "상품이름은 필수 입력입니다.")
  private String productName;                       // 상품이름

  @NotEmpty(message = "상품설명은 필수 입력입니다.")
  private String productContent;                    // 상품설명

  private String productDetail;                     // 상품상세정보

  @NotNull(message = "가격은 필수 입력입니다.")
  private Integer productCost;                      // 소비자가(정가)

  @NotNull(message = "가격은 필수 입력입니다.")
  private Integer productPrice;                     // 판매가

  private Integer productDis;                       // 할인율

  @NotNull(message = "재고수량은 필수 입력입니다.")
  private Integer productCnt;                       // 상품재고수량

  private Integer productLike;                      // 상품좋아요(관심,찜)

  private Integer productViewcnt;                   // 상품조회수

  @Enumerated(EnumType.STRING)
  private SellStateRole sellStateRole;              // 상품판매상태

  private LocalDate reDate;                         // 등록날짜

  private LocalDateTime modDate;                    // 수정날짜

  private List<Integer> productImages;

  private List<MultipartFile> productImageFiles;


  public List<Integer> getProductImages() {
    return productImages;
  }

  public void setProductImages(List<Integer> productImages) {
    this.productImages = productImages;
  }

  public List<MultipartFile> getProductImageFiles() {
    return productImageFiles;
  }

  public void setProductImageFiles(List<MultipartFile> productImageFiles) {
    this.productImageFiles = productImageFiles;
  }

 */