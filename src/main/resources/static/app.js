const policyForm = document.getElementById("policyForm");
const searchBtn = document.getElementById("searchBtn");
const formMessage = document.getElementById("formMessage");
const resultSummary = document.getElementById("resultSummary");
const policyResults = document.getElementById("policyResults");

const samplePolicies = [
    {
        id: "YP-001",
        title: "청년 월세 한시 특별지원",
        organization: "국토교통부",
        summary: "주거비 부담이 큰 청년에게 월세 일부를 지원합니다.",
        region: "전국",
        minAge: 19,
        maxAge: 34,
        targetTags: ["LOW_INCOME", "SINGLE_HOUSEHOLD", "HOUSING"],
        applicationPeriod: "상시 또는 지자체 공고 기간",
        supportType: "현금 지원",
        url: "https://www.bokjiro.go.kr"
    },
    {
        id: "YP-002",
        title: "국민취업지원제도",
        organization: "고용노동부",
        summary: "취업을 준비하는 청년에게 상담, 직업훈련, 구직촉진수당을 제공합니다.",
        region: "전국",
        minAge: 15,
        maxAge: 34,
        targetTags: ["JOB_SEEKER", "LOW_INCOME", "EMPLOYMENT"],
        applicationPeriod: "상시",
        supportType: "취업 지원",
        url: "https://www.kua.go.kr"
    },
    {
        id: "YP-003",
        title: "청년도약계좌",
        organization: "금융위원회",
        summary: "중장기 자산 형성을 돕기 위해 정부기여금과 비과세 혜택을 제공합니다.",
        region: "전국",
        minAge: 19,
        maxAge: 34,
        targetTags: ["EMPLOYED", "LOW_INCOME", "ASSET"],
        applicationPeriod: "금융기관별 신청 기간",
        supportType: "자산 형성",
        url: "https://ylaccount.kinfa.or.kr"
    },
    {
        id: "YP-004",
        title: "서울 청년수당",
        organization: "서울특별시",
        summary: "서울 거주 미취업 청년에게 활동지원금과 진로 프로그램을 지원합니다.",
        region: "서울",
        minAge: 19,
        maxAge: 34,
        targetTags: ["JOB_SEEKER", "SEOUL", "EMPLOYMENT"],
        applicationPeriod: "서울시 공고 확인",
        supportType: "활동 지원금",
        url: "https://youth.seoul.go.kr"
    },
    {
        id: "YP-005",
        title: "경기도 청년기본소득",
        organization: "경기도",
        summary: "경기도 거주 청년에게 분기별 지역화폐를 지급합니다.",
        region: "경기",
        minAge: 24,
        maxAge: 24,
        targetTags: ["GYEONGGI", "LOCAL_RESIDENT", "INCOME"],
        applicationPeriod: "분기별 접수",
        supportType: "지역화폐",
        url: "https://apply.jobaba.net"
    },
    {
        id: "YP-006",
        title: "대학생 국가장학금",
        organization: "한국장학재단",
        summary: "소득 구간과 학적 요건에 따라 등록금 부담을 줄여주는 장학금입니다.",
        region: "전국",
        minAge: 18,
        maxAge: 29,
        targetTags: ["STUDENT", "LOW_INCOME", "EDUCATION"],
        applicationPeriod: "학기별 신청",
        supportType: "교육비 지원",
        url: "https://www.kosaf.go.kr"
    }
];

policyForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const request = {
        birthDate: valueOf("birthDateInput"),
        age: numberValueOf("ageInput"),
        region: valueOf("regionInput"),
        employmentStatus: valueOf("employmentInput"),
        educationLevel: valueOf("educationInput"),
        incomeLevel: valueOf("incomeInput"),
        householdType: valueOf("householdInput")
    };

    if (!request.birthDate && !request.age) {
        showMessage("생년월일 또는 나이를 입력해주세요.", true);
        return;
    }

    setLoading(true);
    showMessage("정책을 찾는 중입니다.", false);

    try {
        const data = await loadRecommendations(request);

        renderPolicies(data);
        showMessage("추천 결과를 불러왔습니다.", false);
    } catch (error) {
        policyResults.className = "policy-results empty";
        policyResults.innerHTML = "<p>추천 결과를 불러오는 중 문제가 발생했습니다.</p>";
        resultSummary.textContent = "잠시 후 다시 시도해주세요.";
        showMessage(error.message, true);
    } finally {
        setLoading(false);
    }
});

async function loadRecommendations(request) {
    if (window.location.protocol === "file:") {
        return recommendLocally(request);
    }

    try {
        const response = await fetch("/api/policies/recommendations", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || "추천 결과를 불러오지 못했습니다.");
        }

        return data;
    } catch (error) {
        console.warn("백엔드 API 호출 실패, 브라우저 샘플 추천으로 전환합니다.", error);
        return recommendLocally(request);
    }
}

function valueOf(id) {
    return document.getElementById(id).value.trim() || null;
}

function numberValueOf(id) {
    const value = valueOf(id);
    return value ? Number(value) : null;
}

function setLoading(isLoading) {
    searchBtn.disabled = isLoading;
    searchBtn.textContent = isLoading ? "검색 중..." : "맞춤 정책 찾기";
}

function showMessage(message, isError) {
    formMessage.textContent = message;
    formMessage.classList.toggle("error", isError);
}

function renderPolicies(policies) {
    policyResults.className = "policy-results";

    if (policies.length === 0) {
        policyResults.classList.add("empty");
        policyResults.innerHTML = `
            <div class="empty-state">
                <strong>추천 결과 없음</strong>
                <p>조건을 조금 넓혀 다시 검색해보세요.</p>
            </div>
        `;
        resultSummary.textContent = "추천 가능한 정책 0건";
        return;
    }

    resultSummary.textContent = `추천 가능한 정책 ${policies.length}건`;
    policyResults.innerHTML = policies.map(policyTemplate).join("");
}

function recommendLocally(request) {
    const age = request.age || ageFromBirthDate(request.birthDate);

    const matched = samplePolicies
        .map(policy => matchPolicy(policy, request, age))
        .filter(policy => policy.matchScore > 0)
        .sort((a, b) => b.matchScore - a.matchScore);

    if (matched.length > 0) {
        return matched;
    }

    return samplePolicies.map(policy => toResponse(policy, 10, ["입력 조건과 완전히 일치하지 않아도 확인해볼 만한 정책입니다."]));
}

function ageFromBirthDate(birthDate) {
    if (!birthDate) {
        return null;
    }

    const today = new Date();
    const birthday = new Date(birthDate);
    let age = today.getFullYear() - birthday.getFullYear();
    const monthDiff = today.getMonth() - birthday.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthday.getDate())) {
        age -= 1;
    }

    return age;
}

function matchPolicy(policy, request, age) {
    let score = 0;
    const reasons = [];

    if (age !== null && age >= policy.minAge && age <= policy.maxAge) {
        score += 35;
        reasons.push("나이 조건 일치");
    }

    if (isRegionMatched(policy.region, request.region)) {
        score += 25;
        reasons.push("거주 지역 조건 일치");
    }

    score += addTagScore(policy, request.employmentStatus, "취업 상태 조건 일치", reasons);
    score += addTagScore(policy, request.educationLevel, "학력/재학 조건 일치", reasons);
    score += addTagScore(policy, request.incomeLevel, "소득 조건 일치", reasons);
    score += addTagScore(policy, request.householdType, "가구 형태 조건 일치", reasons);

    return toResponse(policy, Math.min(score, 100), reasons.length ? reasons : ["상세 자격 조건 확인 필요"]);
}

function toResponse(policy, matchScore, matchReasons) {
    return {
        id: policy.id,
        title: policy.title,
        organization: policy.organization,
        summary: policy.summary,
        region: policy.region,
        ageRange: `${policy.minAge}세 ~ ${policy.maxAge}세`,
        targetTags: policy.targetTags,
        applicationPeriod: policy.applicationPeriod,
        supportType: policy.supportType,
        url: policy.url,
        matchScore,
        matchReasons
    };
}

function isRegionMatched(policyRegion, userRegion) {
    if (!userRegion) {
        return policyRegion === "전국";
    }

    return policyRegion === "전국" || userRegion.includes(policyRegion);
}

function addTagScore(policy, value, reason, reasons) {
    if (!value || !policy.targetTags.includes(value)) {
        return 0;
    }

    reasons.push(reason);
    return 15;
}

function policyTemplate(policy) {
    const tags = policy.targetTags
        .map(tag => `<span class="tag">${tagLabel(tag)}</span>`)
        .join("");

    const reasons = policy.matchReasons
        .map(reason => `<li>${reason}</li>`)
        .join("");

    return `
        <article class="policy-card">
            <div class="policy-topline">
                <span>${policy.organization}</span>
                <strong style="--score: ${policy.matchScore}%">${policy.matchScore}%</strong>
            </div>
            <h3>${policy.title}</h3>
            <p>${policy.summary}</p>
            <dl class="policy-meta">
                <div>
                    <dt>지역</dt>
                    <dd>${policy.region}</dd>
                </div>
                <div>
                    <dt>연령</dt>
                    <dd>${policy.ageRange}</dd>
                </div>
                <div>
                    <dt>지원</dt>
                    <dd>${policy.supportType}</dd>
                </div>
                <div>
                    <dt>신청</dt>
                    <dd>${policy.applicationPeriod}</dd>
                </div>
            </dl>
            <div class="tag-row">${tags}</div>
            <div class="reason-box">
                <span>추천 이유</span>
                <ul>${reasons}</ul>
            </div>
            <a href="${policy.url}" target="_blank" rel="noopener noreferrer">공고 확인하기</a>
        </article>
    `;
}

function tagLabel(tag) {
    const labels = {
        LOW_INCOME: "소득요건",
        SINGLE_HOUSEHOLD: "1인 가구",
        HOUSING: "주거",
        JOB_SEEKER: "구직",
        EMPLOYMENT: "취업",
        EMPLOYED: "재직",
        ASSET: "자산형성",
        SEOUL: "서울",
        GYEONGGI: "경기",
        LOCAL_RESIDENT: "거주요건",
        INCOME: "소득지원",
        STUDENT: "학생",
        EDUCATION: "교육"
    };

    return labels[tag] || tag;
}
