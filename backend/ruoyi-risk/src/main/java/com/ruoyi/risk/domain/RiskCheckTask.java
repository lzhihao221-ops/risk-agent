package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 贷后检查任务对象 risk_check_task
 */
public class RiskCheckTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long companyId;
    private String loanId;
    private String taskType;       // REGULAR/SPECIAL/ALERT
    private Integer taskStatus;    // 0待执行 1执行中 2已完成 3已逾期
    private Long assigneeId;
    private String assigneeName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    private String checkResult;    // NORMAL/ATTENTION/ALERT/RISK
    private String checkReport;
    private String remark;

    // 关联字段
    private String companyName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public Integer getTaskStatus() { return taskStatus; }
    public void setTaskStatus(Integer taskStatus) { this.taskStatus = taskStatus; }

    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }

    public String getAssigneeName() { return assigneeName; }
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getCompleteTime() { return completeTime; }
    public void setCompleteTime(Date completeTime) { this.completeTime = completeTime; }

    public String getCheckResult() { return checkResult; }
    public void setCheckResult(String checkResult) { this.checkResult = checkResult; }

    public String getCheckReport() { return checkReport; }
    public void setCheckReport(String checkReport) { this.checkReport = checkReport; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
