package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.LoanCreditQuery;
import com.ruoyi.risk.domain.LoanCreditApproval;
import com.ruoyi.risk.domain.LoanCreditLimit;
import com.ruoyi.risk.service.ILoanCreditQueryService;
import com.ruoyi.risk.service.ILoanCreditApprovalService;
import com.ruoyi.risk.service.ILoanCreditLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan/credit")
public class LoanCreditController extends BaseController {
    @Autowired
    private ILoanCreditQueryService queryService;
    @Autowired
    private ILoanCreditApprovalService approvalService;
    @Autowired
    private ILoanCreditLimitService limitService;

    // ==================== 征信查询 ====================

    @PreAuthorize("@ss.hasPermi('loan:credit:query:list')")
    @GetMapping("/query/list")
    public TableDataInfo queryList(LoanCreditQuery query) {
        startPage();
        List<LoanCreditQuery> list = queryService.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:query:query')")
    @GetMapping("/query/{id}")
    public AjaxResult queryGetInfo(@PathVariable Long id) {
        return success(queryService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:query:add')")
    @PostMapping("/query")
    public AjaxResult queryAdd(@RequestBody LoanCreditQuery record) {
        return toAjax(queryService.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:query:edit')")
    @PutMapping("/query")
    public AjaxResult queryEdit(@RequestBody LoanCreditQuery record) {
        return toAjax(queryService.update(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:query:remove')")
    @DeleteMapping("/query/{ids}")
    public AjaxResult queryRemove(@PathVariable Long[] ids) {
        return toAjax(queryService.deleteByIds(ids));
    }

    // ==================== 授信审批 ====================

    @PreAuthorize("@ss.hasPermi('loan:credit:approval:list')")
    @GetMapping("/approval/list")
    public TableDataInfo approvalList(LoanCreditApproval query) {
        startPage();
        List<LoanCreditApproval> list = approvalService.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:approval:query')")
    @GetMapping("/approval/{id}")
    public AjaxResult approvalGetInfo(@PathVariable Long id) {
        return success(approvalService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:approval:add')")
    @PostMapping("/approval")
    public AjaxResult approvalAdd(@RequestBody LoanCreditApproval record) {
        return toAjax(approvalService.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:approval:edit')")
    @PutMapping("/approval")
    public AjaxResult approvalEdit(@RequestBody LoanCreditApproval record) {
        return toAjax(approvalService.update(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:approval:remove')")
    @DeleteMapping("/approval/{ids}")
    public AjaxResult approvalRemove(@PathVariable Long[] ids) {
        return toAjax(approvalService.deleteByIds(ids));
    }

    // ==================== 授信额度 ====================

    @PreAuthorize("@ss.hasPermi('loan:credit:limit:list')")
    @GetMapping("/limit/list")
    public TableDataInfo limitList(LoanCreditLimit query) {
        startPage();
        List<LoanCreditLimit> list = limitService.selectList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:limit:query')")
    @GetMapping("/limit/{id}")
    public AjaxResult limitGetInfo(@PathVariable Long id) {
        return success(limitService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:limit:query')")
    @GetMapping("/limit/company/{companyId}")
    public AjaxResult limitGetByCompany(@PathVariable Long companyId) {
        return success(limitService.selectByCompanyId(companyId));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:limit:add')")
    @PostMapping("/limit")
    public AjaxResult limitAdd(@RequestBody LoanCreditLimit record) {
        return toAjax(limitService.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:limit:edit')")
    @PutMapping("/limit")
    public AjaxResult limitEdit(@RequestBody LoanCreditLimit record) {
        return toAjax(limitService.update(record));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:limit:remove')")
    @DeleteMapping("/limit/{ids}")
    public AjaxResult limitRemove(@PathVariable Long[] ids) {
        return toAjax(limitService.deleteByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('loan:credit:limit:edit')")
    @PutMapping("/limit/refresh/{id}")
    public AjaxResult limitRefresh(@PathVariable Long id) {
        return toAjax(limitService.refreshAvailableLimit(id));
    }
}
