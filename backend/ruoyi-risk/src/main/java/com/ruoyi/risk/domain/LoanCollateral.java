package com.ruoyi.risk.domain;

import java.math.BigDecimal;

public class LoanCollateral {
    private Long id;
    private Long companyId;
    private Long appId;
    private String collateralType;
    private String collateralName;
    private String collateralDesc;
    private String certNo;
    private String location;
    private BigDecimal evalValue;
    private BigDecimal pledgeValue;
    private BigDecimal pledgeRatio;
    private java.util.Date evalDate;
    private String evalOrg;
    private java.util.Date expireDate;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getAppId() { return appId; }
    public void setAppId(Long appId) { this.appId = appId; }
    public String getCollateralType() { return collateralType; }
    public void setCollateralType(String collateralType) { this.collateralType = collateralType; }
    public String getCollateralName() { return collateralName; }
    public void setCollateralName(String collateralName) { this.collateralName = collateralName; }
    public String getCollateralDesc() { return collateralDesc; }
    public void setCollateralDesc(String collateralDesc) { this.collateralDesc = collateralDesc; }
    public String getCertNo() { return certNo; }
    public void setCertNo(String certNo) { this.certNo = certNo; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public BigDecimal getEvalValue() { return evalValue; }
    public void setEvalValue(BigDecimal evalValue) { this.evalValue = evalValue; }
    public BigDecimal getPledgeValue() { return pledgeValue; }
    public void setPledgeValue(BigDecimal pledgeValue) { this.pledgeValue = pledgeValue; }
    public BigDecimal getPledgeRatio() { return pledgeRatio; }
    public void setPledgeRatio(BigDecimal pledgeRatio) { this.pledgeRatio = pledgeRatio; }
    public java.util.Date getEvalDate() { return evalDate; }
    public void setEvalDate(java.util.Date evalDate) { this.evalDate = evalDate; }
    public String getEvalOrg() { return evalOrg; }
    public void setEvalOrg(String evalOrg) { this.evalOrg = evalOrg; }
    public java.util.Date getExpireDate() { return expireDate; }
    public void setExpireDate(java.util.Date expireDate) { this.expireDate = expireDate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
