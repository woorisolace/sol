package com.example.clean.Repository;

import com.example.clean.Entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {
//    @Query(value = "SELECT n FROM notice n WHERE n.adminidnoticeid = :adminidnoticeid",
//    nativeQuery = true)
//    List<NoticeEntity> findByAdminid(Integer adminidnoticeid);
}
