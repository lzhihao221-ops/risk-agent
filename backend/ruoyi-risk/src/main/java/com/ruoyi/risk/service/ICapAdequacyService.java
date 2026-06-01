package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.CapAdequacy;
import java.util.List;

public interface ICapAdequacyService {
    CapAdequacy selectById(Long id);
    List<CapAdequacy> selectList(CapAdequacy query);
    int insert(CapAdequacy record);
    int update(CapAdequacy record);
    int deleteByIds(Long[] ids);
    CapAdequacy selectLatest();
}
