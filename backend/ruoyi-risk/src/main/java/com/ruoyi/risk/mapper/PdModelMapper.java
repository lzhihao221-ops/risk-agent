package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.PdModel;
import java.util.List;

public interface PdModelMapper {
    PdModel selectById(Long modelId);
    List<PdModel> selectList(PdModel query);
    List<PdModel> selectEnabled();
    int insert(PdModel record);
    int update(PdModel record);
    int deleteByIds(Long[] ids);
}
