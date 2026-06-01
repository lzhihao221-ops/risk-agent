package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.RiskAlertRule;
import java.util.List;

/**
 * 预警规则Mapper接口
 */
public interface RiskAlertRuleMapper {

    RiskAlertRule selectRiskAlertRuleById(Long id);

    List<RiskAlertRule> selectRiskAlertRuleList(RiskAlertRule rule);

    int insertRiskAlertRule(RiskAlertRule rule);

    int updateRiskAlertRule(RiskAlertRule rule);

    int deleteRiskAlertRuleByIds(Long[] ids);

    int deleteRiskAlertRuleById(Long id);

    /** 查询所有启用的规则 */
    List<RiskAlertRule> selectActiveRules();
}
