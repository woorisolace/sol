package com.example.clean.Service;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.ImageDTO;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Entity.ImageEntity;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AiService {

    private final FileService fileService;
    private final ImageService imageService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final ProductRepository productRepository;


    // 전체 상품 목록 조회 (검색 포함, 페이징 처리)
    public Page<ProductDTO> findALl (String sellState, String categoryType, Pageable page) throws Exception {

        int currentPage = page.getPageNumber() - 1;
        int pageLimit = page.getPageSize();

        Pageable pageable = PageRequest.of(currentPage, pageLimit, Sort.by(Sort.Direction.DESC, "productId")); // 등록일 기준으로 정렬


        Page<ProductEntity> productEntityPage;

        if ("ca".equals(categoryType)) {
            productEntityPage = productRepository.findByCategoryType(CategoryTypeRole.valueOf(categoryType), pageable);
        } else if ("s".equals(sellState)) {
            productEntityPage = productRepository.findBySellStateRole(SellStateRole.valueOf(sellState), pageable);
        } else {
            productEntityPage = productRepository.findAll(pageable);
        }

        // 상품정보 및 이미지들을 Entity에서 DTO로 복수전달
        List<ProductDTO> productDTOList = new ArrayList<>();

        for(ProductEntity entity:productEntityPage) {
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
                    .imageDTOs(mapImagesToDTOs(entity.getProductImages()))
                    .build();

            productDTOList.add(productDTO);
        }
        return new PageImpl<>(productDTOList, pageable, productEntityPage.getTotalElements());
    }

    //각 상품에 이미지테이블 전달
    private List<ImageDTO> mapImagesToDTOs(List<ImageEntity> imagesEntities) {
        List<ImageDTO> imageDTOs = new ArrayList<>();

        for (ImageEntity imageEntity : imagesEntities) {
            ImageDTO imageDTO =  modelMapper.map(imageEntity, ImageDTO.class); //
            imageDTOs.add(imageDTO);
        }
        return imageDTOs;
    }

    //상품개별조회
    public ProductDTO findOne(Integer productId) throws Exception {     //개별 조회(상세)
        modelMapper.typeMap(ProductEntity.class, ProductDTO.class)
                .addMappings(mapper -> mapper.skip(ProductDTO::setImages));

        Optional<ProductEntity> data = productRepository.findById(productId); // in(int)->out(Optional<entity>)
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




/*
//전체 조회
  public Page<ProductDTO> findAll(String categoryType, String sellState, Pageable page) throws Exception {

    int currnetPage = page.getPageNumber()-1;
    int pageLimit = 1;     // 한 페이지당 읽을 데이터의 개수 (행 수)

    Pageable pageable = PageRequest.of(currnetPage, pageLimit, Sort.by(Sort.Direction.DESC, "productId"));

    //페이지정보로 조회
    Page<ProductEntity> productEntityPage = productRepository.findAll(pageable);

    // 상품정보 및 이미지들을 Entity에서 DTO로 복수전달
    Page<ProductDTO> productDTOList = productEntityPage.map(entity -> ProductDTO.builder()
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
        .imageDTOs(mapImagesToDTOs(entity.getProductImages()))
        .build()
    );
    return productDTOList;
  }
 */