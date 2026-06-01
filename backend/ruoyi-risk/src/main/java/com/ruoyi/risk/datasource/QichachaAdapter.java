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
import java.util.Map;

/**
 * 企查查数据源适配器
 * API文档: https://open.qichacha.com/
 */
@Component
public class QichachaAdapter implements DataSourceAdapter {

    private static final Logger log = LoggerFactory.getLogger(QichachaAdapter.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${risk.datasource.qichacha.token:}")
    private String apiToken;

    @Value("${risk.datasource.qichacha.base-url:https://api.qichacha.com}")
    private String baseUrl;

    @Override
    public String getName() { return "企查查"; }

    @Override
    public boolean isAvailable() {
        return apiToken != null && !apiToken.isEmpty();
    }

    @Override
    public Map<String, Object> queryCompanyInfo(String keyword) {
        return callApi("/company/search", Map.of("keyword", keyword));
    }

    @Override
    public Map<String, Object> queryExecutionInfo(String companyName) {
        return callApi("/company/zhixing", Map.of("keyword", companyName));
    }

    @Override
    public Map<String, Object> queryDishonestInfo(String companyName) {
        return callApi("/company/shixin", Map.of("keyword", companyName));
    }

    @Override
    public Map<String, Object> queryAbnormalInfo(String companyName) {
        return callApi("/company/abnormal", Map.of("keyword", companyName));
    }

    @Override
    public Map<String, Object> queryLawsuitInfo(String companyName) {
        return callApi("/company/lawsuit", Map.of("keyword", companyName));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> callApi(String path, Map<String, String> params) {
        if (!isAvailable()) {
            return Map.of("error", "企查查 API Token 未配置");
        }
        try {
            StringBuilder urlBuilder = new StringBuilder(baseUrl + path + "?");
            params.forEach((k, v) -> urlBuilder.append(k).append("=").append(v).append("&"));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .header("token", apiToken)
                .header("Content-Type", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("企查查 API 响应: {} - {}", path, response.statusCode());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), Map.class);
            }
            return Map.of("error", "API返回 " + response.statusCode());
        } catch (Exception e) {
            log.error("企查查 API 调用失败: {}", e.getMessage());
            return Map.of("error", e.getMessage());
        }
    }
}
