package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 资本计量 - 拨备计提实体
 */
public class CapProvision {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calcDate;
    private Long loanId;
    private String loanNo;
    private Long companyId;
    private String companyName;
    private String fiveCategory;
    private String provisionType;
    private BigDecimal carryingAmount;
    private BigDecimal provisionRate;
    private BigDecimal provisionAmount;
    private BigDecimal accumulatedProvision;
    private BigDecimal shortfall;
    private Integer status;
    private String remark;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getCalcDate() { return calcDate; }
    public void setCalcDate(Date calcDate) { this.calcDate = calcDate; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public String getLoanNo() { return loanNo; }
    public void setLoanNo(String loanNo) { this.loanNo = loanNo; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getFiveCategory() { return fiveCategory; }
    public void setFiveCategory(String fiveCategory) { this.fiveCategory = fiveCategory; }
    public String getProvisionType() { return provisionType; }
    public void setProvisionType(String provisionType) { this.provisionType = provisionType; }
    public BigDecimal getCarryingAmount() { return carryingAmount; }
    public void setCarryingAmount(BigDecimal carryingAmount) { this.carryingAmount = carryingAmount; }
    public BigDecimal getProvisionRate() { return provisionRate; }
    public void setProvisionRate(BigDecimal provisionRate) { this.provisionRate = provisionRate; }
    public BigDecimal getProvisionAmount() { return provisionAmount; }
    public void setProvisionAmount(BigDecimal provisionAmount) { this.provisionAmount = provisionAmount; }
    public BigDecimal getAccumulatedProvision() { return accumulatedProvision; }
    public void setAccumulatedProvision(BigDecimal accumulatedProvision) { this.accumulatedProvision = accumulatedProvision; }
    public BigDecimal getShortfall() { return shortfall; }
    public void setShortfall(BigDecimal shortfall) { this.shortfall = shortfall; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
