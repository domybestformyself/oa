package yx.sz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yx.sz.Service.IdentityService;
import yx.sz.common.util.pager.PageModel;
import yx.sz.pojo.Module;
import yx.sz.vo.TreeData;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/identity/module")
public class ModuleController {
    @Resource // by type
    private IdentityService identityService;

    @RequestMapping(value="/mgrModule")
    public String mgrModule(){
        return "identity/module/mgrModule";
    }

    @RequestMapping(value="/getModulesByParent")
    public String getModulesByParent(String parentCode, PageModel pageModel, Model model){
       try {
            List<Module> sonModules = identityService.getModulesByParent(parentCode,pageModel);
            model.addAttribute("modules", sonModules);
            model.addAttribute("parentCode", parentCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "identity/module/module";
    }

    @RequestMapping(value="/loadAllModuleTrees",produces="application/json; charset=UTF-8")
    @ResponseBody
    public List<TreeData>loadAllModuleTrees(){
        try {
            return identityService.loadAllModuleTrees();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value="/showAddModule")
    public String showAddModule(String parentCode,Model model){
       model.addAttribute("parentCode", parentCode);
        return "identity/module/addModule";
    }

    @RequestMapping(value="/addModule")
    public String addModule(String parentCode,Module module ,Model model){
        try {
            identityService.addModule(parentCode,module);
            model.addAttribute("tip", "添加成功");
            model.addAttribute("parentCode", parentCode);
        } catch (Exception e) {
            model.addAttribute("tip", "添加失败");
            e.printStackTrace();
        }
        return "identity/module/addModule";
    }




}
