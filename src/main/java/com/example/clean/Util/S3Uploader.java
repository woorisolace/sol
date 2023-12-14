package com.example.clean.Util;


import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.clean.Entity.ImageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//S3 파일 업로드 클래스

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;


    //플라스크 AI결과 이미지 업로드
    public String AIResultS3(File uploadFile, String fileName) throws IOException {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //일반 파일 S3에 업로드
    //dirName : 업로드된 파일이 S3에서 어떤 디렉토리에 위치할지를 지정
    public String upload(MultipartFile multipartFile, String dirName) throws IOException{
        File uploadFile = convert(multipartFile).orElseThrow(() -> new IllegalArgumentException("파일 전환 실패"));
        return upload(uploadFile, dirName);
    }


    //public void deleteFile(List<ImageEntity> deleteFile, String dirName) throws IOException {
    //S3 파일삭제
    //List<ImageEntity> deleteFile -> 단일 파일을 삭제하도록 수정해야함
    public void deleteFile(String deleteFile, String dirName) throws IOException {

        String fileName = dirName+"/"+deleteFile;
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch(SdkClientException e) {
            throw new IOException("Error deleting file from S3", e);
        }
    }


    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName) {
        String newFileName = UUID.randomUUID() + uploadFile.getName();
        //String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
        String fileName = dirName + "/" + newFileName;          // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName);    // s3로 업로드
        removeNewFile(uploadFile);

        return newFileName;
        //return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


    // 이미지 URL 가져오기
    public String getImageUrl(String dirName, String fileName) {
        //String key = fileName;
        String key = dirName + "/" + fileName;  // dirName 추가
        return amazonS3Client.getUrl(bucket, key).toString();
    }


    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }


    private Optional<File> convert(MultipartFile multipartFile) throws IOException{
        //System.out.println(System.getProperty("user.dir"));
        File convertFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();

    }

}