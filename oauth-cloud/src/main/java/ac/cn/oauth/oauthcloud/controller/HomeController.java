package ac.cn.oauth.oauthcloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Title: HomeController
 * @ProjectName sso
 * @Description: TODO
 * @Author liunengkai
 * @Date: 7/13/21 22:51
 * @Description:
 */
@Controller
@RequestMapping("/public")
public class HomeController {

    @GetMapping("/login.html")
    public String login(){
        return "login";
    }

    @GetMapping("/home")
    @ResponseBody
    public String home(){
        return "home";
    }

    @GetMapping("/root")
    @ResponseBody
    public String root(){
        return "root";
    }

    @PostMapping("/login.do")
    public String forwardLogin(){
        return "forward:/login";
    }

}
