package com.example.clean.Repository;

import com.example.clean.Constant.AdminNoticeRole;
import com.example.clean.Constant.CategoryTypeRole;
import com.example.clean.Constant.SellStateRole;
import com.example.clean.DTO.AdminNoticeDTO;
import com.example.clean.Entity.AdminNoticeEntity;
import com.example.clean.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AdminNoticeRepository extends JpaRepository<AdminNoticeEntity, Integer> {
    //오름차순(ASC) , 내림차순(DESC)

    @Query("SELECT a FROM AdminNoticeEntity a WHERE a.admintitle like %:admintitle% order by a.adminnoticeid ASC ")
    List<AdminNoticeEntity> findSearch(String admintitle);

    @Query("SELECT a FROM AdminNoticeEntity a WHERE a.admintitle like %:title%")
    Page<AdminNoticeEntity> findAdmintitle(@Param("title") String title,Pageable pageable);


    @Query("SELECT a FROM AdminNoticeEntity a WHERE a.adminNoticeRole = :adminNoticeRole")
    Page<AdminNoticeEntity> findByNoticeType(@Param("adminNoticeRole")AdminNoticeRole adminNoticeRole, Pageable pageable);

    @Query("SELECT n FROM AdminNoticeEntity  n WHERE " +
        "(:type IS NULL OR " +
        "(:type = 't' AND n.admintitle like %:title%) OR " +
        "(:type = 'c' AND n.admincategory LIKE %:adminnotice%))")
    Page<AdminNoticeEntity> findBySearch(
        @Param("type") String type,
        @Param("title") String title,
        @Param("adminnotice") String adminnotice,
        Pageable pageable
    );
    @Query("SELECT MAX(a.adminnoticeid) FROM AdminNoticeEntity a WHERE a.adminnoticeid < :currentNoticeId")
    Integer findPreviousNoticeId(@Param("currentNoticeId") Integer currentNoticeId);

    @Query("SELECT COALESCE(MIN(a.adminnoticeid), -1) FROM AdminNoticeEntity a WHERE a.adminnoticeid > :currentNoticeId")
    Integer findNextNoticeId(@Param("currentNoticeId") Integer currentNoticeId);
}