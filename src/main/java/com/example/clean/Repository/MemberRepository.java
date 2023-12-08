package com.example.clean.Repository;

import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Repository;

import java.util.List;

//데이터베이스를 사용하기 위한 SQL
@Repository
public interface MemberRepository extends JpaRepository<UserEntity, String> {

    //로그인은 이메일로 조회(MemberEntity에서 email필드로 조회)
    UserEntity findByEmail(String email);

}

//UserEntity findById(Integer id);