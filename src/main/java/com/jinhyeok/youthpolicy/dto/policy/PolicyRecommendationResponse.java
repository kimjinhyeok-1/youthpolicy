package com.jinhyeok.youthpolicy.dto.policy;

import java.util.List;

public class PolicyRecommendationResponse {

    private final String id;
    private final String title;
    private final String organization;
    private final String summary;
    private final String region;
    private final String ageRange;
    private final List<String> targetTags;
    private final String applicationPeriod;
    private final String supportType;
    private final String url;
    private final int matchScore;
    private final List<String> matchReasons;

    public PolicyRecommendationResponse(String id, String title, String organization, String summary,
                                        String region, String ageRange, List<String> targetTags,
                                        String applicationPeriod, String supportType, String url,
                                        int matchScore, List<String> matchReasons) {
        this.id = id;
        this.title = title;
        this.organization = organization;
        this.summary = summary;
        this.region = region;
        this.ageRange = ageRange;
        this.targetTags = targetTags;
        this.applicationPeriod = applicationPeriod;
        this.supportType = supportType;
        this.url = url;
        this.matchScore = matchScore;
        this.matchReasons = matchReasons;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganization() {
        return organization;
    }

    public String getSummary() {
        return summary;
    }

    public String getRegion() {
        return region;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public List<String> getTargetTags() {
        return targetTags;
    }

    public String getApplicationPeriod() {
        return applicationPeriod;
    }

    public String getSupportType() {
        return supportType;
    }

    public String getUrl() {
        return url;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public List<String> getMatchReasons() {
        return matchReasons;
    }
}
