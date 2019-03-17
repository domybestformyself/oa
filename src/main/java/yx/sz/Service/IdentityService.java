package yx.sz.Service;

import org.springframework.stereotype.Service;
import yx.sz.dto.UserModule;
import yx.sz.pojo.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
@Service

public interface IdentityService {
    /**
     * 异步登录的业务层方法
     * @param params
     * @return
     */
    public Map<String, Object> login(Map<String, Object> params) ;


    List<UserModule> getUserPopedomModules();

    void updateSelf(User user, HttpSession session);
}
