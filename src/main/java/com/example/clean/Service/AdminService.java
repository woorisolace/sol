package com.example.clean.Service;

import com.example.clean.Entity.UserEntity;
import com.example.clean.Repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    //관리자 - 회원 조회(회원 목록 페이지)
    public List<UserEntity> memberList() {
        return adminRepository.findAll();
    }


}
