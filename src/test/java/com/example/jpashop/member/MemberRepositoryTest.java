package com.example.jpashop.member;

import com.example.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

     @Autowired
     MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    void saveMember() {
        //given
        Member member = new Member();
        member.setUsername("memeber1");
        
        //when
        Long save = memberRepository.save(member);
        Member member1 = memberRepository.find(save);
        //then
        Assertions.assertThat(member1.getId()).isEqualTo(member.getId());
        
    }

    @Test
    void find() {
    }
}