package com.ruoyi.risk.controller;

import java.math.BigDecimal;
import java.util.List;
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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.LoanAssetPreservation;
import com.ruoyi.risk.service.ILoanAssetPreservationService;

/**
 * 资产保全Controller
 */
@RestController
@RequestMapping("/loan/asset")
public class LoanAssetController extends BaseController
{
    @Autowired
    private ILoanAssetPreservationService loanAssetPreservationService;

    /**
     * 查询资产保全列表
     */
    @PreAuthorize("@ss.hasPermi('loan:asset:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanAssetPreservation loanAssetPreservation)
    {
        startPage();
        List<LoanAssetPreservation> list = loanAssetPreservationService.selectList(loanAssetPreservation);
        return getDataTable(list);
    }

    /**
     * 获取资产保全详细信息
     */
    @PreAuthorize("@ss.hasPermi('loan:asset:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(loanAssetPreservationService.selectById(id));
    }

    /**
     * 新增资产保全
     */
    @PreAuthorize("@ss.hasPermi('loan:asset:add')")
    @PostMapping
    public AjaxResult add(@RequestBody LoanAssetPreservation loanAssetPreservation)
    {
        return toAjax(loanAssetPreservationService.insert(loanAssetPreservation));
    }

    /**
     * 修改资产保全
     */
    @PreAuthorize("@ss.hasPermi('loan:asset:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody LoanAssetPreservation loanAssetPreservation)
    {
        return toAjax(loanAssetPreservationService.update(loanAssetPreservation));
    }

    /**
     * 删除资产保全
     */
    @PreAuthorize("@ss.hasPermi('loan:asset:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(loanAssetPreservationService.deleteByIds(ids));
    }

    /**
     * 更新回收金额
     */
    @PreAuthorize("@ss.hasPermi('loan:asset:edit')")
    @GetMapping("/recovery/{id}")
    public AjaxResult updateRecovery(@PathVariable Long id, BigDecimal recoveryAmount)
    {
        return toAjax(loanAssetPreservationService.updateRecoveryAmount(id, recoveryAmount));
    }
}
