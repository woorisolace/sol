package com.example.clean.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//자원과 외부폴더를 연결 설정
//사용자 클래스를 Bean에 등록
//WebMvcConfigurer인터페이스 상속받아서 내용 작성

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  //application에서 사용자 변수 읽어오기
  @Value(("${uploadPath}"))
  String uploadPath;


  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/images/**")
        .addResourceLocations(uploadPath);    //자원의 위치
  }

}
