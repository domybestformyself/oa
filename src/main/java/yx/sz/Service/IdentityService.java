package yx.sz.Service;

import org.springframework.stereotype.Service;
import yx.sz.common.util.pager.PageModel;
import yx.sz.dto.UserModule;
import yx.sz.pojo.Module;
import yx.sz.pojo.Role;
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

    Module getModuleByCode(String code);

    void updateModule(Module module);

    void deleteModules(String ids);

    List<Role> getRoleByPager(PageModel pageModel);

    void addRole(Role role);

    Role getRoleById(Long id);

    void updateRole(Role role);

    List<User> selectNotRoleUser(Role role, PageModel pageModel);

    List<User> selectRoleUser(Role role, PageModel pageModel);

    void bindUser(Role role, String ids);

    void unBindUser(Role role, String ids);

    void deleteUserByUserIds(String ids);

    void addUser(User user);

    String isUserValidAjax(String userId);

    void updateUser(User user);

    void activeUser(User user);

    User getUserById(String userId);

    List<Module> getModulesByParent(String parentCode);

    List<String> getRoleModuleOperasCodes(Role role, String parentCode);

    void bindPopedom(String codes, Role role, String parentCode);
}
