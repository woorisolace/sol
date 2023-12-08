package com.example.clean.DTO;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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

  //회원 구매수량
  private Integer productNum;

  //합계 = 판매가 * 옵션 선택 수량
  private Integer producTotal;

  //할인율 = (정가 -할인가)/정가
  private Integer productDis;

  //상품옵션
  private Integer productOpt;

  //재고수
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


