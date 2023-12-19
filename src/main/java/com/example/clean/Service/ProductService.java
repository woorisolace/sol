package com.example.clean.Service;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.ImageDTO;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.OrderEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Repository.ProductRepository;
import com.example.clean.Util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

// 상품의 이미지를 S3로부터 가져와서 ProductDTO에 설정

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  //파일이 저장될 경로
  @Value("${imgUploadLocation}")
  private String imgUploadLocation;

  //파일 저장을 위한 클래스
  private final S3Uploader s3Uploader;

  private final FileService fileService;
  private final ImageService imageService;
  private final ModelMapper modelMapper = new ModelMapper();

  private final ProductRepository productRepository;


  //제품등록
  public void insertProduct(ProductDTO productDTO, List<MultipartFile> imageFiles) throws Exception {

    List<ImageDTO> dataDTO = productDTO.getImageDTOs();   //이미지테이블정보 분리
    List<MultipartFile> images = productDTO.getImages();  //이미지파일 분리

    //맵핑전 불필요한 필드 제외
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.typeMap(ProductDTO.class, ProductEntity.class)
        .addMappings(mapper -> mapper.skip(ProductEntity::setProductImages));

    //신규 상품 등록
    ProductEntity data = modelMapper.map(productDTO, ProductEntity.class);
    ProductEntity dataEntity = productRepository.save(data);

    int index = 0;
    for (MultipartFile file : images) {
      ImageDTO jobDTO = dataDTO.get(index);

      try {
        imageService.uploadImage(jobDTO, dataEntity, file);       //이미지 등록 및 이미지테이블에 정보 등록
      } catch (IOException e) {
        //
      }
      index++;
    }
  }



  //카테고리 별로 상품 목록 조회 (검색 포함, 페이징 처리) -> 회원용
  public Page<ProductDTO> categoryList (String categoryTypeRole, Pageable page) throws Exception {

    int currentPage = page.getPageNumber() -1;
    int itemsPerPage = 8; // 한 페이지에 표시되는 총 상품 수


    //제품번호 기준 역순으로 정렬
    Pageable pageable = PageRequest.of(currentPage, itemsPerPage, Sort.by(Sort.Direction.DESC, "productId"));

    //진열조건 - 카테고리 이름에 맞춰
    Page<ProductEntity> productEntityPage;

    // "ALL"이면 모든 카테고리의 상품을 조회하도록 처리
    if ("ALL".equalsIgnoreCase(categoryTypeRole)) {
      productEntityPage = productRepository.findBySellStateRole(SellStateRole.SELL, pageable);
    } else {
      // 다른 카테고리의 경우 해당 카테고리의 상품을 조회
      productEntityPage = productRepository.findProductEntityByCategoryTypeRoleAndSellState(
          CategoryTypeRole.valueOf(categoryTypeRole), SellStateRole.SELL, pageable);
    }

    // "ALL"이면 모든 카테고리의 상품을 조회하도록 처리
    if ("ALL".equalsIgnoreCase(categoryTypeRole)) {
      // MEMBERSALE인 경우 제외하고 조회
      productEntityPage = productRepository.findByCategoryTypeRoleNotAndSellStateRole(
          CategoryTypeRole.MEMBERSALE, SellStateRole.SELL, pageable);
    } else {
      // 다른 카테고리의 경우 해당 카테고리의 상품을 조회
      productEntityPage = productRepository.findProductEntityByCategoryTypeRoleAndSellState(
          CategoryTypeRole.valueOf(categoryTypeRole), SellStateRole.SELL, pageable);
    }

    // 상품정보 및 이미지들을 Entity에서 DTO로 복수전달
    List<ProductDTO> productDTOList = new ArrayList<>();

    for (ProductEntity entity : productEntityPage) {
      ProductDTO productDTO = ProductDTO.builder()
          .productId(entity.getProductId())
          .productName(entity.getProductName())
          .productContent(entity.getProductContent())
          .productDetail(entity.getProductDetail())
          .productCost(entity.getProductCost())
          .productPrice(entity.getProductPrice())
          .productDis(entity.getProductDis())
          .productOpt(entity.getProductOpt())
          .productCnt(entity.getProductCnt())
          .productLike(entity.getProductLike())
          .productViewcnt(entity.getProductViewcnt())
          .categoryTypeRole(entity.getCategoryTypeRole())
          .sellStateRole(entity.getSellStateRole())
          .reDate(entity.getReDate())
          .modDate(entity.getModDate())
          .imageDTOs(entity.getProductImages() != null ? mapImagesToDTOs(entity.getProductImages()) : Collections.emptyList())
          .build();

      productDTOList.add(productDTO);
    }
    return new PageImpl<>(productDTOList, pageable, productEntityPage.getTotalElements());
  }


  //각 상품에 이미지테이블 전달
  private List<ImageDTO> mapImagesToDTOs(List<ImageEntity> imagesEntities) throws Exception {
    if (imagesEntities != null && !imagesEntities.isEmpty()) {
      List<ImageDTO> imageDTOs = new ArrayList<>();     //변환된 ImageDTO 객체를 담을 imageDTOs 리스트를 초기화

      for (ImageEntity imageEntity : imagesEntities) {
        ImageDTO imageDTO = modelMapper.map(imageEntity, ImageDTO.class);

        // S3에서 이미지 URL 가져오기
        String imageUrl = s3Uploader.getImageUrl("static", imageEntity.getImageFile());

        log.info("상품 서비스 Image URL: {}", imageUrl);

        imageDTO.setImageFile(imageEntity.getImageFile());    //가져올 이미지 파일 이름이나 경로를 저장 또는 imageUrl

        imageDTOs.add(imageDTO);
      }
      return imageDTOs;
    }
    // 이미지가 없는 경우 빈 목록 반환
    return Collections.emptyList();
  }


  //상품개별조회
  public ProductDTO findOne(Integer productId) throws Exception {     //개별 조회(상세)
    modelMapper.typeMap(ProductEntity.class, ProductDTO.class)
        .addMappings(mapper -> mapper.skip(ProductDTO::setImages));

    Optional<ProductEntity> data = productRepository.findById(productId); // in(int)->out(Optional<entity>)
    System.out.println("Received productId in findOne 출력됨: " + productId);
    ProductEntity entity = data.get();

    //변환
    ProductDTO result = data.map(mapper -> modelMapper.map(mapper, ProductDTO.class)).orElse(null);
    List<ImageDTO> imageDTOS = entity.getProductImages().stream()
        .map(imageEntity -> modelMapper.map(imageEntity, ImageDTO.class))
        .collect(Collectors.toList());
    result.setImageDTOs(imageDTOS);

    return result;
  }


  //수정 (글만 또는 이미지만 또는 글+이미지 수정)
  public void updateProductAndImages(ProductDTO productDTO, List<MultipartFile> imageFiles) throws Exception {

    List<ImageDTO> dataDTO = productDTO.getImageDTOs();  //이미지테이블정보 분리

    //맵핑전 불필요한 필드 제외
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.typeMap(ProductDTO.class, ProductEntity.class)
        .addMappings(mapper -> mapper.skip(ProductEntity::setProductImages));

    //상품 수정 등록
    //ProductEntity data = modelMapper.map(productDTO, ProductEntity.class);
    ProductEntity productEntity = productRepository.save(modelMapper.map(productDTO, ProductEntity.class));

    int index = 0;
    for (MultipartFile file : imageFiles) {
      ImageDTO imgDTO = dataDTO.get(index);
      imgDTO.setProductid(productEntity.getProductId());

      imageService.updateImage(imgDTO, productEntity, file);
      index++;
    }
  }


  //삭제
  public void delete (Integer productId) throws Exception {

    ProductEntity productEntity = productRepository.findByProductId(productId); //상품조회

    if (productEntity == null) {
      return;
    }

    // 연결된 주문과의 관계를 끊음
    for (OrderEntity orderEntity : productEntity.getOrderEntityList()) {
      orderEntity.setProductEntity(null);
    }

    //연결된 이미지 삭제
    List<ImageEntity> imageEntitys = productEntity.getProductImages();
    for (ImageEntity data : imageEntitys) {
      fileService.deleteFile(data.getImageFile());
    }

    //S3 + 상품 삭제
    productRepository.deleteById(productId);

  }


  //전체 상품 목록 조회 (검색 포함, 페이징 처리) -> 관리자
  public Page<ProductDTO> adminFindAll(String type, String keyword, String sellState, String categoryType, Pageable page) throws Exception {

    int currentPage = page.getPageNumber() - 1;
    int pageLimit = 10;    //한페이지에 총 8개의 상품 진열

    //제품번호 기준 역순으로 정렬
    Pageable pageable = PageRequest.of(currentPage, pageLimit, Sort.by(Sort.Direction.DESC, "productId"));

    Page<ProductEntity> productEntityPage;

    if ("n".equals(type) && keyword != null && !keyword.isEmpty()) {
      // 상품명 조회
      productEntityPage = productRepository.findProductName(keyword, pageable);
      System.out.println("상품명 조회");
    } else if ("nc".equals(type) && keyword != null && !keyword.isEmpty()) {
      // 상품명+내용 조회
      productEntityPage = productRepository.findProNameContent(keyword, pageable);
      System.out.println("상품명+내용 조회");
    } else if ("ca".equals(type) && categoryType != null && !categoryType.isEmpty()) {
      // 카테고리 조회
      productEntityPage = productRepository.findByCategoryType(CategoryTypeRole.valueOf(categoryType), pageable);
      System.out.println("카테고리 조회");
    } else if ("s".equals(type) && sellState != null && !sellState.isEmpty()) {
      // 판매상태 조회
      productEntityPage = productRepository.findAllByStateRole(SellStateRole.valueOf(sellState), pageable);
      System.out.println("판매형태 조회");
    } else {
      // 전체 조회
      productEntityPage = productRepository.findAll(pageable);
      System.out.println("전체 조회");
    }


    // 상품정보 및 이미지들을 Entity에서 DTO로 복수전달
    List<ProductDTO> productDTOList = new ArrayList<>();
    for (ProductEntity entity : productEntityPage) {
      ProductDTO productDTO = ProductDTO.builder()
          .productId(entity.getProductId())
          .productName(entity.getProductName())
          .productContent(entity.getProductContent())
          .productDetail(entity.getProductDetail())
          .productCost(entity.getProductCost())
          .productPrice(entity.getProductPrice())
          .productDis(entity.getProductDis())
          .productOpt(entity.getProductOpt())
          .productCnt(entity.getProductCnt())
          .productLike(entity.getProductLike())
          .productViewcnt(entity.getProductViewcnt())
          .categoryTypeRole(entity.getCategoryTypeRole())
          .sellStateRole(entity.getSellStateRole())
          .reDate(entity.getReDate())
          .modDate(entity.getModDate())
          .imageDTOs(mapImagesToDTOs(entity.getProductImages()))
          .build();

      productDTOList.add(productDTO);
    }
    return new PageImpl<>(productDTOList, pageable, productEntityPage.getTotalElements());
  }


}







