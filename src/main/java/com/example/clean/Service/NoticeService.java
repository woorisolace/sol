package com.example.clean.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
