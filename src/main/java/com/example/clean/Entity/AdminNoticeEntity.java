package com.example.clean.Entity;

import com.example.clean.Constant.AdminNoticeRole;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
@Table(name="adminnotice")
@SequenceGenerator(
    name = "adminnotice_SEQ",
    sequenceName = "adminnotice_SEQ",
    initialValue = 1,
    allocationSize = 1
)
public class AdminNoticeEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adminnotice_SEQ")
    @Column(name="adminnoticeid")
    private Integer adminnoticeid;

    @Column(name="admincategory", nullable = false)
    private String admincategory;       //공지유형

    @Column(name="admintitle",nullable = false,length = 50)
    private String admintitle;          //제목

    @Column(name="adminwriter", nullable = false,length = 20)
    private String adminwriter;         //작성자

    @Column(name="admincontent",nullable = false,length = 2000)
    private String admincontent;        //공지내용

    @Enumerated(EnumType.STRING)
    @Column(name="adminnotice")
    private AdminNoticeRole adminNoticeRole;

}
