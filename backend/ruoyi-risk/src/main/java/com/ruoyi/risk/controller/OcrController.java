package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.risk.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ocr")
public class OcrController extends BaseController {

    @Autowired
    private OcrService ocrService;

    /**
     * 图片OCR识别
     */
    @PostMapping("/image")
    public AjaxResult imageOcr(@RequestParam("file") MultipartFile file) {
        try {
            String result = ocrService.recognizeImage(file);
            return success(result);
        } catch (Exception e) {
            return error("识别失败: " + e.getMessage());
        }
    }

    /**
     * 报表识别
     */
    @PostMapping("/report")
    public AjaxResult reportOcr(@RequestParam("file") MultipartFile file) {
        try {
            String result = ocrService.recognizeFinancialReport(file);
            return success(result);
        } catch (Exception e) {
            return error("识别失败: " + e.getMessage());
        }
    }
}
