package com.ruoyi.risk.domain;

import java.math.BigDecimal;

/**
 * 贷款台账实体
 */
public class LoanLedger {
    private Long id;
    private String loanNo;
    private Long appId;
    private String appNo;
    private Long companyId;
    private String companyName;
    private String productType;
    private BigDecimal loanAmount;
    private BigDecimal loanBalance;
    private BigDecimal interestRate;
    private Integer loanTerm;
    private java.util.Date loanStartDate;
    private java.util.Date loanEndDate;
    private String repaymentType;
    private String guaranteeType;
    private Integer fiveCategory;
    private Integer overdueDays;
    private BigDecimal overdueAmount;
    private Integer status;
    private Long managerId;
    private String managerName;
    private String remark;
    private String createBy;
    private java.util.Date createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLoanNo() { return loanNo; }
    public void setLoanNo(String loanNo) { this.loanNo = loanNo; }
    public Long getAppId() { return appId; }
    public void setAppId(Long appId) { this.appId = appId; }
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
    public BigDecimal getLoanBalance() { return loanBalance; }
    public void setLoanBalance(BigDecimal loanBalance) { this.loanBalance = loanBalance; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public Integer getLoanTerm() { return loanTerm; }
    public void setLoanTerm(Integer loanTerm) { this.loanTerm = loanTerm; }
    public java.util.Date getLoanStartDate() { return loanStartDate; }
    public void setLoanStartDate(java.util.Date loanStartDate) { this.loanStartDate = loanStartDate; }
    public java.util.Date getLoanEndDate() { return loanEndDate; }
    public void setLoanEndDate(java.util.Date loanEndDate) { this.loanEndDate = loanEndDate; }
    public String getRepaymentType() { return repaymentType; }
    public void setRepaymentType(String repaymentType) { this.repaymentType = repaymentType; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public Integer getFiveCategory() { return fiveCategory; }
    public void setFiveCategory(Integer fiveCategory) { this.fiveCategory = fiveCategory; }
    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }
    public BigDecimal getOverdueAmount() { return overdueAmount; }
    public void setOverdueAmount(BigDecimal overdueAmount) { this.overdueAmount = overdueAmount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public java.util.Date getCreateTime() { return createTime; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
}
