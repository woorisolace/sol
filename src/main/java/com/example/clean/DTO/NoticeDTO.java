package com.example.clean.DTO;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    private Integer noticeid;      //번호

    @NotEmpty(message="공지유형은 생략 할 수 없습니다.")
    private String category;       //공지유형
    @NotEmpty(message="제목은 생략할 수 없습니다.")
    private String title;          //제목
    @NotEmpty(message="작성자는 생략할 수 없습니다.")
    private String writer;         //작성자
    @NotEmpty(message="공지내용은 생략할 수 없습니다.")
    private String content;        //공지내용

    private LocalDateTime reDate;               // 등록날짜
    private LocalDateTime modDate;             // 수정날짜
}
