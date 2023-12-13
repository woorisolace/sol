package com.example.clean.Constant;

public enum AdminNoticeRole {
    DELIVERY("배송공지"),
    PRODUCT("상품공지"),
    EVENT("이벤트"),
    ETC("기타"),
    TIP("청소꿀팁");

    private final String description;

    AdminNoticeRole(String description){ this.description = description;}

    public String getDescription(){
        return description;
    }
}