package com.jinhyeok.youthpolicy.dto.member;

public class MemberResponse {

    private Long id;
    private String loginId;
    private String name;

    public MemberResponse(Long id, String loginId, String name) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getName() {
        return name;
    }
}