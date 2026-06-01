package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.CapAdequacy;
import java.util.List;

public interface CapAdequacyMapper {
    CapAdequacy selectById(Long id);
    List<CapAdequacy> selectList(CapAdequacy query);
    CapAdequacy selectLatest();
    int insert(CapAdequacy record);
    int update(CapAdequacy record);
    int deleteByIds(Long[] ids);
}
