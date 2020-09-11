package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.entity.undo.RelUserAndTheme;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.RelUserAndThemeRepository;
import com.quantumtime.qc.service.IRelUserAndThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelUserAndThemeServiceImpl extends BaseServiceImpl<RelUserAndTheme, Long, RelUserAndThemeRepository> implements IRelUserAndThemeService {

    @Autowired
    private AccountHelp accountHelp;

    @Override
    public Boolean joinTheme(Long themeId) {
        User currentUser = accountHelp.getCurrentUser();
        RelUserAndTheme userAndTheme = new RelUserAndTheme();
        userAndTheme.setAddressId(currentUser.getAddressId());
        userAndTheme.setThemeId(themeId);
        userAndTheme.setUserId(currentUser.getUid());
        this.save(userAndTheme);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unjoinTheme(Long themeId) {
        User currentUser = accountHelp.getCurrentUser();
        this.baseRepository.deleteByThemeIdAndUserIdAndAddressId(themeId, currentUser.getUid(), currentUser.getAddressId());
        return true;
    }

    @Override
    public RelUserAndTheme findRelByUserIdAndThemeId(String userId, Long themeId) {
        RelUserAndTheme relUserAndTheme = this.baseRepository.findByUserIdAndThemeId(userId, themeId);
        return relUserAndTheme;
    }


}
