package com.example.clean.Controller;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.ProductDTO;
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



@Controller
@RequiredArgsConstructor
public class MemberProductController {

  private final ProductService productService;

  @Value("${uploadPath}")
  private String uploadPath;

  @Value("${imgLocation}")
  private String imgLocation;

  // 제품 상세페이지
  //@PathVariable : URL 경로에서 변수 값을 추출하는 데 사용
  @GetMapping("/product/{productId}")
  public String memProDetail(@PathVariable Integer productId, Model model) throws Exception {

    ProductDTO productDTO = productService.findOne(productId);

    model.addAttribute("productDTO", productDTO);
    model.addAttribute("sellState", SellStateRole.values());
    model.addAttribute("categoryType", CategoryTypeRole.values());
    model.addAttribute("imageDTOs", productDTO.getImageDTOs());

    return "/product/productdetail";
  }



  //회원 제품 목록 - (카테고리별로 진열)
  @GetMapping("/productlist/{categoryTypeRole}")
  public String productlistForm(@PathVariable(value = "categoryTypeRole") String categoryTypeRole,
                                @PageableDefault(page = 1) Pageable pageable,
                                Model model) throws Exception {


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

    //해당 변수의 값을 사용해 동적으로 뷰 이름을 생성
    return "product/" + categoryTypeRole;
  }

}
