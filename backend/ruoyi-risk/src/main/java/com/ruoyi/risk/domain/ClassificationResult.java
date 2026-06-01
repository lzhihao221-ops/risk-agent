package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

public class ClassificationResult {
    private Long resultId;
    private Long loanId;
    private Long companyId;
    private String companyName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date classifyDate;
    private String fiveLevel;
    private String previousLevel;
    private Integer overdueDays;
    private BigDecimal outstandingBalance;
    private String triggerRule;
    private BigDecimal score;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Date getClassifyDate() { return classifyDate; }
    public void setClassifyDate(Date classifyDate) { this.classifyDate = classifyDate; }
    public String getFiveLevel() { return fiveLevel; }
    public void setFiveLevel(String fiveLevel) { this.fiveLevel = fiveLevel; }
    public String getPreviousLevel() { return previousLevel; }
    public void setPreviousLevel(String previousLevel) { this.previousLevel = previousLevel; }
    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }
    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(BigDecimal outstandingBalance) { this.outstandingBalance = outstandingBalance; }
    public String getTriggerRule() { return triggerRule; }
    public void setTriggerRule(String triggerRule) { this.triggerRule = triggerRule; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
