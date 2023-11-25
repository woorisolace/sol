package com.example.clean.Controller;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Service.AdminNoticeService;
import com.example.clean.Service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Log4j2
public class AdminController {

  private final AdminService adminService;

  //어드민
  @GetMapping("/admin")
  public String adminForm() throws Exception {
    return "/admin/admin";
  }

  //관리자  회원 목록 페이지
  @GetMapping("/admin_memberlist")
  public String adminMemberList(Model model) throws Exception {
    List<UserEntity> userEntityList = adminService.memberList();
    model.addAttribute("userEntityList", userEntityList);
    /*//페이지 정보
    Page<AdminNoticeDTO> noticeDTOn = adminNoticeService.list(pageable);
    int blockLimit = 10;
    //시작페이지
    int startPage=(((int)(Math.ceil((double)pageable.getPageNumber()/blockLimit)))-1) * blockLimit+1;
    //끝페이지
    int endPage = Math.min(startPage+blockLimit-1, noticeDTOn.getTotalPages());

    //페이지버튼 정보 (HTML에서 작성도 가능, [첫 이전, 페이지 번호, 다음, 끝]
    int prevPage = noticeDTOn.getNumber();         //이전페이지
    int currentPage = noticeDTOn.getNumber()+1;    //현재페이지
    int nextPage = noticeDTOn.getNumber()+2;       //다음페이지
    int lastPage = noticeDTOn.getTotalPages();         //마지막페이지


    model.addAttribute("startPage",startPage);
    model.addAttribute("endPage",endPage);
    model.addAttribute("prevPage",prevPage);
    model.addAttribute("currentPage",currentPage);
    model.addAttribute("nextPage",nextPage);
    model.addAttribute("lastPage",lastPage);

    model.addAttribute("noticeDTOn",noticeDTOn);
    log.info(noticeDTOn);
    * */
    return "/admin/member_list";
  }

}
