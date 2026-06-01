package com.ruoyi.risk.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class RegIndicator extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "指标编码")
    private String indicatorCode;

    @Excel(name = "指标名称")
    private String indicatorName;

    @Excel(name = "指标类型")
    private String indicatorType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计算日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date calcDate;

    @Excel(name = "报表期次")
    private String reportPeriod;

    @Excel(name = "指标值")
    private BigDecimal indicatorValue;

    @Excel(name = "单位")
    private String unit;

    @Excel(name = "预警阈值")
    private BigDecimal thresholdWarn;

    @Excel(name = "警戒阈值")
    private BigDecimal thresholdDanger;

    @Excel(name = "监管红线")
    private BigDecimal regulatoryLine;

    @Excel(name = "状态")
    private String status;

    private String calcDetail;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIndicatorCode() { return indicatorCode; }
    public void setIndicatorCode(String indicatorCode) { this.indicatorCode = indicatorCode; }

    public String getIndicatorName() { return indicatorName; }
    public void setIndicatorName(String indicatorName) { this.indicatorName = indicatorName; }

    public String getIndicatorType() { return indicatorType; }
    public void setIndicatorType(String indicatorType) { this.indicatorType = indicatorType; }

    public Date getCalcDate() { return calcDate; }
    public void setCalcDate(Date calcDate) { this.calcDate = calcDate; }

    public String getReportPeriod() { return reportPeriod; }
    public void setReportPeriod(String reportPeriod) { this.reportPeriod = reportPeriod; }

    public BigDecimal getIndicatorValue() { return indicatorValue; }
    public void setIndicatorValue(BigDecimal indicatorValue) { this.indicatorValue = indicatorValue; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getThresholdWarn() { return thresholdWarn; }
    public void setThresholdWarn(BigDecimal thresholdWarn) { this.thresholdWarn = thresholdWarn; }

    public BigDecimal getThresholdDanger() { return thresholdDanger; }
    public void setThresholdDanger(BigDecimal thresholdDanger) { this.thresholdDanger = thresholdDanger; }

    public BigDecimal getRegulatoryLine() { return regulatoryLine; }
    public void setRegulatoryLine(BigDecimal regulatoryLine) { this.regulatoryLine = regulatoryLine; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCalcDetail() { return calcDetail; }
    public void setCalcDetail(String calcDetail) { this.calcDetail = calcDetail; }
}
