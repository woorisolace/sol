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

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;

    public void saveMember(MemberDTO memberDTO){
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

    private void validateDuplicateMember(UserEntity userEntity){
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

}
