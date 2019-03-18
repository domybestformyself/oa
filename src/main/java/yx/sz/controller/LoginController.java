package yx.sz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yx.sz.Service.IdentityService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    /** 1.定义业务层对象 */
    @Resource // by type
    private IdentityService identityService;

    @RequestMapping("/")
    String index() {
        return "login";
    }

    @RequestMapping(value="/loginAjax",produces="application/json; charset=UTF-8")
    @ResponseBody// 异步请求的响应结果
    Map<String, Object> login(@RequestParam("userId") String userId,
                              @RequestParam("passWord") String passWord,
                              @RequestParam("vcode") String vcode, HttpSession session) {
       try {
           Map<String, Object> params = new HashMap<>();
           params.put("userId", userId);
           params.put("passWord", passWord);
           params.put("vcode", vcode);
           params.put("session", session);
           //获得数据
           Map<String, Object> results = identityService.login(params);
           return results;
       }catch (Exception e){
           e.printStackTrace();
       }
       return  null;
    }
}
