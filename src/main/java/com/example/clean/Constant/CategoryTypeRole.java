package com.example.clean.Constant;

public enum CategoryTypeRole {

  ALL("전체"),
  LIVING("생활용품"),
  BATHROOM("욕실용품"),
  KITCHEN("주방용품"),
  MEMBERSALE("회원전용");

  private final String description;

  CategoryTypeRole(String description) {
    this.description = description;
  }
  public String getDescription() {
    return description;
  }

}






/*

  숫자 추가 하는 경우

  ALL(0, "전체"),
  LIVING(1, "생활용품"),
  BATHROOM(2, "욕실용품"),
  KITCHEN(3, "주방용품"),
  MEMBERSALE(4, "회원전용");

  private final int code;
  private final String description;

  CategoryTypeRole(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

 */