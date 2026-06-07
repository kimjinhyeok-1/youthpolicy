package com.jinhyeok.youthpolicy.domain.policy;

import java.util.List;

public class Policy {

    private final String id;
    private final String title;
    private final String organization;
    private final String summary;
    private final String region;
    private final int minAge;
    private final int maxAge;
    private final List<String> targetTags;
    private final String applicationPeriod;
    private final String supportType;
    private final String url;

    public Policy(String id, String title, String organization, String summary, String region,
                  int minAge, int maxAge, List<String> targetTags, String applicationPeriod,
                  String supportType, String url) {
        this.id = id;
        this.title = title;
        this.organization = organization;
        this.summary = summary;
        this.region = region;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.targetTags = targetTags;
        this.applicationPeriod = applicationPeriod;
        this.supportType = supportType;
        this.url = url;
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

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
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
}
