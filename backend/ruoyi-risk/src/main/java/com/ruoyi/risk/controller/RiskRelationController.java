package com.ruoyi.risk.controller;

import java.util.List;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.risk.domain.RiskCompanyRelation;
import com.ruoyi.risk.mapper.RiskCompanyRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risk/relation")
public class RiskRelationController extends BaseController {

    @Autowired
    private RiskCompanyRelationMapper relationMapper;

    @GetMapping("/list")
    public TableDataInfo list(RiskCompanyRelation relation) {
        startPage();
        List<RiskCompanyRelation> list = relationMapper.selectRiskCompanyRelationList(relation);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(relationMapper.selectRiskCompanyRelationById(id));
    }

    @Log(title = "关联关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiskCompanyRelation relation) {
        return toAjax(relationMapper.insertRiskCompanyRelation(relation));
    }

    @Log(title = "关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(relationMapper.deleteRiskCompanyRelationById(id));
    }
}
