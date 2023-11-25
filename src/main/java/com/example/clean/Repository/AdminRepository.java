package com.example.clean.Repository;

import com.example.clean.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<UserEntity, Integer> {
    //관리자 - 회원 조회(회원 목록 페이지)
    List<UserEntity> findAll();
}
