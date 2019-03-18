package yx.sz.Service;

import org.springframework.stereotype.Service;
import yx.sz.common.util.pager.PageModel;
import yx.sz.dto.UserModule;
import yx.sz.pojo.Module;
import yx.sz.pojo.User;
import yx.sz.vo.TreeData;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
@Service

public interface IdentityService {

    public Map<String, Object> login(Map<String, Object> params) ;


    List<UserModule> getUserPopedomModules();

    void updateSelf(User user, HttpSession session);

    List<User> getUsersByPager(User user, PageModel pageModel);

    Map<String, Object> getAllDeptsAndJobsAjax();

    List<Module> getModulesByParent(String parentCode, PageModel pageModel);

    List<TreeData> loadAllModuleTrees();

    void addModule(String parentCode, Module module);
}
