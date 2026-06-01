package com.ruoyi.risk.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.PdModel;
import com.ruoyi.risk.domain.PdScoreRecord;
import com.ruoyi.risk.mapper.PdModelMapper;
import com.ruoyi.risk.mapper.PdScoreRecordMapper;
import com.ruoyi.risk.service.PdCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/pd/model")
public class PdModelController extends BaseController {

    @Autowired
    private PdModelMapper pdModelMapper;
    @Autowired
    private PdScoreRecordMapper pdScoreRecordMapper;
    @Autowired
    private PdCalculationService pdCalculationService;

    @PreAuthorize("@ss.hasPermi('pd:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(PdModel query) {
        startPage();
        return getDataTable(pdModelMapper.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('pd:model:query')")
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable Long id) {
        return success(pdModelMapper.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('pd:model:add')")
    @PostMapping
    public AjaxResult add(@RequestBody PdModel record) {
        return toAjax(pdModelMapper.insert(record));
    }

    @PreAuthorize("@ss.hasPermi('pd:model:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody PdModel record) {
        return toAjax(pdModelMapper.update(record));
    }

    @PreAuthorize("@ss.hasPermi('pd:model:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pdModelMapper.deleteByIds(ids));
    }

    /** 启用模型 */
    @PreAuthorize("@ss.hasPermi('pd:model:edit')")
    @PostMapping("/{id}/activate")
    public AjaxResult activate(@PathVariable Long id) {
        PdModel model = pdModelMapper.selectById(id);
        if (model == null) return error("模型不存在");
        model.setStatus("1");
        pdModelMapper.update(model);
        return success("已启用");
    }

    /** 单个企业评分 */
    @PreAuthorize("@ss.hasPermi('pd:model:score')")
    @PostMapping("/score")
    public AjaxResult score(@RequestBody JSONObject params) {
        Long modelId = params.getLong("modelId");
        Long companyId = params.getLong("companyId");
        String companyName = params.getString("companyName");
        Long loanId = params.getLong("loanId");
        Map<String, Double> variables = new HashMap<>();
        JSONObject vars = params.getJSONObject("variables");
        if (vars != null) {
            for (String key : vars.keySet()) {
                variables.put(key, vars.getDoubleValue(key));
            }
        }
        PdScoreRecord record = pdCalculationService.scoreCompany(modelId, companyId, companyName, loanId, variables);
        return success(record);
    }

    /** 评分记录查询 */
    @PreAuthorize("@ss.hasPermi('pd:model:list')")
    @GetMapping("/score/record")
    public TableDataInfo scoreRecord(PdScoreRecord query) {
        startPage();
        return getDataTable(pdScoreRecordMapper.selectList(query));
    }
}
