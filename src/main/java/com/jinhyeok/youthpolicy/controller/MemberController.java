package com.jinhyeok.youthpolicy.controller;

import com.jinhyeok.youthpolicy.dto.member.MemberCreateRequest;
import com.jinhyeok.youthpolicy.dto.member.MemberResponse;
import com.jinhyeok.youthpolicy.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public MemberResponse createMember(@RequestBody MemberCreateRequest request) {
        return memberService.join(request);
    }

    @GetMapping
    public List<MemberResponse> members() {
        return memberService.findMembers();
    }
}