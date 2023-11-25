package com.example.clean.Controller;

import com.example.clean.DTO.AdminNoticeDTO;
import com.example.clean.Service.AdminNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@Log4j2
public class MemberNoticeController {

  private final AdminNoticeService adminNoticeService;
  
  //회원공지사항 목록
  @GetMapping("/notice_list")
  public String insertProc(@PageableDefault(page = 1) Pageable pageable, Model model) throws Exception {

    Page<AdminNoticeDTO> noticeDTOS = adminNoticeService.list(pageable);
    
    //페이지 정보
    int blockLimit = 10;
    //시작페이지
    int startPage=(((int)(Math.ceil((double)pageable.getPageNumber()/blockLimit)))-1) * blockLimit+1;
    //끝페이지
    int endPage = Math.min(startPage+blockLimit-1, noticeDTOS.getTotalPages());

    //페이지버튼 정보 (HTML에서 작성도 가능, [첫 이전, 페이지 번호, 다음, 끝]
    int prevPage = noticeDTOS.getNumber();         //이전페이지
    int currentPage = noticeDTOS.getNumber()+1;    //현재페이지
    int nextPage = noticeDTOS.getNumber()+2;       //다음페이지
    int lastPage = noticeDTOS.getTotalPages();         //마지막페이지
    
    model.addAttribute("startPage",startPage);
    model.addAttribute("endPage",endPage);
    model.addAttribute("prevPage",prevPage);
    model.addAttribute("currentPage",currentPage);
    model.addAttribute("nextPage",nextPage);
    model.addAttribute("lastPage",lastPage);

    model.addAttribute("noticeDTOS",noticeDTOS);
    log.info(noticeDTOS);
    return "/notice/notice_list";
  }


  //회원공지사항 상세
  @GetMapping("/notice_detail")
  public String  noticedetailForm(@RequestParam(required = false) Integer adminnoticeid, Model model, RedirectAttributes redirectAttributes) throws Exception {

    if(adminnoticeid == null){
      return "error";
    }
    AdminNoticeDTO noticeDTO = adminNoticeService.detail(adminnoticeid);
    model.addAttribute("noticeDTO",noticeDTO);
    return "/notice/notice_detail";
  }

}
