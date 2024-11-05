package com.sparta.outsorucing.domain.member.controller;

import com.sparta.outsorucing.common.config.JwtUtil;
import com.sparta.outsorucing.domain.member.dto.LoginRequestDto;
import com.sparta.outsorucing.domain.member.dto.MemberResponseDto;
import com.sparta.outsorucing.domain.member.dto.SignupRequestDto;
import com.sparta.outsorucing.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/members")
public class AuthMemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<MemberResponseDto> signUp(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.signUp(signupRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,  HttpServletResponse response) {
        MemberResponseDto memberResponseDto = memberService.login(loginRequestDto);

        String token = jwtUtil.createToken(
                memberResponseDto.getMemberId(),
                memberResponseDto.getNickName(),
                memberResponseDto.getEmail(),
                memberResponseDto.getRole()
        );

        System.out.println("token: "+token);

        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        return ResponseEntity.ok(memberResponseDto);
    }
}
