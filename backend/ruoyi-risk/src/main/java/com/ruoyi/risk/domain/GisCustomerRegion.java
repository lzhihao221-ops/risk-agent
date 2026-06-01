package com.ruoyi.risk.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;

public class GisCustomerRegion extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String regionName;
    private String regionType;
    private Double longitude;
    private Double latitude;
    private Integer customerCount;
    private Integer loanCount;
    private BigDecimal loanAmount;
    private String riskLevel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public String getRegionType() { return regionType; }
    public void setRegionType(String regionType) { this.regionType = regionType; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Integer getCustomerCount() { return customerCount; }
    public void setCustomerCount(Integer customerCount) { this.customerCount = customerCount; }
    public Integer getLoanCount() { return loanCount; }
    public void setLoanCount(Integer loanCount) { this.loanCount = loanCount; }
    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}
