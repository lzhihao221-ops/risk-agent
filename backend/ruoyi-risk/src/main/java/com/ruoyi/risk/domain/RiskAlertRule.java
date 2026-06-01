package com.ruoyi.risk.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预警规则对象 risk_alert_rule
 */
public class RiskAlertRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String ruleName;
    private String ruleCode;
    private String ruleType;       // EVENT/INDICATOR/COMBO
    private String eventType;
    private String indicatorCode;
    private String operator;       // GT/GTE/LT/LTE/EQ/NEQ
    private String thresholdValue;
    private Integer alertLevel;    // 1低 2中 3高
    private Integer isActive;      // 0禁用 1启用
    private String notifyType;     // 站内消息/SMS/企业微信

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getIndicatorCode() { return indicatorCode; }
    public void setIndicatorCode(String indicatorCode) { this.indicatorCode = indicatorCode; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(String thresholdValue) { this.thresholdValue = thresholdValue; }

    public Integer getAlertLevel() { return alertLevel; }
    public void setAlertLevel(Integer alertLevel) { this.alertLevel = alertLevel; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public String getNotifyType() { return notifyType; }
    public void setNotifyType(String notifyType) { this.notifyType = notifyType; }
}
