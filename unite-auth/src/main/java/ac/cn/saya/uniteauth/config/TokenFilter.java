package ac.cn.saya.uniteauth.config;

import ac.cn.saya.uniteauth.tools.ParameterRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: TokenFilter
 * @ProjectName sso
 * @Description: TODO
 * @Author liunengkai
 * @Date: 7/17/21 11:18
 * @Description:
 */

@Component
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestPath = req.getRequestURI();
        if (requestPath.startsWith("/login.do")) {
            Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
            //修改请求参数
            parameterMap.put("grant_type",new String[]{"password"});
            ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(req, parameterMap);
            chain.doFilter(requestWrapper, response);
            return;
        }
        //如果该地址不需要拦截，走正常的流程
        chain.doFilter(request, response);
        return;
    }

}
