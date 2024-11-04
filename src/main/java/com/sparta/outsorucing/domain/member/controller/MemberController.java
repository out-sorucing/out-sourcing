package com.sparta.outsorucing.domain.member.controller;

import com.sparta.outsorucing.common.annotation.Auth;
import com.sparta.outsorucing.common.dto.AuthMember;
import com.sparta.outsorucing.domain.member.dto.WithdrawRequestDto;
import com.sparta.outsorucing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @DeleteMapping
    public String withdraw(@Auth AuthMember authMember, @RequestBody WithdrawRequestDto withdrawRequestDto) {
        return memberService.withdraw(authMember.getId(), withdrawRequestDto);
    }
}
