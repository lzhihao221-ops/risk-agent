package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.risk.service.GisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gis")
public class GisController extends BaseController {

    @Autowired
    private GisService gisService;

    /**
     * 获取所有网点位置
     */
    @GetMapping("/branches")
    public AjaxResult getBranches() {
        List<Map<String, Object>> branches = gisService.getBranchLocations();
        return success(branches);
    }

    /**
     * 获取客户分布
     */
    @GetMapping("/customers")
    public AjaxResult getCustomerDistribution() {
        List<Map<String, Object>> customers = gisService.getCustomerDistribution();
        return success(customers);
    }

    /**
     * 获取风险热力图数据
     */
    @GetMapping("/risk-heatmap")
    public AjaxResult getRiskHeatmap() {
        List<Map<String, Object>> heatmap = gisService.getRiskHeatmapData();
        return success(heatmap);
    }

    /**
     * 外勤打卡
     */
    @PostMapping("/checkin")
    public AjaxResult checkin(@RequestBody Map<String, Object> params) {
        try {
            String result = gisService.checkin(params);
            return success(result);
        } catch (Exception e) {
            return error("打卡失败: " + e.getMessage());
        }
    }

    /**
     * 获取打卡记录
     */
    @GetMapping("/checkin/records")
    public AjaxResult getCheckinRecords(@RequestParam(required = false) String userId,
                                         @RequestParam(required = false) String startDate,
                                         @RequestParam(required = false) String endDate) {
        List<Map<String, Object>> records = gisService.getCheckinRecords(userId, startDate, endDate);
        return success(records);
    }
}
