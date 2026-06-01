package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 征信查询记录实体
 */
public class LoanCreditQuery {
    private Long id;
    private Long companyId;
    private String companyName;
    private String queryType;
    private String querySource;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date queryDate;
    private String queryResult;
    private Integer creditScore;
    private BigDecimal debtAmount;
    private Integer overdueCount;
    private Integer queryCount6m;
    private Integer hasBadRecord;
    private Long operatorId;
    private String operatorName;
    private Integer status;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getQueryType() { return queryType; }
    public void setQueryType(String queryType) { this.queryType = queryType; }
    public String getQuerySource() { return querySource; }
    public void setQuerySource(String querySource) { this.querySource = querySource; }
    public java.util.Date getQueryDate() { return queryDate; }
    public void setQueryDate(java.util.Date queryDate) { this.queryDate = queryDate; }
    public String getQueryResult() { return queryResult; }
    public void setQueryResult(String queryResult) { this.queryResult = queryResult; }
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    public BigDecimal getDebtAmount() { return debtAmount; }
    public void setDebtAmount(BigDecimal debtAmount) { this.debtAmount = debtAmount; }
    public Integer getOverdueCount() { return overdueCount; }
    public void setOverdueCount(Integer overdueCount) { this.overdueCount = overdueCount; }
    public Integer getQueryCount6m() { return queryCount6m; }
    public void setQueryCount6m(Integer queryCount6m) { this.queryCount6m = queryCount6m; }
    public Integer getHasBadRecord() { return hasBadRecord; }
    public void setHasBadRecord(Integer hasBadRecord) { this.hasBadRecord = hasBadRecord; }
    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public java.util.Date getCreateTime() { return createTime; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
}
