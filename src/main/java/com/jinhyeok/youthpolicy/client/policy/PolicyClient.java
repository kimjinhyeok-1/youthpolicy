package com.jinhyeok.youthpolicy.client.policy;

import com.jinhyeok.youthpolicy.domain.policy.Policy;

import java.util.List;

public interface PolicyClient {

    List<Policy> findPolicies();
}
