package com.example.clean.Controller;

import com.example.clean.Constant.AdminNoticeRole;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Log4j2
public class MemberNoticeController {

  private final AdminNoticeService adminNoticeService;

  //회원공지사항 목록
  @GetMapping("/notice_list")
  public String memberlistForm(
      @RequestParam(value = "type", defaultValue = "") String type,
      @RequestParam(value = "admintitle", defaultValue = "") String admintitle,
      @RequestParam(value = "adminNoitce", defaultValue = "") String adminNoitce,
      @PageableDefault(page = 1) Pageable pageable,
      Model model) throws Exception {

    Page<AdminNoticeDTO> adminNoticeDTOS = adminNoticeService.findAll(type, admintitle, adminNoitce, pageable);

    model.addAttribute("noticeDTOS", adminNoticeDTOS.getContent());
    model.addAttribute("type", "t");

    // 열거형의 한글 설명을 리스트로 가져오기
    List<String> adminnoticeOptions = Arrays.stream(AdminNoticeRole.values())
        .map(AdminNoticeRole::getDescription)
        .collect(Collectors.toList());
    model.addAttribute("adminnoticeOptions", adminnoticeOptions); // 공지유형 옵션 전달

    // 페이지 정보
    int blockLimit = 10;
    // 시작 페이지
    int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
    // 끝 페이지
    int endPage = Math.min(startPage + blockLimit - 1, adminNoticeDTOS.getTotalPages());

    // 페이지 버튼 정보 (HTML에서 작성도 가능, [첫 이전, 페이지 번호, 다음, 끝]
    int prevPage = adminNoticeDTOS.getNumber();         // 이전 페이지
    int currentPage = adminNoticeDTOS.getNumber() + 1;    // 현재 페이지
    int nextPage = adminNoticeDTOS.getNumber() + 2;       // 다음 페이지
    int lastPage = adminNoticeDTOS.getTotalPages();         // 마지막 페이지

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("lastPage", lastPage);

    model.addAttribute("type", type);
    model.addAttribute("admintitle", admintitle);

    model.addAttribute("adminNoticeDTOS", adminNoticeDTOS);
    log.info(adminNoticeDTOS.getContent());
    log.info("Notice DTOs : {}", adminNoticeDTOS.getContent());

    return "/notice/notice_list";
  }



  @GetMapping("/searchNotices")
  //RequestParam(html에서 전달받을 변수)(value="변수명",defaultvalue="값이 없을 때 대체할 값"
  public String searchAdminNotices(
      @RequestParam(name = "admintitle", defaultValue = "")String admintitle,
      Model model)throws Exception{
    //Service 처리
    List<AdminNoticeDTO> noticeDTOS = adminNoticeService.search(admintitle);

    model.addAttribute("noticeDTOS",noticeDTOS);
    model.addAttribute("type","t");

    return "forward:/notice_list";

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
