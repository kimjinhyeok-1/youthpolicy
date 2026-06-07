package com.jinhyeok.youthpolicy.service;

import com.jinhyeok.youthpolicy.domain.Member;
import com.jinhyeok.youthpolicy.dto.member.MemberCreateRequest;
import com.jinhyeok.youthpolicy.dto.member.MemberResponse;
import com.jinhyeok.youthpolicy.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // [추가됨] 암호화 도구
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    // ⭐️ [코드 설명 1] Config에서 만든 암호화 기계를 주입받기 위해 선언합니다.
    private final PasswordEncoder passwordEncoder;

    // ⭐️ [코드 설명 2] 기존 생성자에 passwordEncoder를 추가해서 스프링이 자동으로 꽂아주게 만듭니다.
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponse join(MemberCreateRequest request) {
        // 기존 중복 검사 로직 유지
        validateDuplicateLoginId(request.getLoginId());

        // ⭐️ [코드 설명 3] 핵심 수정: 유저가 입력한 비밀번호("1234")를 복잡한 암호문으로 변환합니다.
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = new Member();
        member.setLoginId(request.getLoginId());
        // ⭐️ [코드 설명 4] 평문 대신 암호화된 비밀번호를 엔티티에 세팅합니다.
        member.setPassword(encodedPassword);
        member.setName(request.getName());

        Member savedMember = memberRepository.save(member);

        return new MemberResponse(
                savedMember.getId(),
                savedMember.getLoginId(),
                savedMember.getName()
        );
    }

    private void validateDuplicateLoginId(String loginId) {
        memberRepository.findByLoginId(loginId)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    public List<MemberResponse> findMembers() {
        return memberRepository.findAll()
                .stream()
                .map(member -> new MemberResponse(
                        member.getId(),
                        member.getLoginId(),
                        member.getName()
                ))
                .collect(Collectors.toList());
    }

    // 🚀 [코드 설명 5] 새로 추가된 로그인 로직! Controller가 이 메서드를 호출하게 됩니다.
    public Member login(String loginId, String rawPassword) {
        // 1. 아이디로 유저 찾기 (Optional 처리를 orElseThrow로 깔끔하게 처리)
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // ⭐️ [코드 설명 6] 비밀번호 일치 여부 확인 (평문 vs 암호문)
        // matches(입력한 비번, DB에 저장된 비번) -> 일치하지 않으면 에러 발생
        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member; // 로그인 성공 시 회원 객체를 반환해서 Controller가 세션에 ID를 담을 수 있게 합니다.
    }
}