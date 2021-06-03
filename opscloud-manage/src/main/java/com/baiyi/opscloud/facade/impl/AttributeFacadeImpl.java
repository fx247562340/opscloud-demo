package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.PreviewAttributeVO;
import com.baiyi.opscloud.facade.AttributeFacade;
import com.baiyi.opscloud.factory.attribute.impl.AnsibleAttribute;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.ansible.config.AnsibleConfig.ANSIBLE_HOSTS;

/**
 * @Author baiyi
 * @Date 2020/4/10 11:28 上午
 * @Version 1.0
 */
@Service
public class AttributeFacadeImpl implements AttributeFacade {

    @Resource
    private AnsibleAttribute ansibleAttribute;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private AnsibleConfig ansibleConfig;

    @Override
    public void createAnsibleHostsTask() {
        try {
            String context = ansibleAttribute.getHeadInfo();
            List<OcServerGroup> serverGroupList = ocServerGroupService.queryAll();
            for (OcServerGroup serverGroup : serverGroupList) {
                PreviewAttributeVO.PreviewAttribute previewAttribute = ansibleAttribute.build(serverGroup);
                if (!StringUtils.isEmpty(previewAttribute.getContent()))
                    context += "\n" + previewAttribute.getContent();
            }
            IOUtils.createFile(ansibleConfig.acqInventoryPath(), ANSIBLE_HOSTS, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
