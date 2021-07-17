package ac.cn.saya.uniteauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @Title: ResourceConfigure
 * @ProjectName sso
 * @Description: TODO
 * @Author liunengkai
 * @Date: 7/14/21 23:21
 * @Description:
 * https://item.jd.com/12765369.html#crumb-wrap
 * https://item.jd.com/12818876.html
 */

@Configuration
@EnableResourceServer
public class ResourceConfigure extends ResourceServerConfigurerAdapter {

    /**
     * 在这里如果以/public/**请求路径的，都允许直接访问。否则，都必须携带access_token才能访问。
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and().authorizeRequests().antMatchers("/login.do","/login.html").permitAll()
                .and().authorizeRequests().anyRequest().authenticated();
    }
}
