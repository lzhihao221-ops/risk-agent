package com.ruoyi.risk.controller;

import java.util.List;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.risk.domain.RiskAlertRule;
import com.ruoyi.risk.mapper.RiskAlertRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risk/rule")
public class RiskRuleController extends BaseController {

    @Autowired
    private RiskAlertRuleMapper ruleMapper;

    @GetMapping("/list")
    public TableDataInfo list(RiskAlertRule rule) {
        startPage();
        List<RiskAlertRule> list = ruleMapper.selectRiskAlertRuleList(rule);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(ruleMapper.selectRiskAlertRuleById(id));
    }

    @Log(title = "预警规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiskAlertRule rule) {
        return toAjax(ruleMapper.insertRiskAlertRule(rule));
    }

    @Log(title = "预警规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiskAlertRule rule) {
        return toAjax(ruleMapper.updateRiskAlertRule(rule));
    }

    @Log(title = "预警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(ruleMapper.deleteRiskAlertRuleById(id));
    }
}
