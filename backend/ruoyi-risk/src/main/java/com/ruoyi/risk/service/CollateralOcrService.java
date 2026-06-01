package com.ruoyi.risk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;
import java.util.regex.*;

@Service
public class CollateralOcrService {

    private static final Logger log = LoggerFactory.getLogger(CollateralOcrService.class);

    /**
     * 房产证识别
     */
    public Map<String, Object> recognizeHouseCert(MultipartFile file) throws Exception {
        String rawText = doOcr(file);
        Map<String, Object> result = new HashMap<>();
        result.put("type", "房产证");
        result.put("rawText", rawText);
        
        // 提取关键信息
        result.put("ownerName", extractField(rawText, "所有权人|权利人|姓名", "所有人"));
        result.put("propertyNo", extractField(rawText, "房产证号|不动产权第|权证号", "证号"));
        result.put("location", extractField(rawText, "坐落|房屋坐落|地址", "地址"));
        result.put("area", extractField(rawText, "建筑面积|面积", "面积"));
        result.put("usage", extractField(rawText, "用途|房屋用途", "用途"));
        result.put("rightType", extractField(rawText, "权利类型|性质", "性质"));
        
        return result;
    }

    /**
     * 土地证识别
     */
    public Map<String, Object> recognizeLandCert(MultipartFile file) throws Exception {
        String rawText = doOcr(file);
        Map<String, Object> result = new HashMap<>();
        result.put("type", "土地证");
        result.put("rawText", rawText);
        
        result.put("ownerName", extractField(rawText, "使用权人|权利人|姓名", "所有人"));
        result.put("landNo", extractField(rawText, "土地证号|地号", "证号"));
        result.put("location", extractField(rawText, "坐落|土地坐落|地址", "地址"));
        result.put("area", extractField(rawText, "使用权面积|面积", "面积"));
        result.put("usage", extractField(rawText, "用途|土地用途", "用途"));
        result.put("rightType", extractField(rawText, "权利类型|性质", "性质"));
        
        return result;
    }

    /**
     * 车辆登记证识别
     */
    public Map<String, Object> recognizeVehicleCert(MultipartFile file) throws Exception {
        String rawText = doOcr(file);
        Map<String, Object> result = new HashMap<>();
        result.put("type", "车辆登记证");
        result.put("rawText", rawText);
        
        result.put("ownerName", extractField(rawText, "所有人|车主|姓名", "车主"));
        result.put("plateNo", extractField(rawText, "号牌号码|车牌号|车牌", "车牌"));
        result.put("vin", extractField(rawText, "车辆识别代号|VIN|车架号", "VIN"));
        result.put("engineNo", extractField(rawText, "发动机号|引擎号", "发动机号"));
        result.put("brand", extractField(rawText, "品牌|车辆品牌|厂牌", "品牌"));
        result.put("model", extractField(rawText, "车辆型号|型号", "型号"));
        
        return result;
    }

    /**
     * 营业执照识别
     */
    public Map<String, Object> recognizeBusinessLicense(MultipartFile file) throws Exception {
        String rawText = doOcr(file);
        Map<String, Object> result = new HashMap<>();
        result.put("type", "营业执照");
        result.put("rawText", rawText);
        
        result.put("companyName", extractField(rawText, "名称|企业名称|公司名称", "公司名"));
        result.put("creditCode", extractField(rawText, "统一社会信用代码|信用代码", "信用代码"));
        result.put("legalPerson", extractField(rawText, "法定代表人|负责人|法人", "法人"));
        result.put("registeredCapital", extractField(rawText, "注册资本|注册资金", "注册资本"));
        result.put("establishDate", extractField(rawText, "成立日期|注册日期", "成立日期"));
        result.put("businessScope", extractField(rawText, "经营范围", "经营范围"));
        
        return result;
    }

    /**
     * 通用押品识别
     */
    public Map<String, Object> recognizeGeneral(MultipartFile file, String type) throws Exception {
        if ("auto".equals(type)) {
            // 自动识别类型
            type = autoDetectType(file);
        }
        
        switch (type) {
            case "house": return recognizeHouseCert(file);
            case "land": return recognizeLandCert(file);
            case "vehicle": return recognizeVehicleCert(file);
            case "business": return recognizeBusinessLicense(file);
            default:
                String rawText = doOcr(file);
                Map<String, Object> result = new HashMap<>();
                result.put("type", "通用文档");
                result.put("rawText", rawText);
                return result;
        }
    }

    /**
     * 执行OCR识别 - 使用Python pytesseract
     */
    private String doOcr(MultipartFile file) throws Exception {
        File tempFile = File.createTempFile("collateral_ocr_", ".png");
        file.transferTo(tempFile);
        
        try {
            // 使用Python pytesseract进行OCR
            String pythonScript = 
                "import pytesseract\n" +
                "from PIL import Image\n" +
                "import sys\n" +
                "\n" +
                "img = Image.open('" + tempFile.getAbsolutePath().replace("\\", "\\\\") + "')\n" +
                "text = pytesseract.image_to_string(img, lang='chi_sim+eng')\n" +
                "print(text)\n";
            
            ProcessBuilder pb = new ProcessBuilder("python3", "-c", pythonScript);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("OCR处理失败，退出码: " + exitCode);
            }
            
            return result.toString().trim();
        } finally {
            tempFile.delete();
        }
    }

    /**
     * 提取字段
     */
    private String extractField(String text, String pattern, String defaultValue) {
        Pattern p = Pattern.compile(pattern + "[：:]*\\s*([\\u4e00-\\u9fa5a-zA-Z0-9]+)");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1).trim();
        }
        return defaultValue;
    }

    /**
     * 自动检测文档类型
     */
    private String autoDetectType(MultipartFile file) {
        String filename = file.getOriginalFilename().toLowerCase();
        if (filename.contains("房产") || filename.contains("不动产权")) {
            return "house";
        } else if (filename.contains("土地") || filename.contains("国土")) {
            return "land";
        } else if (filename.contains("车辆") || filename.contains("行驶证") || filename.contains("登记证")) {
            return "vehicle";
        } else if (filename.contains("营业") || filename.contains("执照")) {
            return "business";
        }
        return "general";
    }
}
