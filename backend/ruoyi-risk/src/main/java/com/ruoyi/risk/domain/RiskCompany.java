package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业基本信息对象 risk_company
 */
public class RiskCompany extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 企业ID */
    private Long id;

    /** 企业名称 */
    @Excel(name = "企业名称")
    private String companyName;

    /** 统一社会信用代码 */
    @Excel(name = "信用代码")
    private String creditCode;

    /** 法定代表人 */
    @Excel(name = "法定代表人")
    private String legalPerson;

    /** 注册资本（万元） */
    @Excel(name = "注册资本(万元)")
    private BigDecimal regCapital;

    /** 成立日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成立日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date establishDate;

    /** 所属行业 */
    @Excel(name = "所属行业")
    private String industry;

    /** 省份 */
    private String province;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 区县 */
    private String district;

    /** 注册地址 */
    private String address;

    /** 经营范围 */
    private String businessScope;

    /** 状态：1正常 2注销 3吊销 */
    private Integer status;

    /** 风险等级：0未评估 1低 2中 3高 4极高 */
    @Excel(name = "风险等级", readConverterExp = "0=未评估,1=低,2=中,3=高,4=极高")
    private Integer riskLevel;

    /** 风险评分（0-100，越高越危险） */
    @Excel(name = "风险评分")
    private Integer riskScore;

    /** 最近扫描时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastScanTime;

    private Long managerId;
    private String managerName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCreditCode() { return creditCode; }
    public void setCreditCode(String creditCode) { this.creditCode = creditCode; }

    public String getLegalPerson() { return legalPerson; }
    public void setLegalPerson(String legalPerson) { this.legalPerson = legalPerson; }

    public BigDecimal getRegCapital() { return regCapital; }
    public void setRegCapital(BigDecimal regCapital) { this.regCapital = regCapital; }

    public Date getEstablishDate() { return establishDate; }
    public void setEstablishDate(Date establishDate) { this.establishDate = establishDate; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBusinessScope() { return businessScope; }
    public void setBusinessScope(String businessScope) { this.businessScope = businessScope; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getRiskLevel() { return riskLevel; }
    public void setRiskLevel(Integer riskLevel) { this.riskLevel = riskLevel; }

    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public Date getLastScanTime() { return lastScanTime; }
    public void setLastScanTime(Date lastScanTime) { this.lastScanTime = lastScanTime; }

    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
}
