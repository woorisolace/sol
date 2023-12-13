package com.example.clean.Controller;

import com.example.clean.Constant.AdminNoticeRole;
import com.example.clean.DTO.AdminNoticeDTO;
import com.example.clean.Repository.AdminNoticeRepository;
import com.example.clean.Service.AdminNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Log4j2
public class AdminNoticeController {

  private final AdminNoticeService adminNoticeService;
  private final AdminNoticeRepository adminNoticeRepository;

  //공지목록 -> 버튼, 검색창 추가 수정 필요
  @GetMapping("/admin_noticelist")
  public String noticelistForm(@PageableDefault(page = 1) Pageable pageable, Model model) throws Exception {

    Page<AdminNoticeDTO> noticeDTOn = adminNoticeService.findAll("","","",pageable);


    //페이지 정보
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

    return "/admin/notice_list";
  }



  @PostMapping("/admin_noticelist")
  public String noticelistPorc(@RequestParam(value = "type", defaultValue = "") String type,
                               @RequestParam(value = "title", defaultValue = "")String title,
                               @RequestParam(value = "adminNoitce", defaultValue = "") String adminNoitce,
                               @PageableDefault(page = 1) Pageable pageable,
                               Model model) throws Exception {

    //    Page<AdminNoticeDTO> noticeDTOn = adminNoticeService.list(pageable);
    //열거형의 한글 설명을 리스트로 가져오기
    List<String> adminnoticeOptions = Arrays.stream(AdminNoticeRole.values())
        .map(AdminNoticeRole::getDescription)
        .collect(Collectors.toList());
    model.addAttribute("adminnoticeOptions",adminnoticeOptions); //공지유형 옵션 전달

    Page<AdminNoticeDTO> noticeDTOn = adminNoticeService.findAll(type, title, adminNoitce, pageable);

    noticeDTOn.getTotalElements(); //전체게시글 조회

    //페이지 정보
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

    model.addAttribute("type",type);
    model.addAttribute("title",title);

    model.addAttribute("noticeDTOn",noticeDTOn);
    log.info(noticeDTOn);

    return "/admin/notice_list";
  }



  //공지등록
  @GetMapping("/admin_noticeinsert")
  public String noticeinsertForm(Model model,RedirectAttributes redirectAttributes) throws Exception {
    AdminNoticeDTO noticeDTO = new AdminNoticeDTO();

    model.addAttribute("noticeDTO",noticeDTO);
    log.info(noticeDTO);
    return "admin/notice_insert";
  }

  //공지등록처리
  @PostMapping("/admin_noticeinsert")
  public String adminnoticeinsertProc(@Valid @ModelAttribute("noticeDTO") AdminNoticeDTO noticeDTO, BindingResult bindingResult, Model model)throws Exception{
    System.out.println("dsa");

    if(bindingResult.hasErrors()){ //검증 오류시 등록페이지로 이동
      return "admin/notice_insert";
    }
    System.out.println(noticeDTO.toString());

    adminNoticeService.register(noticeDTO);

    return "redirect:/admin_noticelist";
  }


  //공지수정
  @GetMapping("/admin_noticedetail")
  public String adminnoticedetailForm(@RequestParam(required = false) Integer adminnoticeid, Model model, RedirectAttributes redirectAttributes) throws Exception {
    if(adminnoticeid == null){
      return "error";
    }
    AdminNoticeDTO noticeDTO = adminNoticeService.detail(adminnoticeid);
    model.addAttribute("noticeDTO",noticeDTO);
    return "/admin/notice_detail";
  }


  @PostMapping("/admin_noticedetail")
  public String adminnoticedetailProc(@ModelAttribute("noticeDTO") @Valid AdminNoticeDTO adminNoticeDTO,
                                      BindingResult bindingResult, Model model,
                                      RedirectAttributes redirectAttributes) throws Exception {

    if(bindingResult.hasErrors()){
      return "/admin/notice_detail";
    }
    if(adminNoticeDTO.getAdminnoticeid() == null){
      return "error";
    }

    adminNoticeService.update(adminNoticeDTO);
    redirectAttributes.addAttribute("currentPage",1);

    return "redirect:/admin_noticelist";
  }



  //공지상세
  @GetMapping("/admin_noticeread")
  public String adminnoticeread(@RequestParam(required = false) Integer adminnoticeid, Model model, RedirectAttributes redirectAttributes) throws Exception {

    if(adminnoticeid == null){
      return "error";
    }
    AdminNoticeDTO noticeDTO = adminNoticeService.detail(adminnoticeid);

    //이전,다음버튼
    Integer prevNoticeId = adminNoticeService.findPreviousNoticeId(adminnoticeid);
    Integer nextNoticeId = adminNoticeService.findNextNoticeId(adminnoticeid);

    model.addAttribute("noticeDTO",noticeDTO);
    model.addAttribute("prevNoticeId",prevNoticeId);
    model.addAttribute("nextNoticeId",nextNoticeId);

    return "admin/notice_read";
  }


  //공지삭제
  @GetMapping("/admin_noticeremove")
  public String adminremoveProc(Integer adminnoticeid, Model model) throws Exception{

    adminNoticeService.remove(adminnoticeid);

    return "redirect:/admin_noticelist";
  }

}