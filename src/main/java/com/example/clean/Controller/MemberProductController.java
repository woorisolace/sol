package com.example.clean.Controller;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class MemberProductController {

  private final ProductService productService;

  //회원 제품 목록 - (카테고리별로 진열)
  @GetMapping("/productlist")
  public String productlistForm(
      @RequestParam(value = "type",defaultValue = "")String type,
      @RequestParam(value = "keyword",defaultValue = "")String keyword,
      @RequestParam(value = "sellsState",defaultValue = "")String sellState,
      @RequestParam(value = "categoryType",defaultValue = "")String categoryType,
      @PageableDefault(page = 1)Pageable pageable,
      Model model)throws Exception{
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
    //model.addAttribute("sellStateOptions", sellStateOptions);   // 판매상태 옵션을 전달
    //model.addAttribute("categoryOptions", categoryOptions);     // 카테고리 옵션을 전달

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    model.addAttribute("type", type);
    model.addAttribute("keyword", keyword);

    return "product/list";
  }




  //제품 카테고리 LIVING 페이지
  @GetMapping("/livinglist")
  public String livinglistForm(
          @RequestParam(value = "type",defaultValue = "")String type,
          @RequestParam(value = "keyword",defaultValue = "")String keyword,
          @RequestParam(value = "sellState",defaultValue = "")String sellState,
          @RequestParam(value = "categoryType",defaultValue = "LIVING")String categoryType,
          @PageableDefault(page = 1)Pageable pageable,
          Model model)throws Exception{
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
    //model.addAttribute("sellStateOptions", sellStateOptions);   // 판매상태 옵션을 전달
    //model.addAttribute("categoryOptions", categoryOptions);     // 카테고리 옵션을 전달

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    model.addAttribute("type", type);
    model.addAttribute("keyword", keyword);

    return "product/livinglist";
  }


  //제품 상세페이지 (혜선 작업중)
  @GetMapping("/productdetail")
  public String memProDetail(Integer productId, Model model) throws Exception {

    ProductDTO productDTO = productService.findOne(productId);
    model.addAttribute("productDTO", productDTO);
    return "/product/productdetail";
  }




//  제품 카테고리 BATHROOM("욕실용품") 페이지
@GetMapping("/restroomlist")
public String RestroomlistForm(
        @RequestParam(value = "type",defaultValue = "")String type,
        @RequestParam(value = "keyword",defaultValue = "")String keyword,
        @RequestParam(value = "sellState",defaultValue = "")String sellState,
        @RequestParam(value = "categoryType",defaultValue = "BATHROOM")String categoryType,
        @PageableDefault(page = 1)Pageable pageable,
        Model model)throws Exception{
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
  //model.addAttribute("sellStateOptions", sellStateOptions);   // 판매상태 옵션을 전달
  //model.addAttribute("categoryOptions", categoryOptions);     // 카테고리 옵션을 전달

  model.addAttribute("startPage", startPage);
  model.addAttribute("endPage", endPage);
  model.addAttribute("prevPage", prevPage);
  model.addAttribute("currentPage", currentPage);
  model.addAttribute("nextPage", nextPage);
  model.addAttribute("lastPage", lastPage);

  model.addAttribute("type", type);
  model.addAttribute("keyword", keyword);

  return "product/restroomlist";
}





//  제품 카테고리 KITCHEN("주방용품") 페이지

  @GetMapping("/kitchenlist")
  public String KitchenlistForm(
          @RequestParam(value = "type",defaultValue = "")String type,
          @RequestParam(value = "keyword",defaultValue = "")String keyword,
          @RequestParam(value = "sellState",defaultValue = "")String sellState,
          @RequestParam(value = "categoryType",defaultValue = "KITCHEN")String categoryType,
          @PageableDefault(page = 1)Pageable pageable,
          Model model)throws Exception{
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
    //model.addAttribute("sellStateOptions", sellStateOptions);   // 판매상태 옵션을 전달
    //model.addAttribute("categoryOptions", categoryOptions);     // 카테고리 옵션을 전달

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    model.addAttribute("type", type);
    model.addAttribute("keyword", keyword);

    return "product/kitchenlist";
  }



//  제품 카테고리 MEMBERSALE("회원전용") 페이지

  @GetMapping("/salelist")
  public String SalelistForm(
          @RequestParam(value = "type",defaultValue = "")String type,
          @RequestParam(value = "keyword",defaultValue = "")String keyword,
          @RequestParam(value = "sellState",defaultValue = "")String sellState,
          @RequestParam(value = "categoryType",defaultValue = "MEMBERSALE")String categoryType,
          @PageableDefault(page = 1)Pageable pageable,
          Model model)throws Exception{
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
    //model.addAttribute("sellStateOptions", sellStateOptions);   // 판매상태 옵션을 전달
    //model.addAttribute("categoryOptions", categoryOptions);     // 카테고리 옵션을 전달

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    model.addAttribute("type", type);
    model.addAttribute("keyword", keyword);

    return "product/salelist";
  }






  /*
    //회원공지사항 상세
  @GetMapping("/notice_detail")
  public String  noticedetailForm(@RequestParam(required = false) Integer adminnoticeid, Model model, RedirectAttributes redirectAttributes) throws Exception {

    if(adminnoticeid == null){
      return "error";
    }
    AdminNoticeDTO noticeDTO = adminNoticeService.detail(adminnoticeid);
    model.addAttribute("noticeDTO",noticeDTO);
    return "/notice/notice_detail";
  }
   */



}

