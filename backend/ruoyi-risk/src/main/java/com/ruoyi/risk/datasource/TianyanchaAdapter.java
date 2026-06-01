package com.ruoyi.risk.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 天眼查数据源适配器
 * API文档: https://open.tianyancha.com/
 * 需要注册获取 API Token
 */
@Component
public class TianyanchaAdapter implements DataSourceAdapter {

    private static final Logger log = LoggerFactory.getLogger(TianyanchaAdapter.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${risk.datasource.tianyancha.token:}")
    private String apiToken;

    @Value("${risk.datasource.tianyancha.base-url:https://open.tianyancha.com/services/open}")
    private String baseUrl;

    @Override
    public String getName() { return "天眼查"; }

    @Override
    public boolean isAvailable() {
        return apiToken != null && !apiToken.isEmpty();
    }

    @Override
    public Map<String, Object> queryCompanyInfo(String keyword) {
        return callApi("/ic/baseinfo/normal", Map.of("keyword", keyword));
    }

    @Override
    public Map<String, Object> queryExecutionInfo(String companyName) {
        return callApi("/ic/zhixing/info", Map.of("keyword", companyName));
    }

    @Override
    public Map<String, Object> queryDishonestInfo(String companyName) {
        return callApi("/ic/shixin/info", Map.of("keyword", companyName));
    }

    @Override
    public Map<String, Object> queryAbnormalInfo(String companyName) {
        return callApi("/ic/abnormal/info", Map.of("keyword", companyName));
    }

    @Override
    public Map<String, Object> queryLawsuitInfo(String companyName) {
        return callApi("/ic/lawsuit/info", Map.of("keyword", companyName));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> callApi(String path, Map<String, String> params) {
        if (!isAvailable()) {
            return Map.of("error", "天眼查 API Token 未配置");
        }
        try {
            StringBuilder urlBuilder = new StringBuilder(baseUrl + path + "?");
            params.forEach((k, v) -> urlBuilder.append(k).append("=").append(v).append("&"));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .header("Authorization", apiToken)
                .header("Content-Type", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("天眼查 API 响应: {} - {}", path, response.statusCode());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), Map.class);
            }
            return Map.of("error", "API返回 " + response.statusCode());
        } catch (Exception e) {
            log.error("天眼查 API 调用失败: {}", e.getMessage());
            return Map.of("error", e.getMessage());
        }
    }
}
