package ac.cn.saya.authorize.server.service;

import ac.cn.saya.authorize.server.params.ClientModeParam;
import ac.cn.saya.authorize.common.exception.OAuth2Exception;
import ac.cn.saya.authorize.common.token.OAuth2Token;

/**
 * All rights Reserved, Designed By www.xaaef.com
 * <p>
 * 客户端授权服务
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/12 14:21
 * @copyright 2021 http://www.xaaef.com Inc. All rights reserved.
 */

public interface ClientAuthorizeService {

    /**
     * 客户端认证模式
     *
     * @param param
     * @return OAuth2Token
     * @throws OAuth2Exception
     * @author Wang Chen Chen<932560435@qq.com>
     * @date 2020/7/23 16:48
     */
    OAuth2Token authorize(ClientModeParam param) throws OAuth2Exception;

}
