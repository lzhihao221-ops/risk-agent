package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 贷款申请实体
 */
public class LoanApplication {
    private Long id;
    private String appNo;
    private Long companyId;
    private String companyName;
    private String productType;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private String loanPurpose;
    private BigDecimal interestRate;
    private String repaymentType;
    private String guaranteeType;
    private String collateralDesc;
    private Long applicantId;
    private String applicantName;
    private Integer appStatus;
    private Integer riskScore;
    private Integer riskLevel;
    private String rejectReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date submitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date approveTime;
    private String remark;
    private String createBy;
    private java.util.Date createTime;
    private String updateBy;
    private java.util.Date updateTime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAppNo() { return appNo; }
    public void setAppNo(String appNo) { this.appNo = appNo; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
    public Integer getLoanTerm() { return loanTerm; }
    public void setLoanTerm(Integer loanTerm) { this.loanTerm = loanTerm; }
    public String getLoanPurpose() { return loanPurpose; }
    public void setLoanPurpose(String loanPurpose) { this.loanPurpose = loanPurpose; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getRepaymentType() { return repaymentType; }
    public void setRepaymentType(String repaymentType) { this.repaymentType = repaymentType; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public String getCollateralDesc() { return collateralDesc; }
    public void setCollateralDesc(String collateralDesc) { this.collateralDesc = collateralDesc; }
    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public Integer getAppStatus() { return appStatus; }
    public void setAppStatus(Integer appStatus) { this.appStatus = appStatus; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public Integer getRiskLevel() { return riskLevel; }
    public void setRiskLevel(Integer riskLevel) { this.riskLevel = riskLevel; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
    public java.util.Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(java.util.Date submitTime) { this.submitTime = submitTime; }
    public java.util.Date getApproveTime() { return approveTime; }
    public void setApproveTime(java.util.Date approveTime) { this.approveTime = approveTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public java.util.Date getCreateTime() { return createTime; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public java.util.Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(java.util.Date updateTime) { this.updateTime = updateTime; }
}
