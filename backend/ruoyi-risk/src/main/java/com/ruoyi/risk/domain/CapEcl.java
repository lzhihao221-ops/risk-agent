package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 资本计量 - ECL（预期信用损失）实体
 */
public class CapEcl {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calcDate;
    private Long loanId;
    private String loanNo;
    private Long companyId;
    private String companyName;
    private Integer stage;
    private BigDecimal carryingAmount;
    private BigDecimal pd12m;
    private BigDecimal pdLifetime;
    private BigDecimal lgd;
    private BigDecimal ead;
    private BigDecimal ecl12m;
    private BigDecimal eclLifetime;
    private BigDecimal eclAmount;
    private BigDecimal provisionRate;
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
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public String getLoanNo() { return loanNo; }
    public void setLoanNo(String loanNo) { this.loanNo = loanNo; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Integer getStage() { return stage; }
    public void setStage(Integer stage) { this.stage = stage; }
    public BigDecimal getCarryingAmount() { return carryingAmount; }
    public void setCarryingAmount(BigDecimal carryingAmount) { this.carryingAmount = carryingAmount; }
    public BigDecimal getPd12m() { return pd12m; }
    public void setPd12m(BigDecimal pd12m) { this.pd12m = pd12m; }
    public BigDecimal getPdLifetime() { return pdLifetime; }
    public void setPdLifetime(BigDecimal pdLifetime) { this.pdLifetime = pdLifetime; }
    public BigDecimal getLgd() { return lgd; }
    public void setLgd(BigDecimal lgd) { this.lgd = lgd; }
    public BigDecimal getEad() { return ead; }
    public void setEad(BigDecimal ead) { this.ead = ead; }
    public BigDecimal getEcl12m() { return ecl12m; }
    public void setEcl12m(BigDecimal ecl12m) { this.ecl12m = ecl12m; }
    public BigDecimal getEclLifetime() { return eclLifetime; }
    public void setEclLifetime(BigDecimal eclLifetime) { this.eclLifetime = eclLifetime; }
    public BigDecimal getEclAmount() { return eclAmount; }
    public void setEclAmount(BigDecimal eclAmount) { this.eclAmount = eclAmount; }
    public BigDecimal getProvisionRate() { return provisionRate; }
    public void setProvisionRate(BigDecimal provisionRate) { this.provisionRate = provisionRate; }
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
