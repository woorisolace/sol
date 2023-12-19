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
    return "/main";
  }

}
