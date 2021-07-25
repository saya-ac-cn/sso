package ac.cn.saya.authorize.server.service.impl;

import ac.cn.saya.authorize.server.params.PasswordModeParam;
import ac.cn.saya.authorize.server.repository.ClientDetailsRepository;
import ac.cn.saya.authorize.server.repository.UserInfoRepository;
import ac.cn.saya.authorize.server.service.PasswordAuthorizeService;
import ac.cn.saya.authorize.server.service.TokenCacheService;
import ac.cn.saya.authorize.server.util.JwtTokenUtils;
import ac.cn.saya.authorize.common.domain.ClientDetails;
import ac.cn.saya.authorize.common.domain.TokenValue;
import ac.cn.saya.authorize.common.domain.UserInfo;
import ac.cn.saya.authorize.common.enums.GrantType;
import ac.cn.saya.authorize.common.exception.OAuth2Exception;
import ac.cn.saya.authorize.common.token.OAuth2Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * All rights Reserved, Designed By www.xaaef.com
 * <p>
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/12 16:15
 * @copyright 2021 http://www.xaaef.com Inc. All rights reserved.
 */

@Slf4j
@Service
public class PasswordAuthorizeServiceImpl extends DefaultAuthorizeService implements PasswordAuthorizeService {

    public PasswordAuthorizeServiceImpl(ClientDetailsRepository clientDetailsRepository,
                                        UserInfoRepository userInfoRepository,
                                        TokenCacheService tokenCacheService,
                                        JwtTokenUtils jwtTokenUtils) {
        super(clientDetailsRepository, userInfoRepository, tokenCacheService, jwtTokenUtils);
    }

    @Override
    public OAuth2Token authorize(PasswordModeParam param) throws OAuth2Exception {
        ClientDetails client = validateClient(
                param.getClientId(),
                param.getClientSecret(),
                GrantType.PASSWORD,
                param.getGrantType()
        );

        UserInfo userInfo = validateUserInfo(param.getUsername(), param.getPassword());

        // 生成 唯一的 认证id
        String tokenId = jwtTokenUtils.createTokenId();

        // 服务端 token
        TokenValue tokenValue = TokenValue.builder()
                .tokenId(tokenId)
                .client(client)
                .user(userInfo)
                .grantType(GrantType.PASSWORD)
                .build();

        setTokenCache(tokenId, tokenValue);

        return buildToken(tokenId);
    }

}
