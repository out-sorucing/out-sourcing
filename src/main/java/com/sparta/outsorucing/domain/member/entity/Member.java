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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor
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

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public void createMember(String nickName, String email, String password,
        MemberRole memberRole) {
        this.nickName = nickName;
        this.email = email;
        this.password = BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
        this.role = memberRole;
        this.status = Status.ACTIVE;
    }

    public void deleteMember() {
        this.status = Status.DELETE;
    }

    public void update(Status status) {
        this.status = status;
    }
}
