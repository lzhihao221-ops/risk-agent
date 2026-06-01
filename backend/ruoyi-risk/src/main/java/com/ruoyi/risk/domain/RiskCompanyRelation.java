package com.ruoyi.risk.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class RiskCompanyRelation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "企业ID")
    private Long companyId;

    @Excel(name = "关联企业")
    private String relatedCompany;

    @Excel(name = "关联信用代码")
    private String relatedCreditCode;

    @Excel(name = "关联人")
    private String relatedPerson;

    @Excel(name = "关系类型")
    private String relationType;

    @Excel(name = "关系详情")
    private String relationDetail;

    @Excel(name = "数据来源")
    private String dataSource;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getRelatedCompany() { return relatedCompany; }
    public void setRelatedCompany(String relatedCompany) { this.relatedCompany = relatedCompany; }
    public String getRelatedCreditCode() { return relatedCreditCode; }
    public void setRelatedCreditCode(String relatedCreditCode) { this.relatedCreditCode = relatedCreditCode; }
    public String getRelatedPerson() { return relatedPerson; }
    public void setRelatedPerson(String relatedPerson) { this.relatedPerson = relatedPerson; }
    public String getRelationType() { return relationType; }
    public void setRelationType(String relationType) { this.relationType = relationType; }
    public String getRelationDetail() { return relationDetail; }
    public void setRelationDetail(String relationDetail) { this.relationDetail = relationDetail; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
