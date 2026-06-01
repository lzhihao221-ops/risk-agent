package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

public class VarCalculation {
    private Long varId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calcDate;
    private String portfolioType;
    private String method;
    private BigDecimal confidenceLevel;
    private Integer timeHorizon;
    private BigDecimal varAmount;
    private BigDecimal cvarAmount;
    private BigDecimal meanReturn;
    private BigDecimal volatility;
    private BigDecimal skewness;
    private BigDecimal kurtosis;
    private Integer sampleSize;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getVarId() { return varId; }
    public void setVarId(Long varId) { this.varId = varId; }
    public Date getCalcDate() { return calcDate; }
    public void setCalcDate(Date calcDate) { this.calcDate = calcDate; }
    public String getPortfolioType() { return portfolioType; }
    public void setPortfolioType(String portfolioType) { this.portfolioType = portfolioType; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public BigDecimal getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(BigDecimal confidenceLevel) { this.confidenceLevel = confidenceLevel; }
    public Integer getTimeHorizon() { return timeHorizon; }
    public void setTimeHorizon(Integer timeHorizon) { this.timeHorizon = timeHorizon; }
    public BigDecimal getVarAmount() { return varAmount; }
    public void setVarAmount(BigDecimal varAmount) { this.varAmount = varAmount; }
    public BigDecimal getCvarAmount() { return cvarAmount; }
    public void setCvarAmount(BigDecimal cvarAmount) { this.cvarAmount = cvarAmount; }
    public BigDecimal getMeanReturn() { return meanReturn; }
    public void setMeanReturn(BigDecimal meanReturn) { this.meanReturn = meanReturn; }
    public BigDecimal getVolatility() { return volatility; }
    public void setVolatility(BigDecimal volatility) { this.volatility = volatility; }
    public BigDecimal getSkewness() { return skewness; }
    public void setSkewness(BigDecimal skewness) { this.skewness = skewness; }
    public BigDecimal getKurtosis() { return kurtosis; }
    public void setKurtosis(BigDecimal kurtosis) { this.kurtosis = kurtosis; }
    public Integer getSampleSize() { return sampleSize; }
    public void setSampleSize(Integer sampleSize) { this.sampleSize = sampleSize; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
