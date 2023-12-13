package com.example.clean.Repository;

import com.example.clean.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<UserEntity, Integer> {

    //모든 회원 조회
    Page<UserEntity> findAll(Pageable pageable);

    //회원구분
    Page<UserEntity> findByOauthTypeOrderByReDateDesc(String oauthType, Pageable pageable);

}