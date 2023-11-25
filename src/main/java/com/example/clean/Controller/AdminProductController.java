package com.example.clean.Controller;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Service.ImageService;
import com.example.clean.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminProductController {

  private final ProductService productService;
  private final ImageService imageService;

  //상품목록
  @GetMapping("/admin_productlist")
  public String listForm(@RequestParam(value = "type", defaultValue = "") String type,
                         @RequestParam(value = "keyword", defaultValue = "") String keyword,
                         @RequestParam(value = "sellsState", defaultValue = "") String sellState,        //판매상태 한글로 받아오기
                         @RequestParam(value = "categoryType", defaultValue = "") String categoryType,   // 카테고리 추가
                         @PageableDefault(page=1) Pageable pageable,
                         Model model) throws Exception {


    // SellStateRole 열거형의 한글 설명을 리스트로 가져오기
    List<String> sellStateOptions = Arrays.stream(SellStateRole.values())
        .map(SellStateRole::getDescription)
        .collect(Collectors.toList());
    model.addAttribute("sellStateOptions", sellStateOptions); // 판매상태 옵션을 전달

    // CategoryTypeRole 열거형의 한글 설명을 리스트로 가져오기
    List<String> categoryOptions = Arrays.stream(CategoryTypeRole.values())
        .map(CategoryTypeRole::getDescription)
        .collect(Collectors.toList());
    model.addAttribute("categoryOptions", categoryOptions);

    Page<ProductDTO> productDTOS = productService.findALl(type, keyword, sellState, categoryType, pageable);

    productDTOS.getTotalElements();   //전체 게시글 조회

    int blockLimit = 5;
    int startPage = (((int)(Math.ceil((double)pageable.getPageNumber()/blockLimit)))-1) * blockLimit+1;
    int endPage = Math.min(startPage+blockLimit-1, productDTOS.getTotalPages());

    int prevPage = productDTOS.getNumber();
    int currentPage = productDTOS.getNumber()+1;
    int nextPage = productDTOS.getNumber()+2;
    int lastPage = productDTOS.getTotalPages();

    model.addAttribute("productDTOS", productDTOS);

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    model.addAttribute("type", type);
    model.addAttribute("keyword", keyword);

    return "/admin/product_list";
  }


  //제품등록
  @GetMapping("/admin_product_insert")
  public String productInsertForm(Model model) throws Exception {

    ProductDTO productDTO = new ProductDTO();
    //List<SellStateRole> sellStateRoleList = Arrays.asList(SellStateRole.values());
    //List<CategoryTypeRole> categoryTypeRoleList = Arrays.asList(CategoryTypeRole.values());

    model.addAttribute("productDTO", productDTO);
    model.addAttribute("categoryType", CategoryTypeRole.values());
    model.addAttribute("sellsState", SellStateRole.values());
    return "/admin/product_insert";
  }

  // 상품 등록 처리 (상품 정보 및 이미지 업로드)
  @PostMapping("/admin_product_insert")
  public String productInsertProc(@Valid ProductDTO productDTO, BindingResult bindingResult,
                                  @RequestParam("images") List<MultipartFile> imageFiles, Model model) {

    if (bindingResult.hasErrors()) {
      return "/admin/product_insert";
    }
    try {
      productService.insertProduct(productDTO, imageFiles);
    } catch (Exception e) {
      // Handle the exception, you might want to log it or provide some user-friendly message
      model.addAttribute("error", "상품등록 실패하였습니다. 다시 등록해주세요.");
      return "/admin/product_insert";
    }

    return "redirect:/admin_productlist";
  }


  //제품 상세페이지
  @GetMapping("/admin_product_indetail")
  public String productDetailForm(Integer productId, Model model) throws Exception {

    ProductDTO productDTO = productService.findOne(productId);
    model.addAttribute("productDTO", productDTO);

    return "/admin/product_detail";

  }

  //제품 상세페이지 수정폼  (글만 또는 이미지만 또는 글+이미지 수정)
  @GetMapping("/admin_product_update")
  public String productUpdateForm(Integer productId, Model model) throws Exception {

    ProductDTO productDTO = productService.findOne(productId);

    //model.addAttribute("sellState", productDTO.getSellStateRole());
    //model.addAttribute("categoryType", productDTO.getCategoryTypeRole());
    //열거형 목록
    model.addAttribute("sellState", SellStateRole.values());
    model.addAttribute("categoryType", CategoryTypeRole.values());

    model.addAttribute("productDTO", productDTO); //데이터
    model.addAttribute("imageDTOs", productDTO.getImageDTOs()); //이미지

    return "admin/product_update";
  }
  // 제품 상세페이지 수정처리
  @PostMapping("/admin_product_update")
  public String productUpdateProc(@Valid ProductDTO productDTO, BindingResult bindingResult,
                                  @RequestParam("images") List<MultipartFile> imageFiles,
                                  Model model) throws Exception {

    if (bindingResult.hasErrors()) {
      model.addAttribute("sellState", SellStateRole.values());
      model.addAttribute("categoryType", CategoryTypeRole.values());

      return "admin/product_update";
    }

    try {
      // 상품 정보 및 이미지 업데이트
      productService.updateProductAndImages(productDTO, imageFiles);

      return "redirect:/admin_productlist";
    } catch (Exception e) {

      e.printStackTrace();

      model.addAttribute("error", "상품 수정에 실패하였습니다.");

      model.addAttribute("sellState", SellStateRole.values());
      model.addAttribute("categoryType", CategoryTypeRole.values());
      return "admin/product_update";   //"/admin_product_update";
    }
  }


  //제품등록 상세페이지 삭제
  @GetMapping("/admin_product_delete")
  public String productDeleteForm(Integer productId) throws Exception {
    productService.delete(productId);
    return "redirect:/admin_productlist";
  }


}












/*

// 상품 등록 처리 (상품 정보 및 이미지 업로드)
  @PostMapping("/admin_product_insert")
  public String productInsert(@Valid ProductDTO productDTO,
                              BindingResult bindingResult,
                              @RequestParam("imgFile") MultipartFile imagefile,
                              Model model) {

    try {
      if (bindingResult.hasErrors()) {
        return "/admin/product_insert";
      }

      // 이미지 등록 후 이미지 번호 획득
      Integer imageId = imageService.uploadImage(imagefile);

      // 상품에 이미지 번호 설정
      productDTO.getProductImages().add(imageId);

      // 상품 등록
      productService.save(productDTO);

      return "redirect:/admin_product_list"; // 상품 목록 페이지로 이동
    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("error", "상품 등록에 실패하였습니다.");
      return "redirect:/admin_productlist";
    }
  }


 */