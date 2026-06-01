package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险事件对象 risk_event
 */
public class RiskEvent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long companyId;

    @Excel(name = "事件类型")
    private String eventType;

    @Excel(name = "事件标题")
    private String eventTitle;

    private String eventContent;

    @Excel(name = "事件日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date eventDate;

    @Excel(name = "涉及金额(万元)")
    private BigDecimal amount;

    @Excel(name = "严重程度", readConverterExp = "1=低,2=中,3=高")
    private Integer severity;    // 1低 2中 3高

    private Integer isRead;      // 0未读 1已读

    @Excel(name = "处理状态", readConverterExp = "0=未处理,1=已处理")
    private Integer isHandled;   // 0未处理 1已处理

    private String dataSource;
    private String externalId;

    // 关联字段
    @Excel(name = "企业名称")
    private String companyName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public String getEventContent() { return eventContent; }
    public void setEventContent(String eventContent) { this.eventContent = eventContent; }

    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getSeverity() { return severity; }
    public void setSeverity(Integer severity) { this.severity = severity; }

    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }

    public Integer getIsHandled() { return isHandled; }
    public void setIsHandled(Integer isHandled) { this.isHandled = isHandled; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
