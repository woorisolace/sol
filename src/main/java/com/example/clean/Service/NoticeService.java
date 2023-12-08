package com.example.clean.Service;

import com.example.clean.DTO.NoticeDTO;
import com.example.clean.Entity.AdminNoticeEntity;
import com.example.clean.Entity.NoticeEntity;
import com.example.clean.Repository.AdminNoticeRepository;
import com.example.clean.Repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

//    private final NoticeRepository noticeRepository;
//    private final AdminNoticeRepository adminNoticeRepository;
//    private final ModelMapper modelMapper = new ModelMapper();
//
//    public void create(int id, NoticeDTO noticeDTO) throws Exception{
//
//        Optional<AdminNoticeEntity> data = adminNoticeRepository.findById(id);
//        AdminNoticeEntity adminNoticeEntity = data.orElseThrow();
//
//        NoticeEntity noticeEntity = modelMapper.map(noticeDTO, NoticeEntity.class);
//
//        noticeEntity.setAdminNoticeEntity(adminNoticeEntity);
//
//        noticeRepository.save(noticeEntity);
//    }

}
