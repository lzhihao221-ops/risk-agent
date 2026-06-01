package com.ruoyi.risk.service;

import java.util.List;
import com.ruoyi.risk.domain.RegIndicator;

public interface IRegIndicatorService {
    public RegIndicator selectById(Long id);
    public List<RegIndicator> selectList(RegIndicator regIndicator);
    public List<RegIndicator> selectByType(String indicatorType);
    public int insert(RegIndicator regIndicator);
    public int update(RegIndicator regIndicator);
    public int deleteByIds(Long[] ids);
}
