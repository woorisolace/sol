package com.example.clean.Controller;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.Role;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.MemberDTO;
import com.example.clean.DTO.OrderDTO;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Service.AdminNoticeService;
import com.example.clean.Service.AdminService;
import com.example.clean.Service.MemberService;
import com.example.clean.Service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Log4j2
public class AdminController {

  private final AdminService adminService;
  private final OrderService orderService;

  //어드민
  @GetMapping("/admin")
  public String adminForm() throws Exception {
    return "/admin/admin";
  }


  //회원목록
  @GetMapping("/admin_memberlist")
  public String adminMemberList(@RequestParam(value = "id", defaultValue = "") Integer id,
                                @RequestParam(value = "oauthType", defaultValue = "") String oauthType,
                                @PageableDefault(page = 1) Pageable pageable,
                                Model model) throws Exception {

    MemberDTO memberDTO = new MemberDTO();

    Page<MemberDTO> memberDTOS = adminService.memberList(memberDTO, id, oauthType, pageable);

    memberDTOS.getTotalElements();   //전체 회원 수 조회

    int blockLimit = 5;
    int startPage = (((int)(Math.ceil((double)pageable.getPageNumber()/blockLimit)))-1) * blockLimit+1;
    int endPage = Math.min(startPage+blockLimit-1, memberDTOS.getTotalPages());

    int prevPage = memberDTOS.getNumber();
    int currentPage = memberDTOS.getNumber()+1;
    int nextPage = memberDTOS.getNumber()+2;
    int lastPage = memberDTOS.getTotalPages();

    model.addAttribute("memberDTO", memberDTO);
    model.addAttribute("memberDTOS", memberDTOS);

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    return "/admin/member_list";
  }


  //회원구매내역
  @GetMapping("/admin_memberorderlist")
  public String memberOrderList(Principal principal,
                                @PageableDefault(page=1) Pageable pageable, Model model) throws Exception {

    Page<OrderDTO> orderDTOS = adminService.getAllOrders(pageable);

    int blockLimit = 5;
    int startPage = (((int)(Math.ceil((double)pageable.getPageNumber()/blockLimit)))-1) * blockLimit+1;
    int endPage = Math.min(startPage+blockLimit-1, orderDTOS.getTotalPages());

    int prevPage = orderDTOS.getNumber();
    int currentPage = orderDTOS.getNumber()+1;
    int nextPage = orderDTOS.getNumber()+2;
    int lastPage = orderDTOS.getTotalPages();

    orderDTOS.getTotalElements();   //총 주문수수

    model.addAttribute("orderDTOS", orderDTOS);

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    return "admin/order_list";
  }

}
