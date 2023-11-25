package com.example.clean.Service;

import com.example.clean.DTO.AdminNoticeDTO;
import com.example.clean.Entity.AdminNoticeEntity;
import com.example.clean.Repository.AdminNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class AdminNoticeService {
    @Value("${imgLocation}")
    private String adminUploadLocation;

    private final AdminNoticeRepository adminNoticeRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final FileService fileService;



    //삭제
    public void remove(Integer id) throws Exception{
        adminNoticeRepository.deleteById(id);
    }

    //삽입
    //(DTO)로 전달받은 내용을 Entity로 변환해서 저장
    public void register(AdminNoticeDTO adminNoticeDTO) throws Exception {
        //변환
        AdminNoticeEntity adminNoticeEntity = modelMapper.map(adminNoticeDTO, AdminNoticeEntity.class);

        adminNoticeRepository.save(adminNoticeEntity);
    }


    //개별조회
    public AdminNoticeDTO detail(Integer Id)throws Exception{
        AdminNoticeEntity adminNoticeEntity = adminNoticeRepository
                .findById(Id)
                .orElseThrow();
        //변환
        AdminNoticeDTO noticeDTO = modelMapper.map(adminNoticeEntity,AdminNoticeDTO.class);

        return noticeDTO;
    }



    //전체조회
    public List<AdminNoticeDTO> findAll() throws Exception{
        List<AdminNoticeEntity> adminNoticeEntityList = adminNoticeRepository.findAll();

        List<AdminNoticeDTO> noticeDTOList = Arrays.asList(modelMapper.map(adminNoticeEntityList, AdminNoticeDTO[].class));

        return noticeDTOList;
    }


    //페이지조회
    public Page<AdminNoticeDTO> list(Pageable page) throws Exception{
        int curPage = page.getPageNumber()-1;
        int pageLimit = 10;

        Pageable pageable = PageRequest.of(curPage,pageLimit,
                Sort.by(Sort.Direction.DESC,"adminnoticeid"));

        Page<AdminNoticeEntity> adminNoticeEntities = adminNoticeRepository.findAll(pageable);

        Page<AdminNoticeDTO> adminNoticeDTOS = adminNoticeEntities.map(data->AdminNoticeDTO.builder()
                .adminnoticeid(data.getAdminnoticeid()).admincategory(data.getAdmincategory())
                .admincontent(data.getAdmincontent()).admintitle(data.getAdmintitle())
                .adminwriter(data.getAdminwriter()).reDate(data.getReDate()).modDate(data.getModDate()).build());

        return adminNoticeDTOS;
    }

    //수정
    public void update(AdminNoticeDTO adminNoticeDTO) throws Exception{
        AdminNoticeEntity adminNoticeEntity = adminNoticeRepository
                .findById(adminNoticeDTO.getAdminnoticeid())
                .orElseThrow();


        adminNoticeDTO.setAdminnoticeid(adminNoticeEntity.getAdminnoticeid());

        adminNoticeEntity.setAdmintitle(adminNoticeDTO.getAdmintitle());
        adminNoticeEntity.setAdminwriter(adminNoticeDTO.getAdminwriter());
        adminNoticeEntity.setAdmincategory(adminNoticeDTO.getAdmincategory());
        adminNoticeEntity.setAdmincontent(adminNoticeDTO.getAdmincontent());
        adminNoticeEntity.setModDate(LocalDateTime.now());


//      AdminNoticeEntity data = modelMapper.map(adminNoticeDTO,AdminNoticeEntity.class);

        adminNoticeRepository.save(adminNoticeEntity);


//        Integer id = adminNoticeDTO.getAdminnoticeid(); //조회에 필요한 변수를 추출
//
//        Optional<AdminNoticeEntity> data = adminNoticeRepository.findById(id); //in(int) -> out(Optional<entity>)
//        //조회한 entity와 작업할 entity
//        AdminNoticeEntity adminNoticeEntity = data.orElseThrow();
//
//        //DTO->Entity 변환(수정할 내용들을 변환)
//        AdminNoticeEntity update = modelMapper.map(adminNoticeDTO,AdminNoticeEntity.class);
//        update.setAdminnoticeid(adminNoticeEntity.getAdminnoticeid()); //조회한 내용을 추가
//
//        adminNoticeRepository.save(update);
    }



}
