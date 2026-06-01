package com.ruoyi.risk.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;

public class GisRiskPoint extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String pointName;
    private Double longitude;
    private Double latitude;
    private String riskType;
    private String riskLevel;
    private Double intensity;
    private BigDecimal amount;
    private String companyName;
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPointName() { return pointName; }
    public void setPointName(String pointName) { this.pointName = pointName; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public Double getIntensity() { return intensity; }
    public void setIntensity(Double intensity) { this.intensity = intensity; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
