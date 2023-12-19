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
