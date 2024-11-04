package com.sparta.outsorucing.domain.member.controller;

import com.sparta.outsorucing.domain.member.dto.LoginRequestDto;
import com.sparta.outsorucing.domain.member.dto.MemberResponseDto;
import com.sparta.outsorucing.domain.member.dto.SignupRequestDto;
import com.sparta.outsorucing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/members")
public class AuthMemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.signUp(signupRequestDto));
    }
}
