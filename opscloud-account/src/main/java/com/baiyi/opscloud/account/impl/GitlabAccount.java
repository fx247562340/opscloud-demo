package com.baiyi.opscloud.account.impl;

import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.builder.AccountBuilder;
import com.baiyi.opscloud.account.builder.UserBuilder;
import com.baiyi.opscloud.common.base.AccountType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserCredential;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.gitlab.GitlabUserCenter;
import com.baiyi.opscloud.gitlab.handler.GitlabUserHandler;
import com.baiyi.opscloud.ldap.config.LdapConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/22 11:20 上午
 * @Version 1.0
 */
@Slf4j
@Component("GitlabAccount")
public class GitlabAccount extends BaseAccount implements IAccount {

    @Resource
    private GitlabUserHandler gitlabUserHandler;

    @Resource
    private GitlabUserCenter gitlabUserCenter;

    @Resource
    private LdapConfig ldapConfig;

    @Override
    protected List<OcUser> getUserList() {
        return gitlabUserHandler.getUsers().stream().map(UserBuilder::build).collect(Collectors.toList());
    }

    @Override
    protected List<OcAccount> getOcAccountList() {
        return gitlabUserHandler.getUsers().stream().map(AccountBuilder::build).collect(Collectors.toList());
    }

    @Override
    protected int getAccountType() {
        return AccountType.GITLAB.getType();
    }

    /**
     * 推送用户公钥 PubKey
     *
     * @param ocUser
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> pushSSHKey(OcUser ocUser) {
        OcUserCredential credential = getOcUserSSHPubKey(ocUser);
        if (credential == null) return new BusinessWrapper<>(ErrorEnum.USER_CREDENTIAL_NOT_EXIST);
        OcAccount ocAccount = getAccount(ocUser);
        if (ocAccount == null)
            return new BusinessWrapper<>(ErrorEnum.ACCOUNT_NOT_EXIST);
        boolean result = gitlabUserCenter.pushKey(ocUser, ocAccount, BeanCopierUtils.copyProperties(credential, UserCredentialVO.UserCredential.class));
        if (result) {
            ocAccount.setSshKey(1);
            ocAccountService.updateOcAccount(ocAccount);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.SYSTEM_ERROR);
    }


    /**
     * 创建
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> create(OcUser user) {
        if (gitlabUserCenter.createUser(user, ldapConfig.buildUserDN(user.getUsername())) != null) {
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.ACCOUNT_CREATE_ERROR);
    }

    /**
     * 移除
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> delete(OcUser user) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> update(OcUser user) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> grant(OcUser user, String resource) {
        return update(user);
    }

    @Override
    public BusinessWrapper<Boolean> revoke(OcUser user, String resource) {
        return update(user);
    }

    @Override
    public BusinessWrapper<Boolean> active(OcUser user, boolean active) {
        return BusinessWrapper.SUCCESS;
    }
}
