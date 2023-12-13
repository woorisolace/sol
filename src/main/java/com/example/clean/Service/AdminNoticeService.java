package com.example.clean.Service;

import com.example.clean.Constant.AdminNoticeRole;
import com.example.clean.DTO.AdminNoticeDTO;
import com.example.clean.Entity.AdminNoticeEntity;
import com.example.clean.Repository.AdminNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class AdminNoticeService {
    @Value("${imgLocation}")
    private String adminUploadLocation;

    @Autowired
    private final AdminNoticeRepository adminNoticeRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final FileService fileService;


    public List<AdminNoticeDTO> search(String admintitle){

        List<AdminNoticeEntity> result;
        //검색어가 없을 경우와 있을 경우에 대해서 처리
        if(admintitle == null){ //검색어가 없는 경우
            result = adminNoticeRepository.findAll(); //모든 내용을 조회
        }else { //검색어가 있는 경우
            result = adminNoticeRepository.findSearch(admintitle); //해당 내용을 조회
        }

        //List Entity를 개별 Entity로 바꾼 후 개별 DTO에 담아서 저장하고
        //다시 List DTO에 개별 DTO 다시 저장
        //메소드명의 전달값과 유형이 틀리면 변환작업
        List<AdminNoticeDTO> lists = new ArrayList<>(Arrays.asList(modelMapper.map(result,AdminNoticeDTO[].class)));

        return lists;
    }

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




    //페이지조회
    public Page<AdminNoticeDTO> findAll(String type, String title, String adminNotice, Pageable page) throws Exception{

        int currentpage = page.getPageNumber() - 1; //실제페이지번호
        int pageLimit = 10; //한페이지에 보여줄 글 갯수


        Pageable pageable = PageRequest.of(currentpage,pageLimit,
            Sort.by(Sort.Direction.DESC,"adminnoticeid"));

        Page<AdminNoticeEntity> adminNoticeEntityPage;

        if("t".equals(type) && title != null ){
            adminNoticeEntityPage = adminNoticeRepository.findAdmintitle(title,pageable);
        }else if("c".equals(adminNotice)){
            adminNoticeEntityPage = adminNoticeRepository.findByNoticeType(AdminNoticeRole.valueOf(adminNotice),pageable);
        }else {
            adminNoticeEntityPage = adminNoticeRepository.findAll(pageable);
        }

        List<AdminNoticeDTO> adminNoticeDTOList = new ArrayList<>();

        for(AdminNoticeEntity entity:adminNoticeEntityPage){
            AdminNoticeDTO adminNoticeDTO = AdminNoticeDTO.builder()
                .adminnoticeid(entity.getAdminnoticeid())
                .admincategory(entity.getAdmincategory())
                .adminNoticeRole(entity.getAdminNoticeRole())
                .admincontent(entity.getAdmincontent())
                .adminwriter(entity.getAdminwriter())
                .admintitle(entity.getAdmintitle())
                .modDate(entity.getModDate())
                .reDate(entity.getReDate())
                .build();

            adminNoticeDTOList.add(adminNoticeDTO);
        }

        return new PageImpl<>(adminNoticeDTOList, pageable, adminNoticeEntityPage.getTotalElements());
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

        adminNoticeRepository.save(adminNoticeEntity);
    }

    //이전버튼
    public Integer findPreviousNoticeId(Integer currentNoticeId) {

        return adminNoticeRepository.findPreviousNoticeId(currentNoticeId);
    }

    //다음버튼
    public Integer findNextNoticeId(Integer currentNoticeId) {

        return adminNoticeRepository.findNextNoticeId(currentNoticeId);
    }


}