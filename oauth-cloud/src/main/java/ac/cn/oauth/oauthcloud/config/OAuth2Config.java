package ac.cn.oauth.oauthcloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {


    @Resource
    public PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private TokenStore redisTokenStore;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // authenticationManage() 调用此方法才能支持 password 模式。
        endpoints.authenticationManager(authenticationManager)
                // userDetailsService() 设置用户验证服务。
                //.userDetailsService(authUserDetailsService)
                // tokenStore() 指定 token 的存储方式。
                .tokenStore(redisTokenStore);

    }

    /**
     * oauth2 认证配置
     * 在本案中，采用的是inMemory 方式存储的，将配置保存到内存中，相当于硬编码了。正式环境下的做法是持久化到数据库中
     * authorizedGrantTypes 可以包括如下几种设置中的一种或多种：
     *      authorization_code：授权码类型。
     *      implicit：隐式授权类型。
     *      password：资源所有者（即用户）密码类型。
     *      client_credentials：客户端凭据（客户端ID以及Key）类型。
     *      refresh_token：通过以上授权获得的刷新令牌来获取新的令牌。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client_1")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("refresh_token", "authorization_code","client_credentials")
                // token 的有效期
                .accessTokenValiditySeconds(3600)
                // 用来限制客户端访问的权限，在换取的 token 的时候会带上 scope 参数，只有在 scopes 定义内的，才可以正常换取 token。
                .scopes("all")
                .and()
                .withClient("client_2")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("refresh_token", "authorization_code","password")
                .accessTokenValiditySeconds(3600)
                .scopes("all");
    }

    /**
     * 限制客户端访问认证接口的权限
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 第一行代码是允许客户端访问 OAuth2 授权接口，否则请求 token 会返回 401。
        security.allowFormAuthenticationForClients();
        // 分别是允许已授权用户访问 checkToken 接口和获取 token 接口。
        security.checkTokenAccess("permitAll()");
        security.tokenKeyAccess("isAuthenticated()");
    }

}
