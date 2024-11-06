package com.sparta.outsorucing.domain.member.entity;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sparta.outsorucing.common.enums.MemberRole;
import com.sparta.outsorucing.common.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "유저 고유번호")
    private Long id;

    @Column(name = "nick_name")
    @Comment(value = "이름")
    private String nickName;

    @Column(name = "email")
    @Comment(value = "이메일")
    private String email;

    @Column(name = "password")
    @Comment(value = "비밀번호")
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Setter
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;


    private Long kakaoId;

    @Builder
    public Member(String nickName, String email, String password, MemberRole memberRole) {
        this.nickName = nickName;
        this.email = email;
        this.password = BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
        this.role = memberRole;
        this.status = Status.ACTIVE;
    }

    public Member(String nickName, String email, String password, MemberRole memberRole,
        Long kakaoId) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.role = memberRole;
        this.status = Status.ACTIVE;
        this.kakaoId = kakaoId;
    }

    public Member kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public void deleteMember() {
        this.status = Status.DELETE;
    }

    public void update(Status status) {
        this.status = status;
    }


}