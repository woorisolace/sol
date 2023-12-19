package com.example.clean.Controller;

import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.AdminNoticeDTO;
import com.example.clean.DTO.FlaskResponseDTO;
import com.example.clean.DTO.MemberDTO;
import com.example.clean.DTO.ProductDTO;
import com.example.clean.Service.AdminNoticeService;
import com.example.clean.Service.AiService;
import com.example.clean.Service.MemberService;
import com.example.clean.Util.Flask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AiController {

  @Autowired
  private Flask flask;

  //S3 이미지 정보
  @Value("${cloud.aws.s3.bucket}")
  public String bucket;

  @Value("${cloud.aws.region.static}")
  public String region;

  @Value("${imgUploadLocation}")
  public String folder;

  private final AiService aiService;
  private final MemberService memberService;
  private final AdminNoticeService adminNoticeService;

  //이미지 인식페이지
  @GetMapping("/ai_img_search")
  public String imgsearchForm (Authentication auth, Model model,
                               HttpServletRequest request) throws Exception {

    // 로그인한 사용자의 정보를 가져옴
    MemberDTO memberDTO = null;
    if (auth != null && auth.isAuthenticated()) {
      String userId = auth.getName();

      // 로그인한 회원의 이메일을 사용하여 회원 정보 조회
      memberDTO = memberService.find(userId);

      if (memberDTO != null) {
        return "/ai/img_search";
      }
    } else {
      model.addAttribute("message", "회원만 이용가능합니다.");
      model.addAttribute("searchUrl", "/login");

      // 원래 페이지의 URL을 세션에 저장
      HttpSession session = request.getSession();
      session.setAttribute("originalRequestUrl", "/ai_img_search");

      return "message";
    }
    return "/ai/img_search";
  }


  //이미지 인식페이지 - 다시하기
  @PostMapping("/ai_img_search")
  public String imgsearchProc(Model model) throws Exception {

    return "redirect:/ai_img_search";
  }

  // class 정보에 따라 roomType을 결정
  private String determineRoomType(String className) {
    switch (className) {
      case "toilet":
        return "BATHROOM";
      case "kitchensink":
        return "KITCHEN";
      case "etc":
        return "LIVING";
      // 필요한 경우 다른 클래스에 대한 처리 추가
      default:
        return null;
    }
  }

  // 이미지 결과 페이지에 대한 POST 처리
  @PostMapping("/ai_img_result")
  public String imgResultProc(@RequestParam("file") MultipartFile file,
                              @RequestParam(value = "categoryType", defaultValue = "MEMBERSALE") String categoryType,
                              @PageableDefault(page = 1) Pageable pageable,
                              Model model) {

    try {
      // 플라스크 서버에 분석할 이미지를 전달하여 처리
      FlaskResponseDTO dtos = flask.requestToFlask(file);
      //S3 이미지 정보 전달
      model.addAttribute("bucket", bucket);
      model.addAttribute("region", region);
      model.addAttribute("folder", folder);
      model.addAttribute("dtos", dtos);

      List<String> sellStateOptions = Arrays.stream(SellStateRole.values())
          .map(SellStateRole::getDescription)
          .collect(Collectors.toList());
      model.addAttribute("sellStateOptions", sellStateOptions); // 판매상태 옵션을 전달

      // CategoryTypeRole 열거형의 한글 설명을 리스트로 가져오기
      List<String> categoryOptions = Arrays.stream(CategoryTypeRole.values())
          .map(CategoryTypeRole::getDescription)
          .collect(Collectors.toList());
      model.addAttribute("categoryOptions", categoryOptions);

      // class 정보를 가져와서 해당 roomType을 설정
      Object jsonClassObject = flask.getJsonObject().get("class");
      if (jsonClassObject instanceof List) {
        List<String> classes = (List<String>) jsonClassObject;
        if (!classes.isEmpty()) {
          model.addAttribute("roomType", determineRoomType(classes.get(0)));
        } else {
          // 클래스 목록이 비어 있을 때 에러 메시지를 설정
          model.addAttribute("errorMessage", "이미지 분류에 실패했습니다. 다른 사진을 첨부해주세요.");
          return "/ai/img_search";
        }
      } else {
        // 클래스 목록이 아닌 경우 모델의 "roomType" 속성을 null로 설정
        model.addAttribute("errorMessage", "이미지 분류에 실패했습니다. 다른 사진을 첨부해주세요.");
        return "/ai/img_search";
      }


      Page<AdminNoticeDTO> noticeDTOn = adminNoticeService.findAll("","","",pageable);

      Page<ProductDTO> productDTOS = aiService.memberSaleList(pageable);

      int blockLimit = 5;
      int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
      int endPage = Math.min(startPage + blockLimit - 1, productDTOS.getTotalPages());

      int prevPage = productDTOS.getNumber();
      int currentPage = productDTOS.getNumber() + 1;
      int nextPage = productDTOS.getNumber() + 2;
      int lastPage = productDTOS.getTotalPages();

      model.addAttribute("noticeDTOn",noticeDTOn);
      model.addAttribute("productDTOS", productDTOS);

      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);
      model.addAttribute("prevPage", prevPage);
      model.addAttribute("currentPage", currentPage);
      model.addAttribute("nextPage", nextPage);
      model.addAttribute("lastPage", lastPage);

      return "/ai/img_result";
    } catch (Exception e) {
      // 이미지 분류 실패 시 처리
      model.addAttribute("errorMessage", "이미지 분류에 실패했습니다. 다른 사진을 첨부해주세요.");
      return "/ai/img_search";
    }
  }

}