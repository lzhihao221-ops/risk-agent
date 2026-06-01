package com.ruoyi.risk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@Service
public class OcrService {

    private static final Logger log = LoggerFactory.getLogger(OcrService.class);

    /**
     * 图片OCR识别 - 使用Python pytesseract
     */
    public String recognizeImage(MultipartFile file) throws Exception {
        // 验证文件
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名无效");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("仅支持图片文件（JPG、PNG等）");
        }
        
        // 检查文件大小（最大10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }
        
        // 保存临时文件
        File tempFile = File.createTempFile("ocr_", ".png");
        file.transferTo(tempFile);
        
        try {
            log.info("开始OCR识别，文件：{}，大小：{}字节", originalFilename, file.getSize());
            
            // 使用Python pytesseract进行OCR
            String pythonScript = 
                "import pytesseract\n" +
                "from PIL import Image\n" +
                "import sys\n" +
                "\n" +
                "try:\n" +
                "    img = Image.open('" + tempFile.getAbsolutePath().replace("\\", "\\\\") + "')\n" +
                "    text = pytesseract.image_to_string(img, lang='chi_sim+eng')\n" +
                "    print(text)\n" +
                "except Exception as e:\n" +
                "    print(f'OCR_ERROR: {str(e)}', file=sys.stderr)\n" +
                "    sys.exit(1)\n";
            
            ProcessBuilder pb = new ProcessBuilder("python3", "-c", pythonScript);
            pb.redirectErrorStream(true);
            pb.redirectErrorStream(false); // 分离stderr
            Process process = pb.start();
            
            // 读取标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            
            // 读取错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                String errorMsg = errorOutput.toString().trim();
                log.error("OCR处理失败，退出码：{}，错误：{}", exitCode, errorMsg);
                
                if (errorMsg.contains("OCR_ERROR")) {
                    throw new RuntimeException("OCR识别失败：" + errorMsg.replace("OCR_ERROR: ", ""));
                } else if (errorMsg.contains("No module named 'pytesseract'")) {
                    throw new RuntimeException("OCR服务未就绪：缺少Python依赖，请联系管理员安装pytesseract");
                } else if (errorMsg.contains("tesseract is not installed")) {
                    throw new RuntimeException("OCR服务未就绪：缺少Tesseract引擎，请联系管理员安装Tesseract");
                } else {
                    throw new RuntimeException("OCR处理失败，错误码：" + exitCode);
                }
            }
            
            String ocrResult = result.toString().trim();
            if (ocrResult.isEmpty()) {
                return "（未识别到文字内容）";
            }
            
            log.info("OCR识别完成，识别字符数：{}", ocrResult.length());
            return ocrResult;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("OCR处理被中断", e);
        } finally {
            // 清理临时文件
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /**
     * 身份证识别
     */
    public String recognizeIdCard(MultipartFile file) throws Exception {
        String rawText = recognizeImage(file);
        // 简单的身份证信息提取
        StringBuilder result = new StringBuilder();
        result.append("=== 身份证识别结果 ===\n\n");
        result.append("原始识别文本：\n").append(rawText).append("\n\n");
        result.append("提示：如需更精确的身份证识别，建议使用专业OCR服务。");
        return result.toString();
    }

    /**
     * 银行卡识别
     */
    public String recognizeBankCard(MultipartFile file) throws Exception {
        String rawText = recognizeImage(file);
        StringBuilder result = new StringBuilder();
        result.append("=== 银行卡识别结果 ===\n\n");
        result.append("原始识别文本：\n").append(rawText).append("\n\n");
        result.append("提示：如需更精确的银行卡识别，建议使用专业OCR服务。");
        return result.toString();
    }

    /**
     * 发票识别
     */
    public String recognizeInvoice(MultipartFile file) throws Exception {
        String rawText = recognizeImage(file);
        StringBuilder result = new StringBuilder();
        result.append("=== 发票识别结果 ===\n\n");
        result.append("原始识别文本：\n").append(rawText).append("\n\n");
        result.append("提示：如需更精确的发票识别，建议使用专业OCR服务。");
        return result.toString();
    }

    /**
     * 财务报表识别
     */
    public String recognizeFinancialReport(MultipartFile file) throws Exception {
        String rawText = recognizeImage(file);
        StringBuilder result = new StringBuilder();
        result.append("=== 财务报表识别结果 ===\n\n");
        result.append("原始识别文本：\n").append(rawText).append("\n\n");
        result.append("提示：如需更精确的财务报表识别，建议使用专业OCR服务。");
        return result.toString();
    }
}
