package yx.sz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import yx.sz.Service.IdentityService;
import yx.sz.common.util.pager.PageModel;
import yx.sz.pojo.Role;
import yx.sz.pojo.User;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/identity/role")
public class RoleController {
    /**
     * 1.定义业务层对象
     */
    @Resource // by type
    private IdentityService identityService;

    @RequestMapping("/selectRole")
    public String selectRole(PageModel pageModel, Model model) {
        try {
            List<Role> roles = identityService.getRoleByPager(pageModel);
            model.addAttribute("roles", roles);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "identity/role/role";
    }


    @RequestMapping(value = "/addRole")
    public String addRole(Role role, Model model) {
        try {
            identityService.addRole(role);
            model.addAttribute("tip", "添加成功");
        } catch (Exception e) {
            model.addAttribute("tip", "添加失败");
            e.printStackTrace();
        }
        return "identity/role/addRole";
    }

    @RequestMapping(value = "/showAddRole")
    public String showAddRole() {
        return "identity/role/addRole";
    }

    @RequestMapping(value = "/showUpdateRole")
    public String showUpdateRole(Role role, Model model) {
        try {
            role = identityService.getRoleById(role.getId());
            model.addAttribute("role", role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "identity/role/updateRole";
    }

    @RequestMapping(value = "/updateRole")
    public String updateRole(Role role, Model model) {
        try {
            identityService.updateRole(role);
            model.addAttribute("tip", "修改成功");
        } catch (Exception e) {
            model.addAttribute("tip", "修改失败");
            e.printStackTrace();
        }
        return "identity/role/updateRole";
    }

    @RequestMapping(value = "/selectRoleUser")
    public String selectRoleUser(Role role, PageModel pageModel, Model model) {
        try {
            /** 查询不属于这个角色下的所有用户 */
            List<User> users =identityService.selectRoleUser(role,pageModel);
            role = identityService.getRoleById(role.getId());
            model.addAttribute("users", users);
            model.addAttribute("role", role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "identity/role/bindUser/roleUser";
    }

    @RequestMapping(value="/showBindUser")
    public String selectNotRoleUser(Role role ,PageModel pageModel, Model model){
        try {
            /** 查询不属于这个角色下的所有用户 */
            List<User> users = identityService.selectNotRoleUser(role,pageModel);
            role = identityService.getRoleById(role.getId());
            model.addAttribute("users", users);
            model.addAttribute("role", role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "identity/role/bindUser/bindUser";
    }

    @RequestMapping(value="/bindUser")
    public String bindUser(Role role,String ids, Model model){
        try {
            identityService.bindUser(role,ids);
            model.addAttribute("tip","绑定成功");
        } catch (Exception e) {
            model.addAttribute("tip","绑定失败");
            e.printStackTrace();
        }
        return "forward:/identity/role/showBindUser";
    }
    @RequestMapping(value="/unBindUser")
    public String unBindUser(Role role,String ids, Model model){
        try {
            identityService.unBindUser(role,ids);
            model.addAttribute("tip","解绑成功");
        } catch (Exception e) {
            model.addAttribute("tip","解绑失败");
            e.printStackTrace();
        }
        return "forward:/identity/role/selectRoleUser";
    }

}
