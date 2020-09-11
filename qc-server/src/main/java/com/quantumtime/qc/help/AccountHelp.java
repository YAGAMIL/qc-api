package com.quantumtime.qc.help;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.constant.ThemeConstant;
import com.quantumtime.qc.common.enums.UserStateEnum;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.filter.GlobalRequestContextFilter;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.wrap.BaseInfo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountHelp {

  public void checkCurrentAccountStatusOfEnable() {
    User currentUser = getCurrentUser();
    if (!currentUser.getState().equals(UserStateEnum.ENABLE.getCode())) {
      throw new BizException(ErrorCodeConstant.ACCOUNT_STATUS_ERROR, new Throwable());
    }
  }

  public void checkCurrentAccountStatusOfRegist() {
    User currentUser = getCurrentUser();
    if (!currentUser.getState().equals(UserStateEnum.ENABLE.getCode())
        && !currentUser.getState().equals(UserStateEnum.UNVERIFIED_FIRST.getCode())
        && !currentUser.getState().equals(UserStateEnum.UNVERIFIED_SECOND.getCode())
        && !currentUser.getState().equals(UserStateEnum.UNVERIFIED_THIRD.getCode())) {
      throw new BizException(ErrorCodeConstant.ACCOUNT_STATUS_ERROR, new Throwable());
    }
  }

  public User getCurrentUser() {
    return Optional.ofNullable(GlobalRequestContextFilter.REQUEST_CONTEXT.get())
        .map(BaseInfo::getUser)
        .orElseThrow(
            () ->
                new BizException(ErrorCodeConstant.ACCOUNT_TOKEN_ERROR, new Throwable()));
  }

  public User getUserHolder() {
    BaseInfo baseInfo = GlobalRequestContextFilter.REQUEST_CONTEXT.get();
    if (baseInfo != null && baseInfo.getUser() != null) {
      return baseInfo.getUser();
    }
    return null;
  }

  public Integer getCurrentUserScope() {
    User currentUser = getCurrentUser();
    if (currentUser.getState().equals(UserStateEnum.ENABLE.getCode())) {
      return ThemeConstant.ALL;
    } else if (currentUser.getState().equals(UserStateEnum.UNVERIFIED_FIRST.getCode())
        || currentUser.getState().equals(UserStateEnum.UNVERIFIED_SECOND.getCode())
        || currentUser.getState().equals(UserStateEnum.UNVERIFIED_THIRD.getCode())) {
      return ThemeConstant.PUBLIC;
    } else {
      throw new BizException(ErrorCodeConstant.ACCOUNT_STATUS_ERROR, new Throwable());
    }
  }
}
