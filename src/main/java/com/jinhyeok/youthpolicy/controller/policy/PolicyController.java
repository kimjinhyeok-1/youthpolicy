package com.jinhyeok.youthpolicy.controller.policy;

import com.jinhyeok.youthpolicy.dto.policy.PolicyRecommendationRequest;
import com.jinhyeok.youthpolicy.dto.policy.PolicyRecommendationResponse;
import com.jinhyeok.youthpolicy.service.policy.PolicyRecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyRecommendationService policyRecommendationService;

    public PolicyController(PolicyRecommendationService policyRecommendationService) {
        this.policyRecommendationService = policyRecommendationService;
    }

    @PostMapping("/recommendations")
    public List<PolicyRecommendationResponse> recommend(@RequestBody PolicyRecommendationRequest request) {
        return policyRecommendationService.recommend(request);
    }
}
