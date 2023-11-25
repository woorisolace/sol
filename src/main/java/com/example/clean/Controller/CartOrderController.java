package com.example.clean.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartOrderController {

  //장바구니목록
  @GetMapping("/cart")
  public String cartlistForm() throws Exception {
    return "/cartorder/cart";
  }

  //구매입력
  @GetMapping("/order")
  public String orderForm() throws Exception {
    return "/cartorder/order";
  }
  //구매완료
  @GetMapping("/orderdetail")
  public String orderdetailForm() throws Exception {
    return "/cartorder/order_detail";
  }

  //checkout
  @GetMapping("/checkout")
  public String checkoutForm() throws Exception {
    return "/cartorder/checkout";
  }
}
