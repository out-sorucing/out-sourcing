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
import com.sparta.outsorucing.domain.member.dto.WithdrawRequestDto;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Member member = Member.builder()
                .nickName(signupRequestDto.getNickName())
                .email(signupRequestDto.getEmail())
                .password(signupRequestDto.getPassword())
                .memberRole(MemberRole.of(signupRequestDto.getMemberRole()))
                .build();

        memberRepository.save(member);

        return new MemberResponseDto(member.getId(), member.getNickName(), member.getEmail(), MemberRole.of(signupRequestDto.getMemberRole()), member.getStatus());
    }

    public MemberResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                ()-> new InvalidRequestException("이메일 또는 비밀번호가 잘못되었습니다."));

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new AuthException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        if(member.getStatus().equals(Status.DELETE)) {
            throw new AuthException("탈퇴한 회원입니다");
        }

        jwtUtil.createToken(member.getId(), member.getEmail(), member.getNickName(), member.getRole(), member.getStatus());

        return new MemberResponseDto(member.getId(), member.getNickName(), member.getEmail(), member.getRole(), member.getStatus());
    }

    @Transactional
    public String withdraw(long memberId, WithdrawRequestDto withdrawRequestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new InvalidRequestException("해당 멤버를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(withdrawRequestDto.getPassword(), member.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호 입니다");
        }

        member.deleteMember();

        return member.getNickName()+"님이 정상적으로 탈퇴되었습니다.";
    }
}