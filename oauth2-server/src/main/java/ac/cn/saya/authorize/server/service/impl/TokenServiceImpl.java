package ac.cn.saya.authorize.server.service.impl;

import ac.cn.saya.authorize.server.constant.TokenConstant;
import ac.cn.saya.authorize.server.props.OAuth2ServerProperties;
import ac.cn.saya.authorize.server.repository.ClientDetailsRepository;
import ac.cn.saya.authorize.server.repository.UserInfoRepository;
import ac.cn.saya.authorize.server.service.TokenCacheService;
import ac.cn.saya.authorize.server.service.TokenService;
import ac.cn.saya.authorize.server.util.JwtTokenUtils;
import ac.cn.saya.authorize.common.domain.TokenValue;
import ac.cn.saya.authorize.common.enums.GrantType;
import ac.cn.saya.authorize.common.enums.OAuth2Error;
import ac.cn.saya.authorize.common.exception.OAuth2Exception;
import ac.cn.saya.authorize.common.token.OAuth2Token;
import ac.cn.saya.authorize.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * All rights Reserved, Designed By www.xaaef.com
 * <p>
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/13 10:01
 * @copyright 2021 http://www.xaaef.com Inc. All rights reserved.
 */


@Slf4j
@Service
public class TokenServiceImpl extends DefaultAuthorizeService implements TokenService {

    public TokenServiceImpl(ClientDetailsRepository clientDetailsRepository,
                            UserInfoRepository userInfoRepository,
                            TokenCacheService tokenCacheService,
                            JwtTokenUtils jwtTokenUtils) {
        super(clientDetailsRepository, userInfoRepository, tokenCacheService, jwtTokenUtils);
    }

    @Override
    public TokenValue validate(String bearerToken) throws OAuth2Exception {
        String tokenId = jwtTokenUtils.getIdFromToken(bearerToken);
        TokenValue tokenValue = tokenCacheService.getToken(tokenId);
        if (tokenValue == null || tokenValue.getClient() == null) {
            OAuth2ServerProperties props = jwtTokenUtils.getProps();
            // 判断是否启用单点登录
            if (props.getSso()) {
                String key = TokenConstant.FORCED_OFFLINE_KEY + tokenId;
                // 判断此用户，是不是被挤下线
                String offlineTime = tokenCacheService.getString(key);
                if (StringUtils.hasText(offlineTime)) {
                    // 删除 被挤下线 的消息提示
                    tokenCacheService.remove(key);
                    String errMsg = String.format("您的账号在[ %s ]被其他用户拥下线了！", offlineTime);
                    log.info("errMsg {}", errMsg);
                    throw new OAuth2Exception(errMsg);
                }
            }
            throw new OAuth2Exception("当前登录用户不存在!");
        }
        return tokenValue;
    }

    @Override
    public boolean urlIgnore(String url) {
        OAuth2ServerProperties props = jwtTokenUtils.getProps();
        return SecurityUtils.urlIgnore(props.getExcludePath(), url);
    }

    @Override
    public OAuth2Token refresh(String bearerToken) throws OAuth2Exception {
        String refreshTokenId = jwtTokenUtils.getIdFromToken(bearerToken);
        TokenValue refreshToken = tokenCacheService.getRefreshToken(refreshTokenId);
        if (refreshToken == null) {
            throw new OAuth2Exception(OAuth2Error.REFRESH_TOKEN_INVALID);
        }

        // 移除 旧的 refresh Token
        tokenCacheService.remove(TokenConstant.REFRESH_TOKEN_KEY + refreshTokenId);

        // 移除 旧的 Token
        tokenCacheService.remove(TokenConstant.LOGIN_TOKEN_KEY + refreshToken.getTokenId());

        // 生成 唯一的 认证id
        String tokenId = jwtTokenUtils.createTokenId();

        refreshToken.setTokenId(tokenId);

        setTokenCache(tokenId, refreshToken);

        var props = jwtTokenUtils.getProps();
        String scope = props.getScope();
        if (refreshToken.getGrantType() == GrantType.CLIENT_CREDENTIALS) {
            scope = "read";
        }

        OAuth2Token oAuth2Token = buildToken(tokenId);
        oAuth2Token.setScope(scope);
        return oAuth2Token;
    }


    @Override
    public void logout() {
        String tokenId = SecurityUtils.getTokenId();

        // 移除 Token
        tokenCacheService.remove(TokenConstant.LOGIN_TOKEN_KEY + tokenId);

        if (SecurityUtils.getTokenValue().getGrantType() != GrantType.CLIENT_CREDENTIALS) {
            if (SecurityUtils.getUserInfo() != null) {
                // 移除 在线用户
                tokenCacheService.remove(TokenConstant.ONLINE_USER_KEY + SecurityUtils.getUserInfo().getUsername());
            }
            // 移除 强制下线
            tokenCacheService.remove(TokenConstant.FORCED_OFFLINE_KEY + tokenId);
        }

        // 移除 刷新 token
        tokenCacheService.remove(TokenConstant.REFRESH_TOKEN_KEY + tokenId);

    }


}
