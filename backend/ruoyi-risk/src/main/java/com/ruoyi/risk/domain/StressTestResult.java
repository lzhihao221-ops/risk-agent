package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

public class StressTestResult {
    private Long resultId;
    private Long scenarioId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testDate;
    private String portfolioType;
    private BigDecimal baselineLoss;
    private BigDecimal stressedLoss;
    private BigDecimal lossIncrease;
    private BigDecimal lossIncreaseRate;
    private BigDecimal capitalImpact;
    private BigDecimal capitalRatioAfter;
    private String passFail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String scenarioName;

    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }
    public Long getScenarioId() { return scenarioId; }
    public void setScenarioId(Long scenarioId) { this.scenarioId = scenarioId; }
    public Date getTestDate() { return testDate; }
    public void setTestDate(Date testDate) { this.testDate = testDate; }
    public String getPortfolioType() { return portfolioType; }
    public void setPortfolioType(String portfolioType) { this.portfolioType = portfolioType; }
    public BigDecimal getBaselineLoss() { return baselineLoss; }
    public void setBaselineLoss(BigDecimal baselineLoss) { this.baselineLoss = baselineLoss; }
    public BigDecimal getStressedLoss() { return stressedLoss; }
    public void setStressedLoss(BigDecimal stressedLoss) { this.stressedLoss = stressedLoss; }
    public BigDecimal getLossIncrease() { return lossIncrease; }
    public void setLossIncrease(BigDecimal lossIncrease) { this.lossIncrease = lossIncrease; }
    public BigDecimal getLossIncreaseRate() { return lossIncreaseRate; }
    public void setLossIncreaseRate(BigDecimal lossIncreaseRate) { this.lossIncreaseRate = lossIncreaseRate; }
    public BigDecimal getCapitalImpact() { return capitalImpact; }
    public void setCapitalImpact(BigDecimal capitalImpact) { this.capitalImpact = capitalImpact; }
    public BigDecimal getCapitalRatioAfter() { return capitalRatioAfter; }
    public void setCapitalRatioAfter(BigDecimal capitalRatioAfter) { this.capitalRatioAfter = capitalRatioAfter; }
    public String getPassFail() { return passFail; }
    public void setPassFail(String passFail) { this.passFail = passFail; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }
}
