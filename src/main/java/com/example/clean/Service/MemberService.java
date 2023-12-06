package com.example.clean.Service;

import com.example.clean.Constant.Role;
import com.example.clean.DTO.MemberDTO;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;

    //멤버 등록
    public void saveMember(MemberDTO memberDTO) throws Exception {
        //DTO와 Entity가 매치가 되지 않아 modelmapper는 사용 못함
        //MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class);
        String password = passwordEncoder.encode(memberDTO.getPassword());
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(memberDTO.getEmail());
        userEntity.setNickname(memberDTO.getName());
        userEntity.setPassword(password);
        userEntity.setTel(memberDTO.getTel());
        userEntity.setSample6_postcode(memberDTO.getSample6_postcode());
        userEntity.setSample6_address(memberDTO.getSample6_address());
        userEntity.setSample6_detailAddress(memberDTO.getSample6_detailAddress());
        userEntity.setSample6_extraAddress(memberDTO.getSample6_extraAddress());
        userEntity.setRole(Role.USER);

        validateDuplicateMember(userEntity);
        memberRepository.save(userEntity);
    }


    private void validateDuplicateMember(UserEntity userEntity) throws Exception {
        UserEntity findMember = memberRepository.findByEmail(userEntity.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = memberRepository.findByEmail(email);

        if(userEntity == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
            .username(userEntity.getEmail())
            .password(userEntity.getPassword())
            .roles(userEntity.getRole().toString())
            .build();
    }


    //회원정보 상세보기
    public MemberDTO myinfoDetail(Principal principal) throws Exception {
        // Principal을 이용하여 현재 로그인한 사용자의 이메일을 가져옵니다.
        String userEmail = principal.getName();

        // 이메일을 이용하여 회원 정보를 조회합니다.
        UserEntity userEntity = memberRepository.findByEmail(userEmail);

        // 회원 정보를 DTO로 변환합니다.
        return convertToDTO(userEntity);
    }

    private MemberDTO convertToDTO(UserEntity userEntity) throws Exception {
        // 엔티티의 필드 값을 DTO에 복사하는 로직 작성
        MemberDTO memberDTO = new MemberDTO();
        if (userEntity != null) {
            memberDTO.setId(userEntity.getId());
            memberDTO.setEmail(userEntity.getEmail());
            memberDTO.setName(userEntity.getNickname());
            memberDTO.setPassword(userEntity.getPassword());
            memberDTO.setTel(userEntity.getTel());
            memberDTO.setSample6_postcode(userEntity.getSample6_postcode());
            memberDTO.setSample6_address(userEntity.getSample6_address());
            memberDTO.setSample6_detailAddress(userEntity.getSample6_detailAddress());
            memberDTO.setSample6_extraAddress(userEntity.getSample6_extraAddress());
            memberDTO.setCreateDate(userEntity.getReDate());
        }

        return memberDTO;
    }


    // 회원정보 수정
    public MemberDTO myinfoUpdate(MemberDTO updatedMemberDTO, Principal principal) throws Exception  {
        // Principal을 이용하여 현재 로그인한 사용자의 이메일을 가져옵니다.
        String userEmail = principal.getName();

        // 이메일을 이용하여 회원 정보를 조회합니다.
        UserEntity userEntity = memberRepository.findByEmail(userEmail);

        // 수정할 필드를 업데이트합니다.
        if (userEntity != null) {
            // 이메일은 변경할 수 없다고 가정하고, 변경이 필요하다면 예외처리 또는 다른 방식으로 처리
            // 패스워드 변경이 필요한 경우에는 적절한 암호화 로직 적용
            userEntity.setId(updatedMemberDTO.getId());     //회원 정보 수정에서는 ID를 변경하는 경우가 없으므로 제거 가능
            userEntity.setNickname(updatedMemberDTO.getName());
            userEntity.setTel(updatedMemberDTO.getTel());
            userEntity.setSample6_postcode(updatedMemberDTO.getSample6_postcode());
            userEntity.setSample6_address(updatedMemberDTO.getSample6_address());
            userEntity.setSample6_detailAddress(updatedMemberDTO.getSample6_detailAddress());
            userEntity.setSample6_extraAddress(updatedMemberDTO.getSample6_extraAddress());

            // 변경된 정보를 저장합니다.
            memberRepository.save(userEntity);
        }

        // 수정된 회원 정보를 다시 DTO로 변환하여 반환합니다.
        return convertToDTO(userEntity);
    }


    // 이메일로 회원 정보 찾기
    public MemberDTO find(String email) throws Exception {
        // 이메일을 사용하여 회원 정보를 조회
        UserEntity userEntity = memberRepository.findByEmail(email);

        // 조회된 회원 정보를 DTO로 변환
        MemberDTO memberDTO = modelMapper.map(userEntity, MemberDTO.class);

        return memberDTO;
    }

}