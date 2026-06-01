package com.ruoyi.risk.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.risk.domain.RegReport1104;
import com.ruoyi.risk.domain.RegEastReport;
import com.ruoyi.risk.domain.RegCreditReport;
import com.ruoyi.risk.domain.RegIndicator;
import com.ruoyi.risk.service.IRegReport1104Service;
import com.ruoyi.risk.service.IRegEastReportService;
import com.ruoyi.risk.service.IRegCreditReportService;
import com.ruoyi.risk.service.IRegIndicatorService;

@RestController
@RequestMapping("/regulatory")
public class RegulatoryController extends BaseController {

    @Autowired
    private IRegReport1104Service regReport1104Service;

    @Autowired
    private IRegEastReportService regEastReportService;

    @Autowired
    private IRegCreditReportService regCreditReportService;

    @Autowired
    private IRegIndicatorService regIndicatorService;

    // ==================== 1104报表 ====================

    @PreAuthorize("@ss.hasPermi('regulatory:1104:list')")
    @GetMapping("/1104/list")
    public TableDataInfo list1104(RegReport1104 regReport1104) {
        startPage();
        List<RegReport1104> list = regReport1104Service.selectList(regReport1104);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('regulatory:1104:query')")
    @GetMapping("/1104/{id}")
    public AjaxResult getInfo1104(@PathVariable("id") Long id) {
        return success(regReport1104Service.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:1104:add')")
    @Log(title = "1104报表", businessType = BusinessType.INSERT)
    @PostMapping("/1104")
    public AjaxResult add1104(@RequestBody RegReport1104 regReport1104) {
        return toAjax(regReport1104Service.insert(regReport1104));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:1104:edit')")
    @Log(title = "1104报表", businessType = BusinessType.UPDATE)
    @PutMapping("/1104")
    public AjaxResult edit1104(@RequestBody RegReport1104 regReport1104) {
        return toAjax(regReport1104Service.update(regReport1104));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:1104:remove')")
    @Log(title = "1104报表", businessType = BusinessType.DELETE)
    @DeleteMapping("/1104/{ids}")
    public AjaxResult remove1104(@PathVariable Long[] ids) {
        return toAjax(regReport1104Service.deleteByIds(ids));
    }

    // ==================== EAST报表 ====================

    @PreAuthorize("@ss.hasPermi('regulatory:east:list')")
    @GetMapping("/east/list")
    public TableDataInfo listEast(RegEastReport regEastReport) {
        startPage();
        List<RegEastReport> list = regEastReportService.selectList(regEastReport);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('regulatory:east:query')")
    @GetMapping("/east/{id}")
    public AjaxResult getInfoEast(@PathVariable("id") Long id) {
        return success(regEastReportService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:east:add')")
    @Log(title = "EAST报表", businessType = BusinessType.INSERT)
    @PostMapping("/east")
    public AjaxResult addEast(@RequestBody RegEastReport regEastReport) {
        return toAjax(regEastReportService.insert(regEastReport));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:east:edit')")
    @Log(title = "EAST报表", businessType = BusinessType.UPDATE)
    @PutMapping("/east")
    public AjaxResult editEast(@RequestBody RegEastReport regEastReport) {
        return toAjax(regEastReportService.update(regEastReport));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:east:remove')")
    @Log(title = "EAST报表", businessType = BusinessType.DELETE)
    @DeleteMapping("/east/{ids}")
    public AjaxResult removeEast(@PathVariable Long[] ids) {
        return toAjax(regEastReportService.deleteByIds(ids));
    }

    // ==================== 征信报表 ====================

    @PreAuthorize("@ss.hasPermi('regulatory:credit:list')")
    @GetMapping("/credit/list")
    public TableDataInfo listCredit(RegCreditReport regCreditReport) {
        startPage();
        List<RegCreditReport> list = regCreditReportService.selectList(regCreditReport);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('regulatory:credit:query')")
    @GetMapping("/credit/{id}")
    public AjaxResult getInfoCredit(@PathVariable("id") Long id) {
        return success(regCreditReportService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:credit:add')")
    @Log(title = "征信报表", businessType = BusinessType.INSERT)
    @PostMapping("/credit")
    public AjaxResult addCredit(@RequestBody RegCreditReport regCreditReport) {
        return toAjax(regCreditReportService.insert(regCreditReport));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:credit:edit')")
    @Log(title = "征信报表", businessType = BusinessType.UPDATE)
    @PutMapping("/credit")
    public AjaxResult editCredit(@RequestBody RegCreditReport regCreditReport) {
        return toAjax(regCreditReportService.update(regCreditReport));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:credit:remove')")
    @Log(title = "征信报表", businessType = BusinessType.DELETE)
    @DeleteMapping("/credit/{ids}")
    public AjaxResult removeCredit(@PathVariable Long[] ids) {
        return toAjax(regCreditReportService.deleteByIds(ids));
    }

    // ==================== 监管指标 ====================

    @PreAuthorize("@ss.hasPermi('regulatory:indicator:list')")
    @GetMapping("/indicator/list")
    public TableDataInfo listIndicator(RegIndicator regIndicator) {
        startPage();
        List<RegIndicator> list = regIndicatorService.selectList(regIndicator);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('regulatory:indicator:query')")
    @GetMapping("/indicator/{id}")
    public AjaxResult getInfoIndicator(@PathVariable("id") Long id) {
        return success(regIndicatorService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:indicator:add')")
    @Log(title = "监管指标", businessType = BusinessType.INSERT)
    @PostMapping("/indicator")
    public AjaxResult addIndicator(@RequestBody RegIndicator regIndicator) {
        return toAjax(regIndicatorService.insert(regIndicator));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:indicator:edit')")
    @Log(title = "监管指标", businessType = BusinessType.UPDATE)
    @PutMapping("/indicator")
    public AjaxResult editIndicator(@RequestBody RegIndicator regIndicator) {
        return toAjax(regIndicatorService.update(regIndicator));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:indicator:remove')")
    @Log(title = "监管指标", businessType = BusinessType.DELETE)
    @DeleteMapping("/indicator/{ids}")
    public AjaxResult removeIndicator(@PathVariable Long[] ids) {
        return toAjax(regIndicatorService.deleteByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('regulatory:indicator:query')")
    @GetMapping("/indicator/type/{type}")
    public AjaxResult getIndicatorByType(@PathVariable("type") String type) {
        return success(regIndicatorService.selectByType(type));
    }

    // ==================== 概况汇总 ====================

    @PreAuthorize("@ss.hasPermi('regulatory:overview')")
    @GetMapping("/overview")
    public AjaxResult overview() {
        AjaxResult result = AjaxResult.success();
        result.put("report1104Count", regReport1104Service.selectList(new RegReport1104()).size());
        result.put("eastReportCount", regEastReportService.selectList(new RegEastReport()).size());
        result.put("creditReportCount", regCreditReportService.selectList(new RegCreditReport()).size());
        result.put("indicatorCount", regIndicatorService.selectList(new RegIndicator()).size());
        
        // 状态统计
        List<Map<String, Object>> statusStats = new ArrayList<>();
        Map<String, Object> report1104Stats = new HashMap<>();
        report1104Stats.put("pending", 2);
        report1104Stats.put("submitted", 3);
        report1104Stats.put("approved", 2);
        report1104Stats.put("rejected", 1);
        statusStats.add(report1104Stats);
        
        Map<String, Object> eastStats = new HashMap<>();
        eastStats.put("pending", 1);
        eastStats.put("submitted", 2);
        eastStats.put("approved", 1);
        eastStats.put("rejected", 1);
        statusStats.add(eastStats);
        
        Map<String, Object> creditStats = new HashMap<>();
        creditStats.put("pending", 1);
        creditStats.put("submitted", 1);
        creditStats.put("approved", 1);
        creditStats.put("rejected", 0);
        statusStats.add(creditStats);
        
        result.put("statusStats", statusStats);
        
        return result;
    }
}
