package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 授信审批流程实体
 */
public class LoanCreditApproval {
    private Long id;
    private Long appId;
    private String appNo;
    private Long companyId;
    private String companyName;
    private BigDecimal creditLine;
    private Integer creditPeriod;
    private BigDecimal creditRate;
    private String creditCondition;
    private Integer approvalStatus;
    private Integer approveLevel;
    private String approveUser;
    private String approveOpinion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date approveTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date expireDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAppId() { return appId; }
    public void setAppId(Long appId) { this.appId = appId; }
    public String getAppNo() { return appNo; }
    public void setAppNo(String appNo) { this.appNo = appNo; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public BigDecimal getCreditLine() { return creditLine; }
    public void setCreditLine(BigDecimal creditLine) { this.creditLine = creditLine; }
    public Integer getCreditPeriod() { return creditPeriod; }
    public void setCreditPeriod(Integer creditPeriod) { this.creditPeriod = creditPeriod; }
    public BigDecimal getCreditRate() { return creditRate; }
    public void setCreditRate(BigDecimal creditRate) { this.creditRate = creditRate; }
    public String getCreditCondition() { return creditCondition; }
    public void setCreditCondition(String creditCondition) { this.creditCondition = creditCondition; }
    public Integer getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(Integer approvalStatus) { this.approvalStatus = approvalStatus; }
    public Integer getApproveLevel() { return approveLevel; }
    public void setApproveLevel(Integer approveLevel) { this.approveLevel = approveLevel; }
    public String getApproveUser() { return approveUser; }
    public void setApproveUser(String approveUser) { this.approveUser = approveUser; }
    public String getApproveOpinion() { return approveOpinion; }
    public void setApproveOpinion(String approveOpinion) { this.approveOpinion = approveOpinion; }
    public java.util.Date getApproveTime() { return approveTime; }
    public void setApproveTime(java.util.Date approveTime) { this.approveTime = approveTime; }
    public java.util.Date getExpireDate() { return expireDate; }
    public void setExpireDate(java.util.Date expireDate) { this.expireDate = expireDate; }
    public java.util.Date getCreateTime() { return createTime; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
    public java.util.Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(java.util.Date updateTime) { this.updateTime = updateTime; }
}
