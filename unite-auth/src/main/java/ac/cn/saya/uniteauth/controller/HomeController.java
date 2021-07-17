package ac.cn.saya.uniteauth.controller;

import ac.cn.saya.uniteauth.tools.ParameterRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: HomeController
 * @ProjectName sso
 * @Description: TODO
 * @Author liunengkai
 * @Date: 7/13/21 22:51
 * @Description:
 */
@Controller
public class HomeController {

    @GetMapping("/login.html")
    public String login(){
        return "login";
    }

    @PostMapping("/login.do")
    public void login(HttpServletRequest request,HttpServletResponse response) throws Exception{
//        Map paramter = new HashMap(4);
//        paramter.put("grant_type", "password");
//        HttpServletRequest req = (HttpServletRequest) request;
//        request = new ParameterRequestWrapper(req, paramter);

        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String[]> parameterMap = request.getParameterMap();

        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location","/oauth/token");
    }

    @GetMapping("/api/user")
    @ResponseBody
    public String home(){
        return "home";
    }



}
