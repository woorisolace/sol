package com.example.clean.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;

import java.util.UUID;

//이미지 엔티티를 생성하고 파일을 업로드
//각 이미지 타입(대표, 서브, 상세)에 대한 리스트로 받아서 처리
//각 이미지에 대해 파일을 업로드하고 이미지 엔티티를 생성

@Service
public class FileService {

  //저장할 경로, 파일명, 데이터값
  @Value("${imgLocation}")
  private String imgLocation;

  //저장할 경로,파일명,데이터 값
  public String uploadFile(String originalFileName,  byte[] filedata) throws Exception {

    UUID uuid = UUID.randomUUID();    //문자열 생성
    String extendsion = originalFileName.substring(originalFileName.lastIndexOf("."));    //문자열 분리
    String saveFileName = uuid.toString()+extendsion;    //새로운 파일명
    String uploadFullurl = imgLocation+saveFileName;      //저장위치 및 파일명

    //하드디스크에 파일 저장
    FileOutputStream fos = new FileOutputStream(uploadFullurl);
    fos.write(filedata);
    fos.close();

    return saveFileName;    //데이터베이스에 저장할 파일명
  }

  // 파일 삭제 (상품을 수정시 기존 파일을 삭제하고 새로운 파일을 저장)
  public void deleteFile(String fileName) throws Exception {
    String deleteFileName = imgLocation + fileName;

    File deleteFile = new File(deleteFileName);
    if (deleteFile.exists()) {
      deleteFile.delete();
    }
  }
}
