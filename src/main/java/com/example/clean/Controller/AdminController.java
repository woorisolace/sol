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

    int blockLimit = 10;
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

  //회원주문목록 조회
  @GetMapping("/admin_memberorderlist")
  public String adminOrderList(@PageableDefault(page = 1) Pageable pageable,
                               Model model) throws Exception {
    try {
      // 모든 회원 주문 조회
      Page<OrderDTO> allOrders = orderService.getAllOrders(pageable);

      int blockLimit = 10;
      int startPage = (((int)(Math.ceil((double)pageable.getPageNumber()/blockLimit)))-1) * blockLimit+1;
      int endPage = Math.min(startPage+blockLimit-1, allOrders.getTotalPages());

      int prevPage = allOrders.getNumber();
      int currentPage = allOrders.getNumber()+1;
      int nextPage = allOrders.getNumber()+2;
      int lastPage = allOrders.getTotalPages();

      // 모델에 주문 목록 추가
      model.addAttribute("allOrders", allOrders);

      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);
      model.addAttribute("prevPage", prevPage);
      model.addAttribute("currentPage", currentPage);
      model.addAttribute("nextPage", nextPage);
      model.addAttribute("lastPage", lastPage);
    } catch (Exception e) {
      // 예외 처리
      e.printStackTrace(); // 실제로는 로깅 등으로 변경해야 함
      model.addAttribute("error", "주문 목록을 불러오는 중 오류가 발생했습니다.");
    }

    return "/admin/order_list";
  }
}
