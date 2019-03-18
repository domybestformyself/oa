package yx.sz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import yx.sz.Service.IdentityService;
import yx.sz.common.util.pager.PageModel;
import yx.sz.pojo.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/identity/user")
public class UserController {

    /** 1.定义业务层对象 */
    @Resource // by type
    private IdentityService identityService;
    @RequestMapping(value="/updateSelf")
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

    @RequestMapping(value="/selectUser")
    public String selectUser(User user , HttpServletRequest request, PageModel pageModel, Model model){
        try {
            /** 1.自己处理 ：只需要处理get请求的参数
             *   post请求的参数不会乱码
             *  */
            if(request.getMethod().toLowerCase().indexOf("get")!=-1){
                if(user!=null && !StringUtils.isEmpty(user.getName())){
                    String name = user.getName();
                    /**
                     * 浏览器到后台乱码
                     * */
                    name = new String(name.getBytes("ISO-8859-1") , "UTF-8");
                    user.setName(name);
                }
            }
            List<User> users = identityService.getUsersByPager(user,pageModel);
            model.addAttribute("users", users);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "identity/user/user";
    }

}
