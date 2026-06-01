package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.risk.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiAssistantController extends BaseController {

    @Autowired
    private AiAssistantService aiAssistantService;

    /**
     * 智能风控助手 - 对话接口
     */
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody Map<String, String> params) {
        String message = params.get("message");
        String sessionId = params.getOrDefault("sessionId", "default");
        
        if (message == null || message.trim().isEmpty()) {
            return error("请输入消息");
        }
        
        try {
            String response = aiAssistantService.chat(sessionId, message);
            return success(response);
        } catch (Exception e) {
            return error("AI服务异常: " + e.getMessage());
        }
    }

    /**
     * 风险分析 - 分析贷款风险
     */
    @PostMapping("/analyze/risk")
    public AjaxResult analyzeRisk(@RequestBody Map<String, Object> params) {
        try {
            String analysis = aiAssistantService.analyzeRisk(params);
            return success(analysis);
        } catch (Exception e) {
            return error("分析失败: " + e.getMessage());
        }
    }

    /**
     * 智能报告 - 生成风险报告
     */
    @PostMapping("/report/generate")
    public AjaxResult generateReport(@RequestBody Map<String, Object> params) {
        try {
            String report = aiAssistantService.generateReport(params);
            return success(report);
        } catch (Exception e) {
            return error("报告生成失败: " + e.getMessage());
        }
    }

    /**
     * 催收建议 - 生成催收策略
     */
    @PostMapping("/collection/suggest")
    public AjaxResult suggestCollection(@RequestBody Map<String, Object> params) {
        try {
            String suggestion = aiAssistantService.suggestCollection(params);
            return success(suggestion);
        } catch (Exception e) {
            return error("建议生成失败: " + e.getMessage());
        }
    }

    /**
     * 异常检测 - 检测异常模式
     */
    @PostMapping("/anomaly/detect")
    public AjaxResult detectAnomaly(@RequestBody Map<String, Object> params) {
        try {
            String result = aiAssistantService.detectAnomaly(params);
            return success(result);
        } catch (Exception e) {
            return error("检测失败: " + e.getMessage());
        }
    }
}
