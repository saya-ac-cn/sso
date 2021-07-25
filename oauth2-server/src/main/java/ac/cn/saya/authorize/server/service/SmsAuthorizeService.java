package ac.cn.saya.authorize.server.service;

import ac.cn.saya.authorize.server.params.SmsModeParam;
import ac.cn.saya.authorize.common.domain.UserInfo;
import ac.cn.saya.authorize.common.exception.OAuth2Exception;
import ac.cn.saya.authorize.common.token.OAuth2Token;

/**
 * All rights Reserved, Designed By www.xaaef.com
 * <p>
 * 手机短信验证码 授权服务
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/12 14:21
 * @copyright 2021 http://www.xaaef.com Inc. All rights reserved.
 */

public interface SmsAuthorizeService {

    /**
     * 发送短信
     *
     * @param clientId
     * @param mobile
     * @author Wang Chen Chen
     * @date 2021/7/12 14:23
     */
    String sendSms(String clientId, String mobile) throws OAuth2Exception;

    /**
     * 校验 短信验证码
     *
     * @param mobile
     * @param code
     * @author Wang Chen Chen
     * @date 2021/7/12 14:23
     */
    UserInfo validateSmsCode(String mobile, String code) throws OAuth2Exception;

    /**
     * 短信验证码 认证模式
     *
     * @param param
     * @return OAuth2Token
     * @throws OAuth2Exception
     * @author Wang Chen Chen<932560435@qq.com>
     * @date 2020/7/23 16:48
     */
    OAuth2Token authorize(SmsModeParam param) throws OAuth2Exception;

}
