package ac.cn.oauth.oauthcloud.config;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义认证
 *
 * @author lnk
 * @date 2021/7/13 18:30
 */
@Component
public class LoginValidateAuthenticationProvider implements AuthenticationProvider {

    @Resource
    @Lazy
    public PasswordEncoder passwordEncoder;

    //@Resource
    //private UserMapper userMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取输入的用户名
        String username = authentication.getName();
        //获取输入的明文
        String rawPassword = (String) authentication.getCredentials();


        // 线上环境应该通过用户名查询数据库获取加密后的密码
        String password = passwordEncoder.encode("123456");

        //验证密码
        if (!passwordEncoder.matches(rawPassword, password)) {
            throw new BadCredentialsException("输入密码错误!");
        }
        String role = "ROLE_USER";
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(username, rawPassword, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //确保authentication能转成该类
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
