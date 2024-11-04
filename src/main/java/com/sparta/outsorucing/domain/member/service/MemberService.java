package com.sparta.outsorucing.domain.member.service;

import com.sparta.outsorucing.common.config.JwtUtil;
import com.sparta.outsorucing.common.config.PasswordEncoder;
import com.sparta.outsorucing.common.enums.MemberRole;
import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.common.exception.AuthException;
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
    private final PasswordEncoder passwordEncoder;
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
                memberRole
        );
        memberRepository.save(member);

        return new MemberResponseDto(member.getId(), member.getNickName(), member.getEmail(), memberRole);
    }

//    public MemberResponseDto login(LoginRequestDto loginRequestDto) {
//        Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
//                ()-> new InvalidRequestException("이메일 또는 비밀번호가 잘못되었습니다."));
//
//        if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
//            throw new AuthException("이메일 또는 비밀번호가 잘못되었습니다.");
//        }
//
//        jwtUtil.createToken(member.getId(), member.getEmail(), member.getNickName(), member.getRole());
//
//        return new MemberResponseDto(member.getId(), member.getNickName(), member.getEmail(), member.getRole());
//    }

    public String login1(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
            ()-> new InvalidRequestException("이메일 또는 비밀번호가 잘못되었습니다."));

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new AuthException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        String token =jwtUtil.createToken(member.getId(), member.getNickName(), member.getEmail(), member.getRole());
        System.out.println(token);
        return token;

    }
}
