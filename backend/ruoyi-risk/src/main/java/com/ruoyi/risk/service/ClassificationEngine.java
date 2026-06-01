package com.ruoyi.risk.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.risk.domain.ClassificationRule;
import com.ruoyi.risk.domain.ClassificationResult;
import com.ruoyi.risk.domain.ClassificationMigration;
import com.ruoyi.risk.mapper.ClassificationRuleMapper;
import com.ruoyi.risk.mapper.ClassificationResultMapper;
import com.ruoyi.risk.mapper.ClassificationMigrationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ClassificationEngine {

    @Autowired
    private ClassificationRuleMapper ruleMapper;
    @Autowired
    private ClassificationResultMapper resultMapper;
    @Autowired
    private ClassificationMigrationMapper migrationMapper;

    /**
     * 对单笔贷款进行五级分类
     */
    public String classify(Long loanId, Long companyId, String companyName,
                           int overdueDays, BigDecimal outstandingBalance,
                           Map<String, Object> extraParams) {
        List<ClassificationRule> rules = ruleMapper.selectEnabled();
        rules.sort((a, b) -> b.getPriority() - a.getPriority());

        String result = "正常";
        String triggerRule = "默认-正常";

        for (ClassificationRule rule : rules) {
            if (evaluateRule(rule, overdueDays, extraParams)) {
                result = rule.getFiveLevel();
                triggerRule = rule.getRuleName();
                break;
            }
        }

        // 查询上次分类
        String previousLevel = null;
        ClassificationResult lastResult = resultMapper.selectLatestByLoan(loanId);
        if (lastResult != null) {
            previousLevel = lastResult.getFiveLevel();
        }

        // 保存结果
        ClassificationResult cr = new ClassificationResult();
        cr.setLoanId(loanId);
        cr.setCompanyId(companyId);
        cr.setCompanyName(companyName);
        cr.setClassifyDate(new Date());
        cr.setFiveLevel(result);
        cr.setPreviousLevel(previousLevel);
        cr.setOverdueDays(overdueDays);
        cr.setOutstandingBalance(outstandingBalance);
        cr.setTriggerRule(triggerRule);
        resultMapper.insert(cr);

        // 检查迁移
        if (previousLevel != null && !previousLevel.equals(result)) {
            ClassificationMigration migration = new ClassificationMigration();
            migration.setLoanId(loanId);
            migration.setCompanyName(companyName);
            migration.setFromLevel(previousLevel);
            migration.setToLevel(result);
            migration.setMigrateDate(new Date());
            migration.setReason(triggerRule);
            migrationMapper.insert(migration);
        }

        return result;
    }

    /**
     * 规则评估
     */
    private boolean evaluateRule(ClassificationRule rule, int overdueDays, Map<String, Object> extraParams) {
        JSONObject condition = JSON.parseObject(rule.getConditionJson());
        String ruleType = rule.getRuleType();

        switch (ruleType) {
            case "OVERDUE_DAYS":
                return evaluateOverdueDays(condition, overdueDays);
            case "PAYMENT_STATUS":
                return evaluatePaymentStatus(condition, extraParams);
            case "DEBT_RATIO":
                return evaluateDebtRatio(condition, extraParams);
            default:
                return false;
        }
    }

    private boolean evaluateOverdueDays(JSONObject condition, int overdueDays) {
        String operator = condition.getString("operator");
        if ("<=".equals(operator)) return overdueDays <= condition.getIntValue("value");
        if (">".equals(operator)) return overdueDays > condition.getIntValue("value");
        if (">=".equals(operator)) return overdueDays >= condition.getIntValue("value");
        if ("=".equals(operator)) return overdueDays == condition.getIntValue("value");
        if ("between".equals(operator)) return overdueDays >= condition.getIntValue("min") && overdueDays <= condition.getIntValue("max");
        return false;
    }

    private boolean evaluatePaymentStatus(JSONObject condition, Map<String, Object> extraParams) {
        String event = condition.getString("event");
        if ("restructuring".equals(event)) {
            return Boolean.TRUE.equals(extraParams.get("isRestructured"));
        }
        if ("interest_overdue_days".equals(event)) {
            int interestOverdueDays = extraParams.get("interestOverdueDays") != null ?
                ((Number) extraParams.get("interestOverdueDays")).intValue() : 0;
            String operator = condition.getString("operator");
            int value = condition.getIntValue("value");
            if (">".equals(operator)) return interestOverdueDays > value;
        }
        return false;
    }

    private boolean evaluateDebtRatio(JSONObject condition, Map<String, Object> extraParams) {
        String field = condition.getString("field");
        double fieldValue = extraParams.get(field) != null ? ((Number) extraParams.get(field)).doubleValue() : 0;
        String operator = condition.getString("operator");
        double value = condition.getDoubleValue("value");
        if (">".equals(operator)) return fieldValue > value;
        if ("<".equals(operator)) return fieldValue < value;
        return false;
    }

    /**
     * 批量分类
     */
    public Map<String, Object> batchClassify(String classifyDate) {
        // 需要集成loan_ledger获取在贷贷款
        Map<String, Object> summary = new HashMap<>();
        summary.put("classifyDate", classifyDate);
        summary.put("total", 0);
        summary.put("正常", 0);
        summary.put("关注", 0);
        summary.put("次级", 0);
        summary.put("可疑", 0);
        summary.put("损失", 0);
        return summary;
    }

    /**
     * 分类统计汇总
     */
    public Map<String, Object> getClassificationSummary(String classifyDate) {
        List<Map<String, Object>> stats = resultMapper.selectSummaryByDate(classifyDate);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("正常", Map.of("count", 0, "amount", BigDecimal.ZERO));
        summary.put("关注", Map.of("count", 0, "amount", BigDecimal.ZERO));
        summary.put("次级", Map.of("count", 0, "amount", BigDecimal.ZERO));
        summary.put("可疑", Map.of("count", 0, "amount", BigDecimal.ZERO));
        summary.put("损失", Map.of("count", 0, "amount", BigDecimal.ZERO));
        for (Map<String, Object> stat : stats) {
            String level = (String) stat.get("five_level");
            summary.put(level, stat);
        }
        return summary;
    }
}
