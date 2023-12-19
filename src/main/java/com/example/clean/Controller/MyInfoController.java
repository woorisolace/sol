package com.example.clean.Controller;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.DTO.OrderDTO;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Repository.MemberRepository;
import com.example.clean.Service.MemberService;
import com.example.clean.Service.OrderService;
import com.example.clean.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyInfoController {

  //S3 이미지 정보
  @Value("${cloud.aws.s3.bucket}")
  public String bucket;

  @Value("${cloud.aws.region.static}")
  public String region;

  @Value("${imgUploadLocation}")
  public String folder;

  private final MemberService memberService;
  private final OrderService orderService;
  private final ProductService productService;

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
  public String myOrderList(Principal principal,
                            @PageableDefault(page=1) Pageable pageable,
                            Model model) throws Exception {

    // 주문 내역을 페이징 정보에 맞게 조회
    Page<OrderDTO> orderDTOS = orderService.myOrderList(principal.getName(), pageable);

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

    //S3 이미지 정보 전달
    model.addAttribute("bucket", bucket);
    model.addAttribute("region", region);
    model.addAttribute("folder", folder);

    return "/member/oder_list";
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