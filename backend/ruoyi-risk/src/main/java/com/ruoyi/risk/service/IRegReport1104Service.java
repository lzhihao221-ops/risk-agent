package com.ruoyi.risk.service;

import java.util.List;
import com.ruoyi.risk.domain.RegReport1104;

public interface IRegReport1104Service {
    public RegReport1104 selectById(Long id);
    public List<RegReport1104> selectList(RegReport1104 regReport1104);
    public int insert(RegReport1104 regReport1104);
    public int update(RegReport1104 regReport1104);
    public int deleteByIds(Long[] ids);
}
