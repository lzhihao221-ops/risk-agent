package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.ClassificationRule;
import java.util.List;

public interface ClassificationRuleMapper {
    ClassificationRule selectById(Long ruleId);
    List<ClassificationRule> selectList(ClassificationRule query);
    List<ClassificationRule> selectEnabled();
    int insert(ClassificationRule record);
    int update(ClassificationRule record);
    int deleteByIds(Long[] ids);
}
