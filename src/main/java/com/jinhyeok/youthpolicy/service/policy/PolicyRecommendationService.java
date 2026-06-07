package com.jinhyeok.youthpolicy.service.policy;

import com.jinhyeok.youthpolicy.client.policy.PolicyClient;
import com.jinhyeok.youthpolicy.domain.policy.Policy;
import com.jinhyeok.youthpolicy.dto.policy.PolicyRecommendationRequest;
import com.jinhyeok.youthpolicy.dto.policy.PolicyRecommendationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class PolicyRecommendationService {

    private final PolicyClient policyClient;

    public PolicyRecommendationService(PolicyClient policyClient) {
        this.policyClient = policyClient;
    }

    public List<PolicyRecommendationResponse> recommend(PolicyRecommendationRequest request) {
        int age = resolveAge(request);

        List<PolicyMatchResult> matchedPolicies = policyClient.findPolicies()
                .stream()
                .map(policy -> match(policy, request, age))
                .filter(result -> result.matchScore() > 0)
                .sorted(Comparator.comparingInt(PolicyMatchResult::matchScore).reversed())
                .toList();

        if (matchedPolicies.isEmpty()) {
            return policyClient.findPolicies()
                    .stream()
                    .map(this::fallbackRecommendation)
                    .toList();
        }

        return matchedPolicies.stream()
                .map(PolicyMatchResult::response)
                .toList();
    }

    private int resolveAge(PolicyRecommendationRequest request) {
        if (request.getAge() != null) {
            return request.getAge();
        }

        if (request.getBirthDate() == null) {
            throw new IllegalArgumentException("나이 또는 생년월일을 입력해주세요.");
        }

        return Period.between(request.getBirthDate(), LocalDate.now()).getYears();
    }

    private PolicyMatchResult match(Policy policy, PolicyRecommendationRequest request, int age) {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (age >= policy.getMinAge() && age <= policy.getMaxAge()) {
            score += 35;
            reasons.add("나이 조건 일치");
        }

        if (isRegionMatched(policy.getRegion(), request.getRegion())) {
            score += 25;
            reasons.add("거주 지역 조건 일치");
        }

        score += addTagScore(policy, request.getEmploymentStatus(), "취업 상태 조건 일치", reasons);
        score += addTagScore(policy, request.getEducationLevel(), "학력/재학 조건 일치", reasons);
        score += addTagScore(policy, request.getIncomeLevel(), "소득 조건 일치", reasons);
        score += addTagScore(policy, request.getHouseholdType(), "가구 형태 조건 일치", reasons);

        if (reasons.isEmpty()) {
            reasons.add("상세 자격 조건 확인 필요");
        }

        return new PolicyMatchResult(
                score,
                new PolicyRecommendationResponse(
                        policy.getId(),
                        policy.getTitle(),
                        policy.getOrganization(),
                        policy.getSummary(),
                        policy.getRegion(),
                        policy.getMinAge() + "세 ~ " + policy.getMaxAge() + "세",
                        policy.getTargetTags(),
                        policy.getApplicationPeriod(),
                        policy.getSupportType(),
                        policy.getUrl(),
                        Math.min(score, 100),
                        reasons
                )
        );
    }

    private boolean isRegionMatched(String policyRegion, String userRegion) {
        if (isBlank(userRegion)) {
            return "전국".equals(policyRegion);
        }

        return "전국".equals(policyRegion) || normalize(userRegion).contains(normalize(policyRegion));
    }

    private int addTagScore(Policy policy, String value, String reason, List<String> reasons) {
        if (isBlank(value)) {
            return 0;
        }

        String normalizedValue = normalize(value);
        boolean matched = policy.getTargetTags()
                .stream()
                .map(this::normalize)
                .anyMatch(tag -> tag.equals(normalizedValue));

        if (matched) {
            reasons.add(reason);
            return 15;
        }

        return 0;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String normalize(String value) {
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private PolicyRecommendationResponse fallbackRecommendation(Policy policy) {
        return new PolicyRecommendationResponse(
                policy.getId(),
                policy.getTitle(),
                policy.getOrganization(),
                policy.getSummary(),
                policy.getRegion(),
                policy.getMinAge() + "세 ~ " + policy.getMaxAge() + "세",
                policy.getTargetTags(),
                policy.getApplicationPeriod(),
                policy.getSupportType(),
                policy.getUrl(),
                10,
                List.of("입력 조건과 완전히 일치하지 않아도 확인해볼 만한 정책입니다.")
        );
    }

    private record PolicyMatchResult(int matchScore, PolicyRecommendationResponse response) {
    }
}
