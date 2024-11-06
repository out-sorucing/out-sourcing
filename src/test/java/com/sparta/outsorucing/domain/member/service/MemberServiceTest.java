package com.sparta.outsorucing.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 - 성공 테스트")
    void Signup(){
        //given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        ReflectionTestUtils.setField(signupRequestDto,"email", "lee@naver.com");
        ReflectionTestUtils.setField(signupRequestDto,"password", "abcd1234");
        ReflectionTestUtils.setField(signupRequestDto,"nickName", "lee");
        ReflectionTestUtils.setField(signupRequestDto,"memberRole", "USER");

        Member member = Member.builder()
                .nickName(signupRequestDto.getNickName())
                .email(signupRequestDto.getEmail())
                .password(signupRequestDto.getPassword())
                .memberRole(MemberRole.USER)
                .build();

        when(memberRepository.existsByEmail(signupRequestDto.getEmail())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        //when
        MemberResponseDto signup = memberService.signUp(signupRequestDto);

        //then
        assertEquals(signup.getNickName(), "lee");
        assertEquals(signup.getEmail(), "lee@naver.com");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 - 실패 테스트(중복된 이메일)")
    void signupWithDuplicateEmail(){
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        ReflectionTestUtils.setField(signupRequestDto,"email", "lee@naver.com");
        ReflectionTestUtils.setField(signupRequestDto,"password", "abcd1234");
        ReflectionTestUtils.setField(signupRequestDto,"nickName", "lee");
        ReflectionTestUtils.setField(signupRequestDto,"memberRole", "USER");

        when(memberRepository.existsByEmail(signupRequestDto.getEmail())).thenReturn(true);

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> memberService.signUp(signupRequestDto));

        //then
        assertEquals(ex.getMessage(), "이미 존재하는 이메일입니다");
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인 - 성공 테스트")
    void login(){
        //given
        Member member = Member.builder()
                .nickName("lee")
                .email("lee@naver.com")
                .password("$2a$10$someEncryptedPassword")
                .memberRole(MemberRole.USER)
                .build();
        ReflectionTestUtils.setField(member,"id", 1L);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        ReflectionTestUtils.setField(loginRequestDto,"email", "lee@naver.com");
        ReflectionTestUtils.setField(loginRequestDto,"password", "abcd1234");

        when(memberRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(anyLong(), anyString(), anyString(), any(MemberRole.class), any(Status.class)))
                .thenReturn("mockedJwtToken");

        //when
        MemberResponseDto response = memberService.login(loginRequestDto);

        //then
        assertEquals(response.getNickName(), "lee");
        assertEquals(response.getEmail(), "lee@naver.com");
        verify(memberRepository, times(1)).findByEmail(loginRequestDto.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequestDto.getPassword(), member.getPassword());
        verify(jwtUtil, times(1)).createToken(anyLong(), anyString(), anyString(), any(MemberRole.class), any(Status.class));
    }

    @Test
    @DisplayName("로그인 - 실패 테스트(탈퇴한 회원)")
    void loginWithDeletedMember() {
        //given
        Member member = Member.builder()
                .nickName("lee")
                .email("lee@naver.com")
                .password("$2a$10$someEncryptedPassword")
                .memberRole(MemberRole.USER)
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(member, "status", Status.DELETE);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        ReflectionTestUtils.setField(loginRequestDto, "email", "lee@naver.com");
        ReflectionTestUtils.setField(loginRequestDto, "password", "wrongPassword12");

        when(memberRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())).thenReturn(true);

        //when
        AuthException ex = assertThrows(AuthException.class, () -> memberService.login(loginRequestDto));

        //then
        assertEquals(ex.getMessage(), "탈퇴한 회원입니다");

    }

    @Test
    @DisplayName("회원 탈퇴")
    void withdraw() {
        //given
        Member member = Member.builder()
                .nickName("lee")
                .email("lee@naver.com")
                .password("$2a$10$someEncryptedPassword")
                .memberRole(MemberRole.USER)
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);

        WithdrawRequestDto withdrawRequestDto = new WithdrawRequestDto();
        ReflectionTestUtils.setField(withdrawRequestDto, "password", "abcd1234");

        when(passwordEncoder.matches(withdrawRequestDto.getPassword(), member.getPassword())).thenReturn(true);

        //when
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        String response = memberService.withdraw(member.getId(), withdrawRequestDto);

        //then
        assertTrue(response.equals(member.getNickName()+"님이 정상적으로 탈퇴되었습니다."));
    }

    @Test
    @DisplayName("회원 탈퇴 - 실패 테스트(잘못된 비밀번호)")
    void withdrawWithWrongPassword() {
        //given
        Member member = Member.builder()
                .nickName("lee")
                .email("lee@naver.com")
                .password("$2a$10$someEncryptedPassword")
                .memberRole(MemberRole.USER)
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);

        WithdrawRequestDto withdrawRequestDto = new WithdrawRequestDto();
        ReflectionTestUtils.setField(withdrawRequestDto, "password", "abcd1234");

        when(passwordEncoder.matches(withdrawRequestDto.getPassword(), member.getPassword())).thenReturn(false);

        //when
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> memberService.withdraw(member.getId(), withdrawRequestDto));

        //then
        assertEquals(ex.getMessage(), "잘못된 비밀번호 입니다");
    }
}