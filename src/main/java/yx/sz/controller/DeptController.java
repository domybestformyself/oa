package yx.sz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yx.sz.Service.IdentityService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/identity/dept")
public class DeptController {
    /** 定义业务层对象 */

    @Resource // by type
    private IdentityService identityService;

    // 异步方法  写数据回去
    @ResponseBody
    @RequestMapping(value="/getAllDeptsAndJobsAjax",produces="application/json; charset=UTF-8")
    public Map<String, Object> getAllDeptsAndJobsAjax(){
        try {
            Thread.sleep(2000);
            Map<String, Object> rs = identityService.getAllDeptsAndJobsAjax();
            System.out.println("rs:"+rs);
            return rs ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
