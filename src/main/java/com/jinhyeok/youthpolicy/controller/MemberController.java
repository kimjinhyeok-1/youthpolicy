package com.jinhyeok.youthpolicy.controller;

import com.jinhyeok.youthpolicy.domain.Member;
import com.jinhyeok.youthpolicy.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/members/new")
    public String createMember(@RequestParam("name") String name) {
        Member member = new Member();
        member.setName(name);

        memberService.join(member);
        return "ok";
    }

    @GetMapping("/members")
    public List<Member> members() {
        return memberService.findMembers();
    }
}