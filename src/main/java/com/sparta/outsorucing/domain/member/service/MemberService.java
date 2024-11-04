package com.sparta.outsorucing.domain.member.service;

import com.sparta.outsorucing.common.config.JwtUtil;
import com.sparta.outsorucing.common.enums.MemberRole;
import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.member.dto.LoginRequestDto;
import com.sparta.outsorucing.domain.member.dto.MemberResponseDto;
import com.sparta.outsorucing.domain.member.dto.SignupRequestDto;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberResponseDto signUp(SignupRequestDto signupRequestDto) {

        if(memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다");
        }

        MemberRole memberRole = MemberRole.of(signupRequestDto.getMemberRole());

        Member member = new Member();
        member.createMember(
                signupRequestDto.getNickName(),
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                memberRole,
                Status.ACTIVE
        );
        memberRepository.save(member);

        return new MemberResponseDto(member.getId(), member.getNickName(), member.getEmail(), memberRole);
    }

}
