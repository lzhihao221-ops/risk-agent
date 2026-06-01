package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.risk.service.CollateralOcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ocr/collateral")
public class CollateralOcrController extends BaseController {

    @Autowired
    private CollateralOcrService collateralOcrService;

    /**
     * 房产证识别
     */
    @PostMapping("/house")
    public AjaxResult recognizeHouse(@RequestParam("file") MultipartFile file) {
        try {
            return success(collateralOcrService.recognizeHouseCert(file));
        } catch (Exception e) {
            return error("识别失败: " + e.getMessage());
        }
    }

    /**
     * 土地证识别
     */
    @PostMapping("/land")
    public AjaxResult recognizeLand(@RequestParam("file") MultipartFile file) {
        try {
            return success(collateralOcrService.recognizeLandCert(file));
        } catch (Exception e) {
            return error("识别失败: " + e.getMessage());
        }
    }

    /**
     * 车辆登记证识别
     */
    @PostMapping("/vehicle")
    public AjaxResult recognizeVehicle(@RequestParam("file") MultipartFile file) {
        try {
            return success(collateralOcrService.recognizeVehicleCert(file));
        } catch (Exception e) {
            return error("识别失败: " + e.getMessage());
        }
    }

    /**
     * 营业执照识别
     */
    @PostMapping("/business")
    public AjaxResult recognizeBusiness(@RequestParam("file") MultipartFile file) {
        try {
            return success(collateralOcrService.recognizeBusinessLicense(file));
        } catch (Exception e) {
            return error("识别失败: " + e.getMessage());
        }
    }

    /**
     * 通用押品识别
     */
    @PostMapping("/general")
    public AjaxResult recognizeGeneral(@RequestParam("file") MultipartFile file,
                                       @RequestParam(defaultValue = "auto") String type) {
        try {
            return success(collateralOcrService.recognizeGeneral(file, type));
        } catch (Exception e) {
            return error("识别失败: " + e.getMessage());
        }
    }
}
