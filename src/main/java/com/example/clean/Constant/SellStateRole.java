package com.example.clean.Constant;

//상품판매상태 (판매중, 판매중지, 재고없음)
public enum SellStateRole {

  SELL("판매중"),
  STOP("판매중지"),
  LACK("재고없음");

  private final String description;

  SellStateRole(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}

//Enum : Java에서 상수를 표현할 때 사용, 상수 간의 관계를 명확하게 표현, 코드의 가독성 향상
//description : 멤버 변수. 각 상태의 설명
//getDescription 메서드 : 열거 상수에 대응하는 한글 설명