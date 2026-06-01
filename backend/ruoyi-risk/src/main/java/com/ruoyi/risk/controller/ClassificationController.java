package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.ClassificationRule;
import com.ruoyi.risk.domain.ClassificationResult;
import com.ruoyi.risk.domain.ClassificationMigration;
import com.ruoyi.risk.mapper.ClassificationRuleMapper;
import com.ruoyi.risk.mapper.ClassificationResultMapper;
import com.ruoyi.risk.mapper.ClassificationMigrationMapper;
import com.ruoyi.risk.service.ClassificationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson2.JSONObject;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/classification")
public class ClassificationController extends BaseController {

    @Autowired
    private ClassificationRuleMapper ruleMapper;
    @Autowired
    private ClassificationResultMapper resultMapper;
    @Autowired
    private ClassificationMigrationMapper migrationMapper;
    @Autowired
    private ClassificationEngine classificationEngine;

    // ========== 规则管理 ==========
    @PreAuthorize("@ss.hasPermi('classification:rule:list')")
    @GetMapping("/rule/list")
    public TableDataInfo ruleList(ClassificationRule query) {
        startPage();
        return getDataTable(ruleMapper.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('classification:rule:add')")
    @PostMapping("/rule")
    public AjaxResult addRule(@RequestBody ClassificationRule record) {
        return toAjax(ruleMapper.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('classification:rule:edit')")
    @PutMapping("/rule")
    public AjaxResult editRule(@RequestBody ClassificationRule record) {
        return toAjax(ruleMapper.update(record));
    }

    @PreAuthorize("@ss.hasPermi('classification:rule:remove')")
    @DeleteMapping("/rule/{ids}")
    public AjaxResult removeRule(@PathVariable Long[] ids) {
        return toAjax(ruleMapper.deleteByIds(ids));
    }

    // ========== 分类结果 ==========
    @PreAuthorize("@ss.hasPermi('classification:result:list')")
    @GetMapping("/result/list")
    public TableDataInfo resultList(ClassificationResult query) {
        startPage();
        return getDataTable(resultMapper.selectList(query));
    }

    // ========== 迁移记录 ==========
    @PreAuthorize("@ss.hasPermi('classification:migration:list')")
    @GetMapping("/migration/list")
    public TableDataInfo migrationList(ClassificationMigration query) {
        startPage();
        return getDataTable(migrationMapper.selectList(query));
    }

    // ========== 分类操作 ==========
    @PreAuthorize("@ss.hasPermi('classification:result:add')")
    @PostMapping("/classify")
    public AjaxResult classify(@RequestBody JSONObject params) {
        Long loanId = params.getLong("loanId");
        Long companyId = params.getLong("companyId");
        String companyName = params.getString("companyName");
        int overdueDays = params.getIntValue("overdueDays");
        BigDecimal balance = params.getBigDecimal("outstandingBalance");
        Map<String, Object> extra = new HashMap<>();
        if (params.containsKey("debtRatio")) extra.put("debt_ratio", params.getDoubleValue("debtRatio"));
        if (params.containsKey("isRestructured")) extra.put("isRestructured", params.getBoolean("isRestructured"));
        String result = classificationEngine.classify(loanId, companyId, companyName, overdueDays, balance, extra);
        return success(result);
    }

    @PreAuthorize("@ss.hasPermi('classification:result:add')")
    @PostMapping("/batchClassify")
    public AjaxResult batchClassify(@RequestBody JSONObject params) {
        String classifyDate = params.getString("classifyDate");
        return success(classificationEngine.batchClassify(classifyDate));
    }

    @PreAuthorize("@ss.hasPermi('classification:result:list')")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam String classifyDate) {
        return success(classificationEngine.getClassificationSummary(classifyDate));
    }
}
