package com.sparta.outsorucing.common.config;

import com.sparta.outsorucing.common.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import com.sparta.outsorucing.common.exception.AuthException;
import com.sparta.outsorucing.common.annotation.Auth;
import com.sparta.outsorucing.common.enums.MemberRole;
import com.sparta.outsorucing.common.dto.AuthMember;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
        boolean isAuthMemberType = parameter.getParameterType().equals(AuthMember.class);

        if (hasAuthAnnotation != isAuthMemberType) {
            throw new AuthException("@Auth와 AuthMember 타입은 함께 사용되어야 합니다.");
        }

        return hasAuthAnnotation;
    }

    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Long memberId = (Long) request.getAttribute("memberId");
        String nickName = (String) request.getAttribute("nickName");
        String email = (String) request.getAttribute("email");
        MemberRole memberRole = MemberRole.of((String) request.getAttribute("memberRole"));
        Status status = Status.valueOf((String) request.getAttribute("status"));

        return new AuthMember(memberId, nickName, email, memberRole, status);
    }
}
