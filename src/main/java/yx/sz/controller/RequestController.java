package yx.sz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import yx.sz.Service.IdentityService;
import yx.sz.dto.UserModule;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/oa")
public class RequestController {

    /** 1.定义业务层对象 */
    @Resource // by type
    private IdentityService identityService;

    @RequestMapping(value="/main")
    public String requestMain(Model model){
        try {
            //查询出当前用户拥有的所有模块权限
            List<UserModule> userModules = identityService.getUserPopedomModules();
            model.addAttribute("userPopedomModules", userModules);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "main";
    }
    @RequestMapping(value="/home")
    public String requestHome(){
        return "home";
    }

    @RequestMapping(value="/login")
    public String requestLogin(){
        System.out.println("登录成功了！");
        return "login";
    }
}
