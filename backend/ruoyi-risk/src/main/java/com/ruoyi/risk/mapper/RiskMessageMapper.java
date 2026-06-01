package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.RiskMessage;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RiskMessageMapper {

    RiskMessage selectMessageById(Long id);

    List<RiskMessage> selectMessageList(RiskMessage message);

    List<RiskMessage> selectUnreadByUserId(Long userId);

    int countUnreadByUserId(Long userId);

    int insertMessage(RiskMessage message);

    int updateMessage(RiskMessage message);

    int markAsRead(Long id);

    int markAllAsRead(Long userId);

    int deleteMessageById(Long id);

    int deleteMessageByIds(Long[] ids);
}
