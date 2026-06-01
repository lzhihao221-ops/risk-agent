package com.ruoyi.risk.mapper;

import java.util.List;
import com.ruoyi.risk.domain.RiskCompanyRelation;

public interface RiskCompanyRelationMapper {
    RiskCompanyRelation selectRiskCompanyRelationById(Long id);
    List<RiskCompanyRelation> selectRiskCompanyRelationList(RiskCompanyRelation relation);
    List<RiskCompanyRelation> selectByCompanyId(Long companyId);
    int insertRiskCompanyRelation(RiskCompanyRelation relation);
    int deleteRiskCompanyRelationById(Long id);
}
