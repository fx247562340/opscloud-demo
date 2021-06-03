package com.baiyi.opscloud.server;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;

/**
 * @Author baiyi
 * @Date 2020/4/1 3:40 下午
 * @Version 1.0
 */
public interface IServer {

    String getKey();

    void sync();

    BusinessWrapper<Boolean> disable(OcServer ocServer);

    BusinessWrapper<Boolean> enable(OcServer ocServer);

    /**
     * 创建
     *
     * @param ocServer
     * @return
     */
    BusinessWrapper<Boolean> create(OcServer ocServer);

    /**
     * 移除
     *
     * @param ocServer
     * @return
     */
    BusinessWrapper<Boolean> remove(OcServer ocServer);

    /**
     * 更新
     *
     * @param ocServer
     * @return
     */
    BusinessWrapper<Boolean> update(OcServer ocServer);
}
