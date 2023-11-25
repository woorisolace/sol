package com.example.clean.Repository;

import com.example.clean.DTO.AdminNoticeDTO;
import com.example.clean.Entity.AdminNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AdminNoticeRepository extends JpaRepository<AdminNoticeEntity, Integer> {
    //오름차순(ASC) , 내림차순(DESC)
    //


}
