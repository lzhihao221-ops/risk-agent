package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.ClassificationResult;
import java.util.List;
import java.util.Map;

public interface ClassificationResultMapper {
    ClassificationResult selectById(Long resultId);
    List<ClassificationResult> selectList(ClassificationResult query);
    ClassificationResult selectLatestByLoan(Long loanId);
    List<Map<String, Object>> selectSummaryByDate(String classifyDate);
    int insert(ClassificationResult record);
    int deleteByIds(Long[] ids);
}
