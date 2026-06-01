package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.PdScoreRecord;
import java.util.List;

public interface PdScoreRecordMapper {
    PdScoreRecord selectById(Long scoreId);
    List<PdScoreRecord> selectList(PdScoreRecord query);
    int insert(PdScoreRecord record);
    int deleteByIds(Long[] ids);
}
