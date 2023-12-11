package com.example.jpashop.service;

import com.example.jpashop.domain.Member;
import com.example.jpashop.repository.MemberRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    //@Rollback(false)
    public void createMember() throws Exception{

        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member); //insert쿼리가 없는건 @Transactional 이 Test에 있으면 기본적으로 Rollback이 기본이다

        Assertions.assertEquals(member, memberRepository.find(savedId));
    }

    @Test
    public void duplicatedName() throws Exception {
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        memberService.join(member1);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });

        Assertions.fail("예외발생 여기까지 코드가 실행되면 테스트 필해");

    }

}