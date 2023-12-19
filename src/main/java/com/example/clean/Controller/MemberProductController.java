package com.example.clean.Controller;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Entity.ProductEntity;
import com.example.clean.Repository.ProductRepository;
import com.example.clean.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MemberProductController {

  //S3 이미지 정보
  @Value("${cloud.aws.s3.bucket}")
  public String bucket;

  @Value("${cloud.aws.region.static}")
  public String region;

  @Value("${imgUploadLocation}")
  public String folder;

  private final ProductRepository productRepository;
  private final ProductService productService;


  // 제품 상세페이지
  @GetMapping("/product/{productId}")
  public String memProDetail(@PathVariable Integer productId, Model model, HttpServletRequest request) throws Exception {

    // 현재 페이지의 URL을 세션에 저장
    HttpSession session = request.getSession();
    session.setAttribute("originalRequestUrl", "/product/" + productId);


    ProductDTO productDTO = productService.findOne(productId);
    List<ProductEntity> relatedProducts = productRepository.findRelatedProductsByCategoryTypeRole(productId, productDTO.getCategoryTypeRole());

    model.addAttribute("relatedProducts",relatedProducts);
    model.addAttribute("productDTO", productDTO);
    model.addAttribute("sellState", SellStateRole.values());
    model.addAttribute("categoryType", CategoryTypeRole.values());
    model.addAttribute("imageDTOs", productDTO.getImageDTOs());

    //S3 이미지 정보 전달
    model.addAttribute("bucket", bucket);
    model.addAttribute("region", region);
    model.addAttribute("folder", folder);

    return "/product/productdetail";
  }


  //회원 제품 목록 - (카테고리별로 진열)
  @GetMapping("/productlist/{categoryTypeRole}")
  public String productlistForm(@PathVariable(value = "categoryTypeRole") String categoryTypeRole,
                                @PageableDefault(page = 1) Pageable pageable,
                                HttpServletRequest request,
                                Model model) throws Exception {

    // 현재 페이지의 URL을 세션에 저장
    HttpSession session = request.getSession();
    session.setAttribute("originalRequestUrl", "/productlist/" + categoryTypeRole);

    Page<ProductDTO> productDTOS = productService.categoryList(categoryTypeRole, pageable);

    int blockLimit = 5;
    int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
    int endPage = Math.min(startPage + blockLimit - 1, productDTOS.getTotalPages());

    int prevPage = productDTOS.getNumber();
    int currentPage = productDTOS.getNumber() + 1;
    int nextPage = productDTOS.getNumber() + 2;
    int lastPage = productDTOS.getTotalPages();

    //전체 상품수 조회
    productDTOS.getTotalElements();

    model.addAttribute("productDTOS", productDTOS);

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    model.addAttribute("totalElements", productDTOS.getTotalElements());

    //S3 이미지 정보 전달
    model.addAttribute("bucket", bucket);
    model.addAttribute("region", region);
    model.addAttribute("folder", folder);

    //해당 변수의 값을 사용해 동적으로 뷰 이름을 생성
    return "product/" + categoryTypeRole;
  }

}
