package com.example.jpashop.service;

import com.example.jpashop.domain.Member;
import com.example.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional //기본적으로 JPA는 trasaction 안에서 작동해야한다.
@RequiredArgsConstructor
public class MemberService {

//    @Autowired
    private final MemberRepository memberRepository;

//    public void setMemberRepository(MemberRepository memberRepository) { //테스트할떄 mock개체를 넘겨줄수 있다
//        this.memberRepository = memberRepository;
//    }

    //@RequiredArgsConstructor 어노테이션으로 final인 필드만 가지는 생성자를 lombok에서 생성해줬다
//    public MemberService(MemberRepository memberRepository) {
 //       this.memberRepository = memberRepository;
 //   }

    //회원가입
    public Long join(Member member){
        //중복회원 검증
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findByName(member.getUsername());
        if(!findMember.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    //회원 전체 조회
    @Transactional(readOnly = true) //조회의 경우 성능 최적화를 한다 자세한 내용은 모룸..
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 이름조회
    @Transactional(readOnly = true)
    public List<Member> findByName(String name){
        return memberRepository.findByName(name);
    }

    //회원 ID조회
    @Transactional(readOnly = true)
    public Member findById(Long id){
        return memberRepository.find(id);
    }
}
