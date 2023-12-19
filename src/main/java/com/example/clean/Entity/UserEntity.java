package com.example.clean.Entity;

import com.example.clean.Constant.Role;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")@SequenceGenerator(
        name = "user_SEQ",
        sequenceName = "user_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class UserEntity extends BaseEntity{

    //기본키
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_SEQ")
    private Integer id;
    
    @Column(name="email", columnDefinition="VARCHAR(100)", nullable=false, unique=true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name="oauth_type", columnDefinition="VARCHAR(50)")
    private String oauthType;

    @Column(name="tel", columnDefinition = "VARCHAR(30)")
    private String tel;//전화번호

    @Column(name="postcode", columnDefinition = "VARCHAR(30)")
    private String sample6_postcode;//우편번호

    @Column(name="address", columnDefinition = "VARCHAR(50)")
    private String sample6_address;//주소

    @Column(name="detailAddress", columnDefinition = "VARCHAR(50)")
    private String sample6_detailAddress;//상세주소

    @Column(name="extraAddress", columnDefinition = "VARCHAR(50)")
    private String sample6_extraAddress;//참고항목

    @Enumerated(EnumType.STRING)
    private Role role;//회원분류

}


