package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 资本计量 - RWA（风险加权资产）实体
 */
public class CapRwa {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calcDate;
    private Long companyId;
    private String companyName;
    private Long loanId;
    private String loanNo;
    private BigDecimal exposureAmount;
    private BigDecimal pd;
    private BigDecimal lgd;
    private BigDecimal ead;
    private BigDecimal maturity;
    private String assetClass;
    private BigDecimal riskWeight;
    private BigDecimal rwaAmount;
    private String calcMethod;
    private Integer status;
    private String remark;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getCalcDate() { return calcDate; }
    public void setCalcDate(Date calcDate) { this.calcDate = calcDate; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public String getLoanNo() { return loanNo; }
    public void setLoanNo(String loanNo) { this.loanNo = loanNo; }
    public BigDecimal getExposureAmount() { return exposureAmount; }
    public void setExposureAmount(BigDecimal exposureAmount) { this.exposureAmount = exposureAmount; }
    public BigDecimal getPd() { return pd; }
    public void setPd(BigDecimal pd) { this.pd = pd; }
    public BigDecimal getLgd() { return lgd; }
    public void setLgd(BigDecimal lgd) { this.lgd = lgd; }
    public BigDecimal getEad() { return ead; }
    public void setEad(BigDecimal ead) { this.ead = ead; }
    public BigDecimal getMaturity() { return maturity; }
    public void setMaturity(BigDecimal maturity) { this.maturity = maturity; }
    public String getAssetClass() { return assetClass; }
    public void setAssetClass(String assetClass) { this.assetClass = assetClass; }
    public BigDecimal getRiskWeight() { return riskWeight; }
    public void setRiskWeight(BigDecimal riskWeight) { this.riskWeight = riskWeight; }
    public BigDecimal getRwaAmount() { return rwaAmount; }
    public void setRwaAmount(BigDecimal rwaAmount) { this.rwaAmount = rwaAmount; }
    public String getCalcMethod() { return calcMethod; }
    public void setCalcMethod(String calcMethod) { this.calcMethod = calcMethod; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
