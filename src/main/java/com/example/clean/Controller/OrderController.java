package com.example.clean.Controller;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.DTO.OrderDTO;
import com.example.clean.DTO.OrderInfoDTO;
import com.example.clean.DTO.ProductDTO;

import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Service.MemberService;
import com.example.clean.Service.OrderService;
import com.example.clean.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@Transactional
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final MemberService memberService;
  private final ProductService productService;


  //상세페이지에서 구매페이지로 이동
  //선택한 제품 정보 + 구매자 정보 전달
  @GetMapping("/order")
  public String orderForm(@RequestParam("productId") Integer productId,   //특정 상품을 식별하는 데 사용
                          Authentication auth,                            //현재 로그인한 사용자의 정보를 가져오기 위해 사용
                          RedirectAttributes redirectAttributes,          //리다이렉트 시에 데이터를 전달하기 위해 사용 (주문 정보 등을 다음 페이지로 전달)
                          Model model) throws Exception {


    // 상품 ID로 상품 정보 조회
    ProductDTO productDTO = productService.findOne(productId);
    if (productDTO == null) {
      log.info("Controller_상품 존재하지 않음");
      return "error";   // 상품이 없으면 오류 페이지로 이동
    }
    model.addAttribute("productDTO", productDTO);
    log.info("Controller_상품 번호 확인완료: {}", productDTO.getProductId());


    // 로그인한 사용자의 정보를 가져옴
    MemberDTO memberDTO = null;
    if (auth != null && auth.isAuthenticated()) {
      String userId = auth.getName();

      // 로그인한 회원의 이메일을 사용하여 회원 정보 조회
      memberDTO = memberService.find(userId);
      log.info("Controller_회원 메일 확인완료: {}", userId);

      if (memberDTO == null) {
        log.info("이메일 없음. 로그인페이지로 이동 필요");
        model.addAttribute("message", "회원만 이용가능합니다.");
        model.addAttribute("searchUrl", "/login");
        return "message";
      }
    } else {
      log.info("인증되지 않은 사용자. 로그인페이지로 이동 필요");
      model.addAttribute("message", "회원만 이용가능합니다.");
      model.addAttribute("searchUrl", "/login");
      return "message";
    }

    // 주문 양식 조회
    OrderDTO orderDTO = orderService.orderForm(memberDTO.getEmail(), productDTO.getProductId());

    model.addAttribute("productId", productId);
    model.addAttribute("orderDTO", orderDTO);
    log.info("주문 양식 조회 완료");

    System.out.println("이메일"+orderDTO.getUserEntity().getEmail());

    return "cartorder/order";
  }


  //주문완료 버튼 누르기
  //주문 정보를 받아서(orderForm에서) 주문을 완료하고 완료 페이지로 이동
  @PostMapping("/order")
  public String orderInfo(Authentication auth,
                          @RequestParam("productId") Integer productId,
                          @ModelAttribute OrderDTO orderDTO,  // 구매 페이지에서 입력한 회원의 배송지 정보와 결제 정보를 구매 완료 페이지로 전달
                          RedirectAttributes redirectAttributes,
                          HttpSession session,
                          Model model) throws Exception {

    //상품 정보 조회
    ProductDTO productDTO = productService.findOne(productId);
    model.addAttribute("productDTO", productDTO);
    log.info("Controller_productDTO 확인완료: {}", productDTO);

    // 로그인한 회원의 이메일을 사용하여 회원 정보 조회
    String userId = auth.getName();
    MemberDTO memberDTO = memberService.find(userId);
    model.addAttribute("memberDTO", memberDTO);
    log.info("Controller_memberDTO 확인완료: {}", memberDTO);


    // 주문 정보 저장
    OrderDTO resultOrderInfoDTO = orderService.orderInfo(memberDTO.getEmail(), productDTO.getProductId());
    session.setAttribute("orderInfoDTO", resultOrderInfoDTO);
    log.info("Controller_주문 정보 저장여부: {}", resultOrderInfoDTO);

    // RedirectAttributes를 이용하여 주문 정보 전달
    redirectAttributes.addFlashAttribute("orderInfoId", resultOrderInfoDTO.getOrderId());
    redirectAttributes.addFlashAttribute("userId", memberDTO.getEmail());
    redirectAttributes.addFlashAttribute("productId", productId);

    return "redirect:/orderSuccess";
  }


  // 주문완료페이지
  // 주문이 완료된 후에 주문 완료 페이지를 조회
  @GetMapping("/orderSuccess")
  public String getOrderSuccess(Authentication auth,
                                Model model,
                                HttpSession session) throws Exception {


    // HttpSession에서 구매 정보를 가져옴 (회원정보, 제품정보 + 배송/결제 정보 등)
    OrderDTO orderInfoDTO = (OrderDTO) session.getAttribute("orderInfoDTO");
    if (orderInfoDTO == null) {
      return "error";
    }

    // 구매 정보를 페이지에 전달
    model.addAttribute("orderDTO", orderInfoDTO);

    return "cartorder/orderSuccess";
  }

}
