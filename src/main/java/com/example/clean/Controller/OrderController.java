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
                          RedirectAttributes redirectAttributes,
                          HttpSession session,
                          Model model) throws Exception {

    // 상품 정보 조회
    ProductDTO productDTO = productService.findOne(orderDTO.getProductId());
    model.addAttribute("productDTO", productDTO);

    // 로그인한 회원의 이메일을 사용하여 회원 정보 조회
    String userId = auth.getName();
    MemberDTO memberDTO = memberService.find(userId);
    model.addAttribute("memberDTO", memberDTO);

    try {
      // 주문 정보 저장
      OrderDTO resultOrderDTO = orderService.orderInfo(memberDTO.getEmail(),
          productDTO.getProductId(), orderDTO.getProductNum(), orderDTO.getPaymentMethod());

      // 주문 정보 및 사용자 정보를 세션에 저장
      saveOrderInfoToSession(orderDTO, session);

      // RedirectAttributes를 이용하여 주문 정보 전달
      session.setAttribute("orderDTO", resultOrderDTO);
      redirectAttributes.addFlashAttribute("orderId", resultOrderDTO.getOrderId());

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

  // 메소드로 추출
  private void saveOrderInfoToSession(OrderDTO orderDTO, HttpSession session) {
    if (orderDTO.getUserEntity() != null) {
      // 세션에 주문 정보 설정
      session.setAttribute("nickname", orderDTO.getUserEntity().getNickname());
      session.setAttribute("sample6_postcode", orderDTO.getUserEntity().getSample6_postcode());
      session.setAttribute("sample6_detailAddress", orderDTO.getUserEntity().getSample6_detailAddress());
      session.setAttribute("sample6_extraAddress", orderDTO.getUserEntity().getSample6_extraAddress());
      session.setAttribute("tel", orderDTO.getUserEntity().getTel());
      session.setAttribute("email", orderDTO.getUserEntity().getEmail());
    }
  }

  @GetMapping("/orderSuccess")
  public String getOrderSuccess(HttpSession session, Model model) throws Exception {
    // 세션에서 주문 정보 읽기
    String nickname = (String) session.getAttribute("nickname");
    String sample6_postcode = (String) session.getAttribute("sample6_postcode");
    String sample6_detailAddress = (String) session.getAttribute("sample6_detailAddress");
    String sample6_extraAddress = (String) session.getAttribute("sample6_extraAddress");
    String tel = (String) session.getAttribute("tel");
    String email = (String) session.getAttribute("email");

    OrderDTO sessionOrderDTO = (OrderDTO) session.getAttribute("orderDTO");

    // sessionOrderDTO가 null이거나 orderId가 null인 경우 처리
    if (sessionOrderDTO == null || sessionOrderDTO.getOrderId() == null) {
      log.error("세션에서 주문 정보 또는 주문 번호를 가져오지 못했습니다.");
      return "error";
    }

    // orderId를 사용하여 주문 정보를 데이터베이스에서 조회
    Integer orderId = sessionOrderDTO.getOrderId();
    OrderDTO orderDTO = orderService.getOrderSuccess(orderId);

    // 주문 정보와 세션에서 가져온 값을 모델에 추가
    model.addAttribute("orderDTO", orderDTO);
    model.addAttribute("nickname", nickname);
    model.addAttribute("sample6_postcode", sample6_postcode);
    model.addAttribute("sample6_detailAddress", sample6_detailAddress);
    model.addAttribute("sample6_extraAddress", sample6_extraAddress);
    model.addAttribute("tel", tel);
    model.addAttribute("email", email);

    return "cartorder/orderSuccess";
  }

}
