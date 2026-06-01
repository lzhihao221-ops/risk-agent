package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 预警记录对象 risk_alert_log
 */
public class RiskAlertLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long ruleId;
    private Long companyId;
    private Long eventId;

    @Excel(name = "预警等级", readConverterExp = "1=低,2=中,3=高,4=极高")
    private Integer alertLevel;

    @Excel(name = "预警标题")
    private String alertTitle;

    private String alertContent;
    private Integer isNotified;
    private Date notifyTime;

    @Excel(name = "处理状态", readConverterExp = "0=未处理,1=已处理,2=已忽略")
    private Integer isHandled;

    private Long handlerId;

    @Excel(name = "处理人")
    private String handlerName;

    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    @Excel(name = "处理结果")
    private String handleResult;

    // 关联字段
    @Excel(name = "企业名称")
    private String companyName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Integer getAlertLevel() { return alertLevel; }
    public void setAlertLevel(Integer alertLevel) { this.alertLevel = alertLevel; }

    public String getAlertTitle() { return alertTitle; }
    public void setAlertTitle(String alertTitle) { this.alertTitle = alertTitle; }

    public String getAlertContent() { return alertContent; }
    public void setAlertContent(String alertContent) { this.alertContent = alertContent; }

    public Integer getIsNotified() { return isNotified; }
    public void setIsNotified(Integer isNotified) { this.isNotified = isNotified; }

    public Date getNotifyTime() { return notifyTime; }
    public void setNotifyTime(Date notifyTime) { this.notifyTime = notifyTime; }

    public Integer getIsHandled() { return isHandled; }
    public void setIsHandled(Integer isHandled) { this.isHandled = isHandled; }

    public Long getHandlerId() { return handlerId; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }

    public String getHandlerName() { return handlerName; }
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }

    public Date getHandleTime() { return handleTime; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }

    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
