package com.example.jpashop.api;

import com.example.jpashop.domain.Member;
import com.example.jpashop.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){ //@RequestBody Joson으로온 body를 Member로 매핑해준다
                                                                                    // 엔티티를 프리젠테이션 계층에서 던지는 데이터를 매핑하면 위험하다 API spec이 entity와 밀접하게 연결되어 있기 때문 즉 DB랑연결?
                                                                                    // 따라서 엔티티랑 매핑하지말고 별도의 DTO를 만들어서 매핑시키는 것이 안전하다
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMember(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id,request.getName());
        Member findMember = memberService.findById(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @GetMapping("/api/v1/members")
    public List<Member> getMembersV1(){
        //이러면 엔티티가 반환되어서 보여주고 싶지 않은 정보도 보여진다. 엔티티에 @JsonIgnore를 추가하면 해당 정보는 반환되지 않기는 한다. but 다른 api에서는 해당 정보는 필요하다면????
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result getMembersV2(){
        //이러면 엔티티가 반환되어서 보여주고 싶지 않은 정보도 보여진다. 엔티티에 @JsonIgnore를 추가하면 해당 정보는 반환되지 않기는 한다. but 다른 api에서는 해당 정보는 필요하다면????
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream().map(m -> new MemberDto(m.getName())).collect(Collectors.toList());
        return new Result(collect.size(),collect);
        //이렇게 Result라는 껍데기로 반환하는 이유는 그냥 collect를 반환하면 jsonArray로 나가기 때문에 데이터 변경의 유연성이 떨어진다.
        //ex [ {...}, {...},...] 보다 { data : [{...}, {...},...] 하면 data외 다른 데이터 추가 가능능
   }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }
    @Data
    private class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    private class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    private class Result<T> {
        private int count;
        private T data;
    }
}
