package com.ruoyi.risk.domain;

import java.math.BigDecimal;

public class LoanScorecard {
    private Long id;
    private Long companyId;
    private String companyName;
    private java.util.Date scoreDate;
    private BigDecimal scoreFinancial;
    private BigDecimal scoreOperation;
    private BigDecimal scoreCredit;
    private BigDecimal scoreCollateral;
    private BigDecimal scoreIndustry;
    private BigDecimal totalScore;
    private String grade;
    private BigDecimal suggestAmount;
    private BigDecimal suggestRate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public java.util.Date getScoreDate() { return scoreDate; }
    public void setScoreDate(java.util.Date scoreDate) { this.scoreDate = scoreDate; }
    public BigDecimal getScoreFinancial() { return scoreFinancial; }
    public void setScoreFinancial(BigDecimal scoreFinancial) { this.scoreFinancial = scoreFinancial; }
    public BigDecimal getScoreOperation() { return scoreOperation; }
    public void setScoreOperation(BigDecimal scoreOperation) { this.scoreOperation = scoreOperation; }
    public BigDecimal getScoreCredit() { return scoreCredit; }
    public void setScoreCredit(BigDecimal scoreCredit) { this.scoreCredit = scoreCredit; }
    public BigDecimal getScoreCollateral() { return scoreCollateral; }
    public void setScoreCollateral(BigDecimal scoreCollateral) { this.scoreCollateral = scoreCollateral; }
    public BigDecimal getScoreIndustry() { return scoreIndustry; }
    public void setScoreIndustry(BigDecimal scoreIndustry) { this.scoreIndustry = scoreIndustry; }
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public BigDecimal getSuggestAmount() { return suggestAmount; }
    public void setSuggestAmount(BigDecimal suggestAmount) { this.suggestAmount = suggestAmount; }
    public BigDecimal getSuggestRate() { return suggestRate; }
    public void setSuggestRate(BigDecimal suggestRate) { this.suggestRate = suggestRate; }
}
