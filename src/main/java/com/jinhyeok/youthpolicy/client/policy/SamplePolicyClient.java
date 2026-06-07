package com.jinhyeok.youthpolicy.client.policy;

import com.jinhyeok.youthpolicy.domain.policy.Policy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SamplePolicyClient implements PolicyClient {

    @Override
    public List<Policy> findPolicies() {
        return List.of(
                new Policy(
                        "YP-001",
                        "청년 월세 한시 특별지원",
                        "국토교통부",
                        "주거비 부담이 큰 청년에게 월세 일부를 지원합니다.",
                        "전국",
                        19,
                        34,
                        List.of("LOW_INCOME", "SINGLE_HOUSEHOLD", "HOUSING"),
                        "상시 또는 지자체 공고 기간",
                        "현금 지원",
                        "https://www.bokjiro.go.kr"
                ),
                new Policy(
                        "YP-002",
                        "국민취업지원제도",
                        "고용노동부",
                        "취업을 준비하는 청년에게 상담, 직업훈련, 구직촉진수당을 제공합니다.",
                        "전국",
                        15,
                        34,
                        List.of("JOB_SEEKER", "LOW_INCOME", "EMPLOYMENT"),
                        "상시",
                        "취업 지원",
                        "https://www.kua.go.kr"
                ),
                new Policy(
                        "YP-003",
                        "청년도약계좌",
                        "금융위원회",
                        "중장기 자산 형성을 돕기 위해 정부기여금과 비과세 혜택을 제공합니다.",
                        "전국",
                        19,
                        34,
                        List.of("EMPLOYED", "LOW_INCOME", "ASSET"),
                        "금융기관별 신청 기간",
                        "자산 형성",
                        "https://ylaccount.kinfa.or.kr"
                ),
                new Policy(
                        "YP-004",
                        "서울 청년수당",
                        "서울특별시",
                        "서울 거주 미취업 청년에게 활동지원금과 진로 프로그램을 지원합니다.",
                        "서울",
                        19,
                        34,
                        List.of("JOB_SEEKER", "SEOUL", "EMPLOYMENT"),
                        "서울시 공고 확인",
                        "활동 지원금",
                        "https://youth.seoul.go.kr"
                ),
                new Policy(
                        "YP-005",
                        "경기도 청년기본소득",
                        "경기도",
                        "경기도 거주 청년에게 분기별 지역화폐를 지급합니다.",
                        "경기",
                        24,
                        24,
                        List.of("GYEONGGI", "LOCAL_RESIDENT", "INCOME"),
                        "분기별 접수",
                        "지역화폐",
                        "https://apply.jobaba.net"
                ),
                new Policy(
                        "YP-006",
                        "대학생 국가장학금",
                        "한국장학재단",
                        "소득 구간과 학적 요건에 따라 등록금 부담을 줄여주는 장학금입니다.",
                        "전국",
                        18,
                        29,
                        List.of("STUDENT", "LOW_INCOME", "EDUCATION"),
                        "학기별 신청",
                        "교육비 지원",
                        "https://www.kosaf.go.kr"
                )
        );
    }
}
