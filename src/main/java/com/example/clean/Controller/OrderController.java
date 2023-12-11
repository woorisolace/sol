package com.example.clean.Controller;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.DTO.OrderDTO;
import com.example.clean.DTO.ProductDTO;

import com.example.clean.Service.MemberService;
import com.example.clean.Service.OrderService;
import com.example.clean.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

//Controller : 클라이언트로부터 전달된 요청을 처리하고 응답을 생성하는 역할
//DTO를 통해 클라이언트로부터 전달된 데이터를 받거나, 비즈니스 로직에 필요한 데이터를 DTO에 담아 비즈니스 로직을 호출
//비즈니스 로직의 결과를 다시 DTO에 담아 클라이언트에게 응답

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

  private final MemberService memberService;
  private final OrderService orderService;
  private final ProductService productService;

  // 구매 양식 조회
  @GetMapping("/order")
  public String orderForm(Authentication auth,                            //현재 로그인한 사용자의 정보를 가져오기 위해 사용
                          @RequestParam("productId") Integer productId,   //특정 상품을 식별하는 데 사용
                          @RequestParam("productNum") Integer productNum,
                          Model model) throws Exception {

    // 상품 ID로 상품 정보 조회
    ProductDTO productDTO = productService.findOne(productId);
    model.addAttribute("productDTO", productDTO);


    // 로그인한 사용자의 정보를 가져옴
    //isAuthenticated : 사용자 인증(Authentication) 상태를 나타내는 불리언(Boolean) 값을 반환하는 메서드나 속성
    MemberDTO memberDTO = null;
    if (auth != null && auth.isAuthenticated()) {
      String userId = auth.getName();
      // 로그인한 회원의 이메일을 사용하여 회원 정보 조회
      memberDTO = memberService.find(userId);
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
    OrderDTO orderDTO = orderService.orderForm(memberDTO.getEmail(), productDTO.getProductId(), productNum);

    // 모델에 추가
    model.addAttribute("productDTO", productDTO);
    model.addAttribute("memberDTO", memberDTO);
    model.addAttribute("orderDTO", orderDTO);

    return "cartorder/order";
  }

  //주문완료 버튼 누르기
  //주문 정보를 받아서(orderForm에서) 주문을 완료하고 완료 페이지로 이동
  @PostMapping("/order")
  public String orderInfo(Authentication auth,
                          @ModelAttribute OrderDTO orderDTO,
                          //@RequestParam("paymentMethod") String paymentMethod,
                          @RequestParam(value="paymentMethod", required=false) String paymentMethod,
                          RedirectAttributes redirectAttributes,
                          HttpSession session,
                          Model model) throws Exception {

    //상품 정보 조회
    ProductDTO productDTO = productService.findOne(orderDTO.getProductId());
    model.addAttribute("productDTO", productDTO);

    // 로그인한 회원의 이메일을 사용하여 회원 정보 조회
    String userId = auth.getName();
    MemberDTO memberDTO = memberService.find(userId);
    model.addAttribute("memberDTO", memberDTO);

    try {
      // 주문 정보 저장
      // resultOrderDTO 객체 :  주문 시 필요한 모든 정보가 포함
      OrderDTO resultOrderDTO = orderService.orderInfo(memberDTO.getEmail(), productDTO.getProductId(), orderDTO.getProductNum(), orderDTO.getPaymentMethod());
      log.info("Controller_결제방법 : {}", orderDTO.getPaymentMethod());

      // RedirectAttributes를 이용하여 주문 정보 전달
      session.setAttribute("orderDTO", resultOrderDTO);
      redirectAttributes.addFlashAttribute("orderId", resultOrderDTO.getOrderId());
      //redirectAttributes.addFlashAttribute("paymentMethod", resultOrderDTO.getPaymentMethod());

      // 주문이 성공적으로 처리되었을 때
      // 리다이렉트 시에 FlashAttribute 사용
      redirectAttributes.addFlashAttribute("message", "구매 감사합니다.");
      redirectAttributes.addFlashAttribute("searchUrl", "/orderSuccess?orderId=" + resultOrderDTO.getOrderId());
      return "redirect:/orderSuccess";
    } catch (Exception e) {
      // 주문 실패 시
      redirectAttributes.addFlashAttribute("error", "주문 실패하였습니다.");
      redirectAttributes.addFlashAttribute("searchUrl", "/order?productId=" + orderDTO.getProductId());
      return "redirect:/message";
    }
  }




  // 주문완료페이지
  // 주문이 완료된 후에 주문 완료 페이지를 조회
  @GetMapping("/orderSuccess")
  public String getOrderSuccess(HttpSession session, Model model) throws Exception {

    //HttpSession에서 구매 정보를 가져옴 (회원정보, 제품정보 + 배송/결제 정보 등)
    OrderDTO sessionOrderDTO = (OrderDTO) session.getAttribute("orderDTO");

    //sessionOrderDTO가 null이거나 orderId가 null인 경우 처리
    if (sessionOrderDTO == null || sessionOrderDTO.getOrderId() == null) {
      log.error("세션에서 주문 정보 또는 주문 번호를 가져오지 못했습니다.");
      return "error";
    }

    // orderId를 사용하여 주문 정보를 데이터베이스에서 조회
    Integer orderId = sessionOrderDTO.getOrderId();
    OrderDTO orderDTO = orderService.getOrderSuccess(orderId);

    // 주문 정보를 페이지에 전달
    model.addAttribute("orderDTO", orderDTO);
    return "cartorder/orderSuccess";

  }

}

