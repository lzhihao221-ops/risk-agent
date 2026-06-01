package com.ruoyi.risk.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.risk.domain.RiskCompany;
import com.ruoyi.risk.service.IRiskCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * 企业信息 Controller
 */
@RestController
@RequestMapping("/risk/company")
public class RiskCompanyController extends BaseController {

    @Autowired
    private IRiskCompanyService companyService;

    /** 企业列表 */
    @PreAuthorize("@ss.hasPermi('risk:company:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiskCompany company) {
        startPage();
        List<RiskCompany> list = companyService.selectRiskCompanyList(company);
        return getDataTable(list);
    }

    /** 企业详情 */
    @PreAuthorize("@ss.hasPermi('risk:company:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(companyService.selectRiskCompanyById(id));
    }

    /** 新增企业 */
    @PreAuthorize("@ss.hasPermi('risk:company:add')")
    @Log(title = "企业管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiskCompany company) {
        return toAjax(companyService.insertRiskCompany(company));
    }

    /** 修改企业 */
    @PreAuthorize("@ss.hasPermi('risk:company:edit')")
    @Log(title = "企业管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiskCompany company) {
        return toAjax(companyService.updateRiskCompany(company));
    }

    /** 删除企业 */
    @PreAuthorize("@ss.hasPermi('risk:company:remove')")
    @Log(title = "企业管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(companyService.deleteRiskCompanyByIds(ids));
    }

    /** 触发风险扫描 */
    @PreAuthorize("@ss.hasPermi('risk:company:edit')")
    @Log(title = "风险扫描", businessType = BusinessType.UPDATE)
    @PostMapping("/scan/{id}")
    public AjaxResult scan(@PathVariable Long id) {
        companyService.scanCompanyRisk(id);
        return success("扫描完成");
    }

    /** 风险等级分布统计 */
    @PreAuthorize("@ss.hasPermi('risk:company:list')")
    @GetMapping("/stats/riskLevel")
    public AjaxResult riskLevelStats() {
        List<Map<String, Object>> stats = companyService.selectRiskLevelStats();
        return success(stats);
    }

    /** 高风险企业TOP榜 */
    @PreAuthorize("@ss.hasPermi('risk:company:list')")
    @GetMapping("/stats/highRisk")
    public AjaxResult highRiskCompanies(@RequestParam(defaultValue = "10") int limit) {
        List<RiskCompany> list = companyService.selectHighRiskCompanies(limit);
        return success(list);
    }

    /** 导出企业列表 */
    @PreAuthorize("@ss.hasPermi('risk:company:export')")
    @Log(title = "企业管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiskCompany company) {
        List<RiskCompany> list = companyService.selectRiskCompanyList(company);
        ExcelUtil<RiskCompany> util = new ExcelUtil<>(RiskCompany.class);
        util.exportExcel(response, list, "企业数据");
    }

    /** 批量导入企业 */
    @PreAuthorize("@ss.hasPermi('risk:company:add')")
    @Log(title = "企业导入", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public AjaxResult importData(@RequestParam("file") MultipartFile file) throws Exception {
        // TODO: parse Excel/CSV and insert companies
        return success("导入功能开发中");
    }
}
