package com.example.clean.Service;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.ImageDTO;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Repository.ProductRepository;
import com.example.clean.Util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AiService {

    //파일이 저장될 경로
    @Value("${imgUploadLocation}")
    private String imgUploadLocation;

    //파일 저장을 위한 클래스
    private final S3Uploader s3Uploader;

    private final FileService fileService;
    private final ImageService imageService;

    private final ModelMapper modelMapper = new ModelMapper();
    private final ProductRepository productRepository;


    //카테고리 별로 상품 목록 조회 (검색 포함, 페이징 처리) -> 회원용
    public Page<ProductDTO> memberSaleList (Pageable page) throws Exception {

        int currentPage = page.getPageNumber() -1;
        int itemsPerPage = 8; // 한 페이지에 표시되는 총 상품 수

        //제품번호 기준 역순으로 정렬
        Pageable pageable = PageRequest.of(currentPage, itemsPerPage, Sort.by(Sort.Direction.DESC, "productId"));

        //진열조건 - MEMBERSALE 제품만
        Page<ProductEntity> productEntityPage = productRepository.findByCategoryType(CategoryTypeRole.MEMBERSALE, pageable);

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
        List<ImageDTO> imageDTOs = new ArrayList<>();

        for (ImageEntity imageEntity : imagesEntities) {
            ImageDTO imageDTO =  modelMapper.map(imageEntity, ImageDTO.class);

            String imageUrl = s3Uploader.getImageUrl("static", imageEntity.getImageFile()); // S3에서 이미지 가져오기
            imageDTO.setImageFile(imageUrl); // ImageDTO에 S3 이미지 URL 설정

            imageDTOs.add(imageDTO);
        }
        return imageDTOs;
    }


    //상품개별조회
    public ProductDTO findOne(Integer productId) throws Exception {
        modelMapper.typeMap(ProductEntity.class, ProductDTO.class)
                .addMappings(mapper -> mapper.skip(ProductDTO::setImages));

        Optional<ProductEntity> data = productRepository.findById(productId);
        ProductEntity entity = data.get();

        //변환
        ProductDTO result = data.map(mapper->modelMapper.map(mapper, ProductDTO.class)).orElse(null);
        List<ImageDTO> imageDTOS = entity.getProductImages().stream()
                .map(imageEntity -> modelMapper.map(imageEntity, ImageDTO.class))
                .collect(Collectors.toList());
        result.setImageDTOs(imageDTOS);

        return result;
    }

}


