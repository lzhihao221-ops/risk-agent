package com.ruoyi.risk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class ClassificationMigration {
    private Long migrationId;
    private Long loanId;
    private String companyName;
    private String fromLevel;
    private String toLevel;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date migrateDate;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getMigrationId() { return migrationId; }
    public void setMigrationId(Long migrationId) { this.migrationId = migrationId; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getFromLevel() { return fromLevel; }
    public void setFromLevel(String fromLevel) { this.fromLevel = fromLevel; }
    public String getToLevel() { return toLevel; }
    public void setToLevel(String toLevel) { this.toLevel = toLevel; }
    public Date getMigrateDate() { return migrateDate; }
    public void setMigrateDate(Date migrateDate) { this.migrateDate = migrateDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
