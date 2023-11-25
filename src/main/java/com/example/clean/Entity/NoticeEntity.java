package com.example.clean.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "notice")
@SequenceGenerator(
    name = "notice_SEQ",
    sequenceName = "notice_SEQ",
    initialValue = 1,
    allocationSize = 1
)

public class NoticeEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_SEQ")
  private Integer noticeid;

//  @Column(name="category", nullable = false)
//  private String category;       //공지유형
//
//  @Column(name="title",nullable = false,length = 50)
//  private String title;          //제목
//
//  @Column(name="writer", nullable = false,length = 20)
//  private String writer;         //작성자
//
//  @Column(name="content",nullable = false,length = 2000)
//  private String content;        //공지내용
//
//  @ManyToOne
//  @JoinColumn(name="adminidnoticeid")
//  private AdminNoticeEntity adminNoticeEntity;

}