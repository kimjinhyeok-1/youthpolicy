package com.jinhyeok.youthpolicy.dto.policy;

import java.time.LocalDate;

public class PolicyRecommendationRequest {

    private Integer age;
    private LocalDate birthDate;
    private String region;
    private String employmentStatus;
    private String educationLevel;
    private String incomeLevel;
    private String householdType;

    public Integer getAge() {
        return age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getRegion() {
        return region;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public String getIncomeLevel() {
        return incomeLevel;
    }

    public String getHouseholdType() {
        return householdType;
    }
}
