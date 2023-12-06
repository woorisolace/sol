package com.example.clean.DTO;


import com.example.clean.Constant.AdminNoticeRole;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;



@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminNoticeDTO {


    private Integer adminnoticeid;      //번호

    @NotBlank(message="공지유형은 생략 할 수 없습니다.")
    private String admincategory;       //공지유형

    @NotEmpty(message="제목은 생략할 수 없습니다.")
    private String admintitle;          //제목

    @NotEmpty(message="작성자는 생략할 수 없습니다.")
    private String adminwriter;         //작성자

    @NotEmpty(message="공지내용은 생략할 수 없습니다.")
    private String admincontent;        //공지내용

    //공지 유형 - Enum값을 문자열로 저장
    private AdminNoticeRole adminNoticeRole;

    private LocalDateTime reDate;               // 등록날짜
    private LocalDateTime modDate;             // 수정날짜
}
