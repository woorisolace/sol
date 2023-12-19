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
