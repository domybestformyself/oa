package yx.sz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import yx.sz.Service.IdentityService;
import yx.sz.pojo.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/identity/user")
public class UserController {

    /** 1.定义业务层对象 */
    @Resource // by type
    private IdentityService identityService;
    @RequestMapping("/updateSelf")
    public String updateSelf(User user, Model model, HttpSession session) {
        try {
            identityService.updateSelf(user, session);
            model.addAttribute("tip", "修改成功！");
        } catch (Exception e) {
            model.addAttribute("tip", "修改失败！");
            e.printStackTrace();
        }
        return "home";
    }
}
