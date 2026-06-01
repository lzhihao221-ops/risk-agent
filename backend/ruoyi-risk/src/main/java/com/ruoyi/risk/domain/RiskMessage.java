package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 站内消息对象 risk_message
 */
public class RiskMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    @Excel(name = "消息标题")
    private String title;

    private String content;

    /** 消息类型(1预警通知2任务提醒3系统通知) */
    @Excel(name = "消息类型", readConverterExp = "1=预警通知,2=任务提醒,3=系统通知")
    private Integer msgType;

    private String refType;
    private Long refId;

    @Excel(name = "已读状态", readConverterExp = "0=未读,1=已读")
    private Integer isRead;

    @Excel(name = "阅读时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getMsgType() { return msgType; }
    public void setMsgType(Integer msgType) { this.msgType = msgType; }

    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }

    public Long getRefId() { return refId; }
    public void setRefId(Long refId) { this.refId = refId; }

    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }

    public Date getReadTime() { return readTime; }
    public void setReadTime(Date readTime) { this.readTime = readTime; }
}
