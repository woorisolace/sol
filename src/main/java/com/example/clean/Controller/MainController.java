package com.example.clean.Controller;

import com.example.clean.DTO.ProductDTO;
import com.example.clean.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
  private final ProductService productService;

  //메인페이지
  @GetMapping("/")
  public String loginForm(Model model) throws Exception {
    // 상위 n개의 베스트 상품을 가져와서 모델에 추가
    //List<ProductDTO> bestProducts = productService.getBestProducts(4);
    //model.addAttribute("bestProducts", bestProducts);

    return "/main";
  }

  //회사소개
  @GetMapping("/companyinfo")
  public String companyinfoForm() throws Exception {
    return "/companyinfo";
  }

}
