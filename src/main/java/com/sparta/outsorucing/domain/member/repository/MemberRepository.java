package com.sparta.outsorucing.domain.member.repository;

import com.sparta.outsorucing.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

}
