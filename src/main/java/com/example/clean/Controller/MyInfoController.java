package com.example.clean.Controller;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.DTO.OrderDTO;
import com.example.clean.Repository.MemberRepository;
import com.example.clean.Service.MemberService;
import com.example.clean.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MyInfoController {
  private MemberRepository memberRepository;

  private final MemberService memberService;
  private final OrderService orderService;
  private final PasswordEncoder passwordEncoder;

  //마이페이지메인
  @GetMapping("/mypage")
  public String mypage1Form(Model model, Principal principal) throws Exception {
    MemberDTO memberDTO = memberService.myinfoDetail(principal);
    model.addAttribute("memberDTO", memberDTO);
    return "/member/my_page";
  }


  //내정보상세
  @GetMapping("/myinfo_detail")
  public String myinfodetailForm(Model model, Principal principal) throws Exception {
    MemberDTO memberDTO = memberService.myinfoDetail(principal);
    model.addAttribute("memberDTO", memberDTO);
    return "member/myinfo_detail";
  }
  //내정보수정
  @GetMapping("/myinfo_update")
  public String myinfoupdateForm() throws Exception {
    return "/member/myinfo_update";
  }


  //나의주문내역
  @GetMapping("/member_oderlist")
  public String myOrderList(Model model, Principal principal, @PageableDefault(page = 1) Pageable pageable) throws Exception {
    try {
      // 주문 내역을 페이징 정보에 맞게 조회
      Page<OrderDTO> orderDTOPage = orderService.getOrderList(principal.getName(), pageable);

      int blockLimit = 10;
      int startPage = (((int)(Math.ceil((double)pageable.getPageNumber()/blockLimit)))-1) * blockLimit+1;
      int endPage = Math.min(startPage+blockLimit-1, orderDTOPage.getTotalPages());

      int prevPage = orderDTOPage.getNumber();
      int currentPage = orderDTOPage.getNumber()+1;
      int nextPage = orderDTOPage.getNumber()+2;
      int lastPage = orderDTOPage.getTotalPages();

      //주문 목록 주문 항목 역순으로 매긴 번호 할당


      model.addAttribute("orderDTOPage", orderDTOPage);

      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);
      model.addAttribute("prevPage", prevPage);
      model.addAttribute("currentPage", currentPage);
      model.addAttribute("nextPage", nextPage);
      model.addAttribute("lastPage", lastPage);

    } catch (Exception e) {
      // 예외 처리
      e.printStackTrace(); // 실제로는 로깅 등으로 변경해야 함
      model.addAttribute("error", "주문 내역을 불러오는 중 오류가 발생했습니다.");
    }

    return "/member/oder_list";
  }

  //나의주문상세
  @GetMapping("/member_oderdetail")
  public String myoderdetailForm() throws Exception {
    return "/member/oder_detail";
  }


  //나의리뷰목록
  @GetMapping("/myreviewlist")
  public String myreviewlistForm() throws Exception {
    return "/member/my_review_list";
  }
  //나의리뷰상세
  @GetMapping("/myreviewdetail")
  public String myreviewdetailForm() throws Exception {
    return "/member/my_review";
  }


  //나의관심상품
  @GetMapping("/mylikelist")
  public String mylikelistForm() throws Exception {
    return "/member/my_like_list";
  }

}