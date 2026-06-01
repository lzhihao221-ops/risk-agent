package com.ruoyi.risk.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AiAssistantService {

    private static final Logger log = LoggerFactory.getLogger(AiAssistantService.class);
    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";
    private static final String MODEL_NAME = "qwen3.6:27b";
    
    // 会话历史（内存存储，生产环境建议用Redis）
    private final Map<String, List<JSONObject>> sessionHistory = new ConcurrentHashMap<>();
    
    // 风控系统提示词
    private static final String SYSTEM_PROMPT = "你是一个专业的银行风控助手，叫做风控智脑。你精通以下领域：" +
            "【风险评估方法论】" +
            "1. 信用风险评估：运用定性与定量相结合的方法，包括5C分析法（品格Character、能力Capacity、资本Capital、担保Collateral、条件Condition）、Z-Score模型、KMV模型等 " +
            "2. 市场风险评估：运用VaR(风险价值)、CVaR(条件风险价值)、久期分析、敏感性分析等方法 " +
            "3. 操作风险评估：采用损失分布法(LDA)、情景分析法、关键风险指标(KRI)监控 " +
            "4. 流动性风险：监控LCR(流动性覆盖率)、NSFR(净稳定资金比率)、流动性缺口等指标 " +
            "【Basel III框架知识】" +
            "1. 资本充足率要求：核心一级资本充足率≥5%、一级资本充足率≥6%、总资本充足率≥8%，并满足资本缓冲要求 " +
            "2. 杠杆率要求：不低于3% " +
            "3. 流动性指标：LCR≥100%、NSFR≥100% " +
            "4. 大额风险暴露：对单一交易对手的风险暴露不超过一级资本净额的25% " +
            "【不良贷款(NPL)管理最佳实践】" +
            "1. 贷前阶段：严格准入标准、完善尽职调查、合理定价覆盖风险 " +
            "2. 贷中阶段：动态风险监测、预警信号识别、贷款重组与展期管理 " +
            "3. 贷后阶段：分类催收策略、不良资产转让、核销管理、以物抵债处理 " +
            "4. 指标管理：不良贷款率控制目标、拨备覆盖率≥150%、贷款拨备率≥2.5% " +
            "【监管合规（银保监会CBIRC/国家金融监督管理总局规则）】" +
            "1. 贷款分类管理：严格执行《贷款风险分类指引》，真实反映资产质量 " +
            "2. 资本管理：遵守《商业银行资本管理办法》，满足最低资本要求 " +
            "3. 风险管理：落实全面风险管理要求，建立三道防线体系 " +
            "4. 信息披露：按时报送监管报表，确保数据真实性、准确性、完整性 " +
            "5. 关联交易：遵守关联交易管理规定，防止利益输送 " +
            "【贷款五级分类标准】" +
            "1. 正常：借款人能正常还本付息，不存在影响还款的不利因素 " +
            "2. 关注：尽管借款人目前有能力偿还，但存在潜在不利因素 " +
            "3. 次级：借款人还款能力出现明显问题，依靠正常经营收入无法足额偿还 " +
            "4. 可疑：借款人无法足额偿还本息，即使执行担保也会造成较大损失 " +
            "5. 损失：在采取所有可能措施后，本息仍无法收回或只能收回极少部分 " +
            "回答要求：简洁专业，使用银行术语，结合具体数据给出可操作的建议，引用相关监管规定时注明来源，中文回答";

    /**
     * 智能对话
     */
    public String chat(String sessionId, String userMessage) {
        List<JSONObject> history = sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
        
        // 添加用户消息
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        history.add(userMsg);
        
        // 保持历史记录在合理范围（最近20轮）
        if (history.size() > 40) {
            history = new ArrayList<>(history.subList(history.size() - 40, history.size()));
            sessionHistory.put(sessionId, history);
        }
        
        // 构建请求
        JSONObject request = new JSONObject();
        request.put("model", MODEL_NAME);
        request.put("stream", false);
        
        JSONArray messages = new JSONArray();
        
        // 系统提示
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.add(systemMsg);
        
        // 历史消息
        messages.addAll(history);
        
        request.put("messages", messages);
        
        // 调用Ollama
        String response = callOllama(request);
        
        // 保存助手回复
        JSONObject assistantMsg = new JSONObject();
        assistantMsg.put("role", "assistant");
        assistantMsg.put("content", response);
        history.add(assistantMsg);
        
        return response;
    }

    /**
     * 风险分析
     */
    public String analyzeRisk(Map<String, Object> params) {
        String prompt = String.format("""
                请分析以下贷款的风险情况：
                - 企业名称：%s
                - 贷款金额：%s万元
                - 贷款期限：%s个月
                - 资产负债率：%s%%
                - 流动比率：%s
                - 逾期天数：%s天
                - 五级分类：%s
                
                请给出：
                1. 风险等级评估（高/中/低）
                2. 主要风险点分析
                3. 风险防控建议""",
                params.getOrDefault("companyName", "未知"),
                params.getOrDefault("amount", "0"),
                params.getOrDefault("period", "0"),
                params.getOrDefault("debtRatio", "0"),
                params.getOrDefault("currentRatio", "0"),
                params.getOrDefault("overdueDays", "0"),
                params.getOrDefault("classification", "未知")
        );
        
        return callOllama(buildRequest(prompt));
    }

    /**
     * 生成风险报告
     */
    public String generateReport(Map<String, Object> params) {
        String prompt = String.format("""
                请生成一份简要的风险分析报告：
                - 报告类型：%s
                - 统计周期：%s
                - 贷款总额：%s亿元
                - 不良贷款率：%s%%
                - 关注类贷款占比：%s%%
                - 资本充足率：%s%%
                
                请包含以下内容：
                1. 执行摘要
                2. 风险概况
                3. 主要风险点
                4. 风险趋势
                5. 政策建议""",
                params.getOrDefault("reportType", "月度风险报告"),
                params.getOrDefault("period", "2026年5月"),
                params.getOrDefault("totalLoan", "0"),
                params.getOrDefault("nplRatio", "0"),
                params.getOrDefault("watchRatio", "0"),
                params.getOrDefault("capitalRatio", "0")
        );
        
        return callOllama(buildRequest(prompt));
    }

    /**
     * 催收建议
     */
    public String suggestCollection(Map<String, Object> params) {
        String prompt = String.format("""
                请为以下逾期贷款提供催收策略建议：
                - 企业名称：%s
                - 逾期金额：%s万元
                - 逾期天数：%s天
                - 五级分类：%s
                - 历史催收次数：%s次
                - 上次催收结果：%s
                
                请给出：
                1. 催收优先级（紧急/重要/一般）
                2. 推荐催收方式
                3. 催收话术建议
                4. 法律措施建议""",
                params.getOrDefault("companyName", "未知"),
                params.getOrDefault("overdueAmount", "0"),
                params.getOrDefault("overdueDays", "0"),
                params.getOrDefault("classification", "未知"),
                params.getOrDefault("collectionCount", "0"),
                params.getOrDefault("lastResult", "无")
        );
        
        return callOllama(buildRequest(prompt));
    }

    /**
     * 异常检测
     */
    public String detectAnomaly(Map<String, Object> params) {
        String prompt = String.format("""
                请分析以下数据是否存在异常模式：
                - 交易笔数：%s笔
                - 交易总额：%s万元
                - 平均单笔金额：%s万元
                - 最大单笔金额：%s万元
                - 夜间交易占比：%s%%
                - 关联交易占比：%s%%
                - 同比变化：%s%%
                
                请判断：
                1. 是否存在异常（是/否）
                2. 异常类型（如有）
                3. 风险提示
                4. 建议措施""",
                params.getOrDefault("transactionCount", "0"),
                params.getOrDefault("totalAmount", "0"),
                params.getOrDefault("avgAmount", "0"),
                params.getOrDefault("maxAmount", "0"),
                params.getOrDefault("nightRatio", "0"),
                params.getOrDefault("relatedRatio", "0"),
                params.getOrDefault("yoyChange", "0")
        );
        
        return callOllama(buildRequest(prompt));
    }

    /**
     * 构建请求
     */
    private JSONObject buildRequest(String prompt) {
        JSONObject request = new JSONObject();
        request.put("model", MODEL_NAME);
        request.put("stream", false);
        
        JSONArray messages = new JSONArray();
        
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.add(systemMsg);
        
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.add(userMsg);
        
        request.put("messages", messages);
        return request;
    }

    /**
     * 调用Ollama API
     */
    private String callOllama(JSONObject request) {
        try {
            URL url = new URL(OLLAMA_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(300000); // 5分钟超时，适应27B模型推理速度
            
            // 发送请求
            try (OutputStream os = conn.getOutputStream()) {
                os.write(request.toJSONString().getBytes());
            }
            
            // 读取响应
            int code = conn.getResponseCode();
            if (code != 200) {
                log.error("Ollama调用失败，状态码: {}", code);
                return "AI服务暂时不可用，请稍后再试。";
            }
            
            byte[] responseBytes = conn.getInputStream().readAllBytes();
            String responseBody = new String(responseBytes, "UTF-8");
            log.info("Ollama响应: {}", responseBody.substring(0, Math.min(200, responseBody.length())));
            JSONObject response = JSON.parseObject(responseBody);
            
            String content = response.getJSONObject("message").getString("content");
            return content != null ? content : "AI未能生成回复";
            
        } catch (java.net.ConnectException e) {
            log.error("Ollama服务未启动或不可达", e);
            return "⚠️ AI服务连接失败：无法连接到Ollama服务。\n\n请检查以下事项：\n" +
                    "1. 确认Ollama服务已启动（运行 `ollama serve`）\n" +
                    "2. 确认模型已下载（运行 `ollama pull qwen3.6:27b`）\n" +
                    "3. 确认服务地址正确：http://localhost:11434\n" +
                    "4. 检查防火墙是否放行11434端口\n\n" +
                    "技术详情：" + e.getMessage();
        } catch (java.net.SocketTimeoutException e) {
            log.error("Ollama请求超时", e);
            return "⚠️ AI响应超时：模型推理时间过长。\n\n" +
                    "当前使用 qwen3.6:27b 模型，首次推理可能较慢。\n" +
                    "建议：等待片刻后重试，或联系管理员调整模型配置。\n\n" +
                    "技术详情：" + e.getMessage();
        } catch (Exception e) {
            log.error("调用Ollama失败", e);
            return "⚠️ AI服务异常：" + e.getMessage() + "\n请稍后重试或联系管理员。";
        }
    }
}
