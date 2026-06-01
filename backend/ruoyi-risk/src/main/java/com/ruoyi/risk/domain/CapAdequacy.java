package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 资本计量 - 资本充足率实体
 */
public class CapAdequacy {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calcDate;
    private String reportPeriod;
    private BigDecimal tier1Capital;
    private BigDecimal tier1CapitalCore;
    private BigDecimal tier1CapitalOther;
    private BigDecimal tier2Capital;
    private BigDecimal totalCapital;
    private BigDecimal rwaCredit;
    private BigDecimal rwaMarket;
    private BigDecimal rwaOperation;
    private BigDecimal totalRwa;
    private BigDecimal car;
    private BigDecimal tier1Ratio;
    private BigDecimal coreRatio;
    private BigDecimal carRequirement;
    private BigDecimal tier1Requirement;
    private BigDecimal coreRequirement;
    private BigDecimal leverageRatio;
    private BigDecimal leverageExposure;
    private Integer status;
    private String calcUser;
    private String confirmUser;
    private String remark;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getCalcDate() { return calcDate; }
    public void setCalcDate(Date calcDate) { this.calcDate = calcDate; }
    public String getReportPeriod() { return reportPeriod; }
    public void setReportPeriod(String reportPeriod) { this.reportPeriod = reportPeriod; }
    public BigDecimal getTier1Capital() { return tier1Capital; }
    public void setTier1Capital(BigDecimal tier1Capital) { this.tier1Capital = tier1Capital; }
    public BigDecimal getTier1CapitalCore() { return tier1CapitalCore; }
    public void setTier1CapitalCore(BigDecimal tier1CapitalCore) { this.tier1CapitalCore = tier1CapitalCore; }
    public BigDecimal getTier1CapitalOther() { return tier1CapitalOther; }
    public void setTier1CapitalOther(BigDecimal tier1CapitalOther) { this.tier1CapitalOther = tier1CapitalOther; }
    public BigDecimal getTier2Capital() { return tier2Capital; }
    public void setTier2Capital(BigDecimal tier2Capital) { this.tier2Capital = tier2Capital; }
    public BigDecimal getTotalCapital() { return totalCapital; }
    public void setTotalCapital(BigDecimal totalCapital) { this.totalCapital = totalCapital; }
    public BigDecimal getRwaCredit() { return rwaCredit; }
    public void setRwaCredit(BigDecimal rwaCredit) { this.rwaCredit = rwaCredit; }
    public BigDecimal getRwaMarket() { return rwaMarket; }
    public void setRwaMarket(BigDecimal rwaMarket) { this.rwaMarket = rwaMarket; }
    public BigDecimal getRwaOperation() { return rwaOperation; }
    public void setRwaOperation(BigDecimal rwaOperation) { this.rwaOperation = rwaOperation; }
    public BigDecimal getTotalRwa() { return totalRwa; }
    public void setTotalRwa(BigDecimal totalRwa) { this.totalRwa = totalRwa; }
    public BigDecimal getCar() { return car; }
    public void setCar(BigDecimal car) { this.car = car; }
    public BigDecimal getTier1Ratio() { return tier1Ratio; }
    public void setTier1Ratio(BigDecimal tier1Ratio) { this.tier1Ratio = tier1Ratio; }
    public BigDecimal getCoreRatio() { return coreRatio; }
    public void setCoreRatio(BigDecimal coreRatio) { this.coreRatio = coreRatio; }
    public BigDecimal getCarRequirement() { return carRequirement; }
    public void setCarRequirement(BigDecimal carRequirement) { this.carRequirement = carRequirement; }
    public BigDecimal getTier1Requirement() { return tier1Requirement; }
    public void setTier1Requirement(BigDecimal tier1Requirement) { this.tier1Requirement = tier1Requirement; }
    public BigDecimal getCoreRequirement() { return coreRequirement; }
    public void setCoreRequirement(BigDecimal coreRequirement) { this.coreRequirement = coreRequirement; }
    public BigDecimal getLeverageRatio() { return leverageRatio; }
    public void setLeverageRatio(BigDecimal leverageRatio) { this.leverageRatio = leverageRatio; }
    public BigDecimal getLeverageExposure() { return leverageExposure; }
    public void setLeverageExposure(BigDecimal leverageExposure) { this.leverageExposure = leverageExposure; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getCalcUser() { return calcUser; }
    public void setCalcUser(String calcUser) { this.calcUser = calcUser; }
    public String getConfirmUser() { return confirmUser; }
    public void setConfirmUser(String confirmUser) { this.confirmUser = confirmUser; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
