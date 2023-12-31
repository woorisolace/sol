package com.example.clean.Service;
import com.example.clean.DTO.ImageDTO;
import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

  //파일업로드
  private final FileService fileService;
  private final ModelMapper modelMapper = new ModelMapper();

  private final ImageRepository imageRepository;



  // 삽입
  public void uploadImage(ImageDTO imageDTO, ProductEntity entity, MultipartFile imageFile) throws Exception {
    String originalFileName = imageFile.getOriginalFilename(); //기존파일명
    String newFileName=""; //UUID로 생성된 새로운 파일명

    if(originalFileName != null && originalFileName.length() >0) { //파일존재시 업로드 진행
      newFileName = fileService.uploadFile(originalFileName, imageFile.getBytes());
    }

    imageDTO.setImageFile(newFileName); //새로운 이름으로 변경
    ImageEntity imageEntity = modelMapper.map(imageDTO, ImageEntity.class); //데이터 변환
    imageEntity.setProductEntity(entity);

    try {
      imageRepository.save(imageEntity);
    } catch(Exception e) {
      //
    }

  }


  // 수정 (글만 또는 이미지만 또는 글+이미지 수정)
  public void updateImage(ImageDTO imageDTO, ProductEntity productEntity, MultipartFile imageFile) throws Exception {

    String originalFileName = imageFile.getOriginalFilename();  // 기존 파일명
    String newFileName = "";    // UUID로 생성된 새로운 파일명

    // 해당 이미지 존재 여부 확인
    ImageEntity imageEntity = imageRepository
        .findById(imageDTO.getImageId())
        .orElseThrow();

    String deleteFileName = imageEntity.getImageFile();    // 이전 파일명

    if (originalFileName != null && originalFileName.length() > 0) {    // 파일 존재시 업로드 진행
      if (deleteFileName.length() != 0) {      // 이전 파일 존재시 삭제
        fileService.deleteFile(deleteFileName);
      }
      newFileName = fileService.uploadFile(originalFileName, imageFile.getBytes());

      imageDTO.setImageFile(newFileName);
      imageDTO.setImageId(imageEntity.getImageId());    // 새로운 파일명을 재등록

      ImageEntity update = modelMapper.map(imageDTO, ImageEntity.class);    // 데이터 변환

      // 이미지와 제품 간의 관계 설정
      update.setProductEntity(productEntity);

      update.setImageFile(newFileName);
      update.setImageId(imageEntity.getImageId());   // 외래키 받아올 때 엔티티로 선언했기 때문에 id가 아닌 entity로 가져와야 함

      imageRepository.save(update);
    }
  }

  //삭제
  public void deleteImage (Integer imageId) throws Exception {
    imageRepository.deleteById(imageId);

  }

}



/*
  //삽입
  // 이미지 업로드하고 이미지 번호 반환
  public Integer uploadImage(MultipartFile imgFile) throws Exception {
    ImageEntity imageEntity = new ImageEntity();
    imageRepository.save(imageEntity);

    // 이미지 저장
    String newFileName = fileService.uploadFile(
        imgLocation,
        imageEntity.getImageFile(),
        imgFile.getBytes()
    );

    // 파일 이름 저장
    imageEntity.setImageFile(newFileName);
    imageRepository.save(imageEntity);

    return imageEntity.getImageId(); // 이미지 번호 반환
  }

*/

  
 /* 
  // 전체 조회
  public List<ImageDTO> findAll() throws Exception {
    List<ImageEntity> imageEntityList = imageRepository.findAll();
    List<ImageDTO> imageDTOS = imageEntityList.stream()
        .map(imageEntity -> modelMapper.map(imageEntity, ImageDTO.class))
        .collect(Collectors.toList());
    return imageDTOS;
  }


  // 개별 조회
  public ImageDTO findOne(Integer imageId) throws Exception {
    Optional<ImageEntity> imageEntityOptional = imageRepository.findById(imageId);
    return imageEntityOptional.map(imageEntity -> modelMapper.map(imageEntity, ImageDTO.class))
        .orElse(null);
  }



  //게시글 수정 (글만 또는 이미지만 또는 글+이미지 수정)
  public void update(ImageDTO imageDTO, MultipartFile imgFile) throws Exception {

    String originalFileName = imgFile.getOriginalFilename();    //저장 할 파일명
    String newFileName = ""; //새로 만든 파일명

    //해당 이미지 존재 여부 확인
    ImageEntity imageEntity = imageRepository.findById(imageDTO.getImageId()).orElseThrow();

    String deleteFileName = imageEntity.getImageFile();  //이전 파일명

    if(originalFileName != null && originalFileName.length()>0) {    //파일이 존재하면 djqfhem
      if(deleteFileName.length() != 0) {        //바꿀 파일이 있으면 삭제
        fileService.deleteFile(imgLocation, deleteFileName);
      }
      newFileName = fileService.uploadFile(
          imgLocation,
          originalFileName
      );
    }
    imageDTO.setImageFile(newFileName);
    imageDTO.setImageId(imageEntity.getImageId());      //새로운 파일명을 재등록
    
    ImageEntity data = modelMapper.map(imageDTO, ImageEntity.class);    //데이터변환
    imageRepository.save(data);  //저장
  }


  //삭제
  public void delete(Integer imageId) throws Exception {
    imageRepository.deleteById(imageId);
  }
 */