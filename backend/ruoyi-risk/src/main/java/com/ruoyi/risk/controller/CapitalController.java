package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.CapRwa;
import com.ruoyi.risk.domain.CapEcl;
import com.ruoyi.risk.domain.CapAdequacy;
import com.ruoyi.risk.domain.CapProvision;
import com.ruoyi.risk.service.ICapRwaService;
import com.ruoyi.risk.service.ICapEclService;
import com.ruoyi.risk.service.ICapAdequacyService;
import com.ruoyi.risk.service.ICapProvisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/capital")
public class CapitalController extends BaseController {

    @Autowired private ICapRwaService rwaService;
    @Autowired private ICapEclService eclService;
    @Autowired private ICapAdequacyService adequacyService;
    @Autowired private ICapProvisionService provisionService;

    // ========== RWA ==========
    @PreAuthorize("@ss.hasPermi('capital:rwa:list')")
    @GetMapping("/rwa/list")
    public TableDataInfo rwaList(CapRwa query) {
        startPage();
        List<CapRwa> list = rwaService.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('capital:rwa:query')")
    @GetMapping("/rwa/{id}")
    public AjaxResult rwaGet(@PathVariable Long id) {
        return success(rwaService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('capital:rwa:add')")
    @PostMapping("/rwa")
    public AjaxResult rwaAdd(@RequestBody CapRwa record) {
        return toAjax(rwaService.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('capital:rwa:edit')")
    @PutMapping("/rwa")
    public AjaxResult rwaEdit(@RequestBody CapRwa record) {
        return toAjax(rwaService.update(record));
    }

    @PreAuthorize("@ss.hasPermi('capital:rwa:remove')")
    @DeleteMapping("/rwa/{ids}")
    public AjaxResult rwaRemove(@PathVariable Long[] ids) {
        return toAjax(rwaService.deleteByIds(ids));
    }

    // ========== ECL ==========
    @PreAuthorize("@ss.hasPermi('capital:ecl:list')")
    @GetMapping("/ecl/list")
    public TableDataInfo eclList(CapEcl query) {
        startPage();
        List<CapEcl> list = eclService.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('capital:ecl:query')")
    @GetMapping("/ecl/{id}")
    public AjaxResult eclGet(@PathVariable Long id) {
        return success(eclService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('capital:ecl:add')")
    @PostMapping("/ecl")
    public AjaxResult eclAdd(@RequestBody CapEcl record) {
        return toAjax(eclService.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('capital:ecl:edit')")
    @PutMapping("/ecl")
    public AjaxResult eclEdit(@RequestBody CapEcl record) {
        return toAjax(eclService.update(record));
    }

    @PreAuthorize("@ss.hasPermi('capital:ecl:remove')")
    @DeleteMapping("/ecl/{ids}")
    public AjaxResult eclRemove(@PathVariable Long[] ids) {
        return toAjax(eclService.deleteByIds(ids));
    }

    // ========== 资本充足率 ==========
    @PreAuthorize("@ss.hasPermi('capital:adequacy:list')")
    @GetMapping("/adequacy/list")
    public TableDataInfo adequacyList(CapAdequacy query) {
        startPage();
        List<CapAdequacy> list = adequacyService.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('capital:adequacy:query')")
    @GetMapping("/adequacy/{id}")
    public AjaxResult adequacyGet(@PathVariable Long id) {
        return success(adequacyService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('capital:adequacy:query')")
    @GetMapping("/adequacy/latest")
    public AjaxResult adequacyLatest() {
        return success(adequacyService.selectLatest());
    }

    @PreAuthorize("@ss.hasPermi('capital:adequacy:add')")
    @PostMapping("/adequacy")
    public AjaxResult adequacyAdd(@RequestBody CapAdequacy record) {
        return toAjax(adequacyService.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('capital:adequacy:edit')")
    @PutMapping("/adequacy")
    public AjaxResult adequacyEdit(@RequestBody CapAdequacy record) {
        return toAjax(adequacyService.update(record));
    }

    @PreAuthorize("@ss.hasPermi('capital:adequacy:remove')")
    @DeleteMapping("/adequacy/{ids}")
    public AjaxResult adequacyRemove(@PathVariable Long[] ids) {
        return toAjax(adequacyService.deleteByIds(ids));
    }

    // ========== 拨备计提 ==========
    @PreAuthorize("@ss.hasPermi('capital:provision:list')")
    @GetMapping("/provision/list")
    public TableDataInfo provisionList(CapProvision query) {
        startPage();
        List<CapProvision> list = provisionService.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('capital:provision:query')")
    @GetMapping("/provision/{id}")
    public AjaxResult provisionGet(@PathVariable Long id) {
        return success(provisionService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('capital:provision:add')")
    @PostMapping("/provision")
    public AjaxResult provisionAdd(@RequestBody CapProvision record) {
        return toAjax(provisionService.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('capital:provision:edit')")
    @PutMapping("/provision")
    public AjaxResult provisionEdit(@RequestBody CapProvision record) {
        return toAjax(provisionService.update(record));
    }

    @PreAuthorize("@ss.hasPermi('capital:provision:remove')")
    @DeleteMapping("/provision/{ids}")
    public AjaxResult provisionRemove(@PathVariable Long[] ids) {
        return toAjax(provisionService.deleteByIds(ids));
    }

    // ========== 概况汇总 ==========
    @GetMapping("/overview")
    public AjaxResult overview() {
        CapAdequacy latest = adequacyService.selectLatest();
        AjaxResult result = success(latest);
        // 添加趋势数据（最近几条资本充足率记录）
        if (latest != null) {
            List<CapAdequacy> trends = new ArrayList<>();
            trends.add(latest);
            result.put("trends", trends);
        }
        return result;
    }
}
