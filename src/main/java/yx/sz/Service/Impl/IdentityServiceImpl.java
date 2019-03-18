package yx.sz.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import yx.sz.Repository.*;
import yx.sz.Service.IdentityService;
import yx.sz.common.util.pager.PageModel;
import yx.sz.dto.UserModule;
import yx.sz.pojo.Module;
import yx.sz.pojo.User;
import yx.sz.utils.OaContants;
import yx.sz.utils.OaException;
import yx.sz.utils.UserHolder;
import yx.sz.vo.TreeData;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.Map.Entry;

@Service("IdentityService")
@Transactional(readOnly = true)
public class IdentityServiceImpl implements IdentityService {
    // session
    /**
     * 定义Repository对象
     */
    @Autowired  // bytype
    @Qualifier("deptRepository") // byName
    private DeptRepository deptRepository;

    @Autowired  // bytype
    @Qualifier("jobRepository") // byName
    private JobRepository jobRepository;

    @Autowired  // bytype
    @Qualifier("userRepository") // byName
    private UserRepository userRepository;

    @Autowired  // bytype
    @Qualifier("moduleRepository") // byName
    private ModuleRepository moduleRepository;

    @Autowired  // bytype
    @Qualifier("roleRepository") // byName
    private RoleRepository roleRepository;

    @Autowired  // bytype
    @Qualifier("popedomRepository") // byName
    private PopedomRepository popedomRepository;

    @Override
    public Map<String, Object> login(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            /** 处理登录的业务逻辑   */
            /** 1.参数非空校验  */
            String userId = (String) params.get("userId");
            String passWord = (String) params.get("passWord");
            String vcode = (String) params.get("vcode");
            HttpSession session = (HttpSession) params.get("session");

            if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(passWord)
                    || StringUtils.isEmpty(vcode)) {
                /** 参数有为空的 */
                result.put("status", 0);
                result.put("tip", "参数有为空的");
            } else {
                /** 参数不为空  */
                /** 校验验证码是否正确
                 *  获取session中当前用户对应的验证码
                 * */


                /** 验证码正确了  */
                /** 根据登录的用户名去查询用户: 判断登录名是否存在  */
                User user = getUserById(userId);
                if (user != null) {
                    /** 登录名存在  */
                    /** 判断密码 */
                    if (user.getPassWord().equals(passWord)) {
                        /** 判断用户是否已经被激活了 */
                        if (user.getStatus() == 1) {
                            /** 登录成功  */
                            /** 1.把登录成功的用户放入当前用户的session会话中  */
                            session.setAttribute(OaContants.USER_SESSION, user);
                            System.out.println("设置用户 ---------------->：" + user);
                            result.put("status", 1);
                            result.put("tip", "登录成功");
                            /** 把登录成功的用户存入到UserHolder*/
                            UserHolder.addCurrentUser(user);
                            /** 2.当用户一登录进入系统的时候,就应该立即去查询该用户所拥有
                             的全部操作权限 --> 存入到当前用户的Session会话中  */
                            Map<String, List<String>> userAllOperasPopedomUrls = getUserAllOperasPopedomUrls();
                            session.setAttribute(OaContants.USER_ALL_OPERAS_POPEDOM_URLS, userAllOperasPopedomUrls);

                        } else {
                            result.put("status", 5);
                            result.put("tip", "您的账号未被激活,请联系管理员激活!");
                        }
                    } else {
                        /** 密码错误     */
                        result.put("status", 2);
                        result.put("tip", "密码错误了");
                    }
                } else {
                    /** 登录名不存在  */
                    result.put("status", 3);
                    result.put("tip", "没有该账户");
                }

            }
            return result;
        } catch (Exception e) {
            throw new OaException("异步登录业务层抛出异常了", e);
        }
    }

    @Transactional
    @Override
    public void updateSelf(User user, HttpSession session) {

        try {
            /** 1.持久化修改   */
            User sessionUser = userRepository.findById(user.getUserId()).get();
            sessionUser.setModifyDate(new Date());
            sessionUser.setModifier(user);
            sessionUser.setName(user.getName());
            sessionUser.setEmail(user.getEmail());
            sessionUser.setTel(user.getTel());
            sessionUser.setPhone(user.getPhone());
            sessionUser.setQuestion(user.getQuestion());
            sessionUser.setAnswer(user.getAnswer());
            sessionUser.setQqNum(user.getQqNum());
            // get一下就可以加载延迟加载的属性
            if (sessionUser.getDept() != null) sessionUser.getDept().getName();
            if (sessionUser.getJob() != null) sessionUser.getJob().getName();
            session.setAttribute(OaContants.USER_SESSION, sessionUser);
        } catch (Exception e) {
            throw new OaException("修改用户失败了", e);
        }

    }

    @SuppressWarnings("serial")
    @Override
    public List<User> getUsersByPager(User user, PageModel pageModel) {
        try {
            Page<User> usersPager = userRepository.findAll(new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query,
                                             CriteriaBuilder cb) {
                    // 本集合用于封装查询条件
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    if (user != null) {
                        /** 是否传入了姓名来查询  */
                        if (!StringUtils.isEmpty(user.getName())) {
                            predicates.add(cb.like(root.<String>get("name"), "%" + user.getName() + "%"));
                        }
                        /** 是否传入手机号码了来查询  */
                        if (!StringUtils.isEmpty(user.getPhone())) {
                            predicates.add(cb.like(root.<String>get("phone"), "%" + user.getPhone() + "%"));
                        }
                        /** 是否传入部门来查询  */
                        if (user.getDept() != null && user.getDept().getId() != null && user.getDept().getId() != 0) {
                            root.join("dept", JoinType.INNER);
                            Path<Long> d_id = root.get("dept").get("id");
                            predicates.add(cb.equal(d_id, user.getDept().getId()));

                        }
                        if (user.getJob() != null && !StringUtils.isEmpty(user.getJob().getCode())
                                && !user.getJob().getCode().equals("0")) {
                            root.join("job", JoinType.INNER);
                            Path<String> j_id = root.get("job").get("code");
                            predicates.add(cb.equal(j_id, user.getJob().getCode()));
                        }
                    }
                    return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
                }
            }, PageRequest.of(pageModel.getPageIndex() - 1, pageModel.getPageSize()));
            pageModel.setRecordCount(usersPager.getTotalElements());
            /** 取每个用户的延迟加载属性 */
            List<User> users = usersPager.getContent();
            for (User u : users) {
                if (u.getDept() != null) u.getDept().getName();
                if (u.getJob() != null) u.getJob().getName();
                if (u.getChecker() != null) u.getChecker().getName();
            }
            return users;
        } catch (Exception e) {
            throw new OaException("查询用户信息异常了", e);
        }
    }

    @Override
    public Map<String, Object> getAllDeptsAndJobsAjax() {
        try {
            /** 1.定义一个Map对象封装最终查询出来的部门信息和职位信息 */
            Map<String, Object> deptJobDatas = new HashMap<>();
            /** 查询部门 ： id name  */
            List<Map<String, Object>> deptsList = deptRepository.findDepts();

            /** 查询部门 ： id name  */
            List<Map<String, Object>> jobLists = jobRepository.findJobs();

            deptJobDatas.put("depts", deptsList);
            deptJobDatas.put("jobs", jobLists);

            return deptJobDatas;
        } catch (Exception e) {
            throw new OaException("查 询部门与职位信息异常了", e);
        }
    }

    @Override
    public List<Module> getModulesByParent(String parentCode, PageModel pageModel) {
        try {
            parentCode = parentCode == null ? "" : parentCode;
            List<Object> values = new ArrayList<>();
            values.add(parentCode + "%");
            values.add(parentCode.length() + OaContants.CODE_LEN);
//			// 子节点的编号的长度是父节点编号长度+步长
//		    // 子节点前几位的编号必须与父节点编码一致
            Page<Module> modulesPager = moduleRepository.findAll(new Specification<Module>() {

                @Override
                public Predicate toPredicate(Root<Module> root, CriteriaQuery<?> query,
                                             CriteriaBuilder cb) {
                    // 本集合用于封装查询条件
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    predicates.add(cb.like(root.<String>get("code"), values.get(0) + ""));
                    predicates.add(cb.equal(cb.length(root.<String>get("code")), values.get(1)));
                    return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
                }
            }, PageRequest.of(pageModel.getPageIndex() - 1, pageModel.getPageSize()));
            pageModel.setRecordCount(modulesPager.getTotalElements());
            /** 取每个用户的延迟加载属性 */
            List<Module> sonModules = modulesPager.getContent();
            for (Module m : sonModules) {
                if (m.getCreater() != null) m.getCreater().getName();
                if (m.getModifier() != null) m.getModifier().getName();
            }
            return sonModules;
        } catch (Exception e) {
            throw new OaException("查询子模块异常", e);

        }
    }

    /**
     * 加载菜单管理功能的tree
     *
     * @return
     */
    @Override
    public List<TreeData> loadAllModuleTrees() {
        try {
            /** 查询所有的模块信息   */
            List<Module> modules = moduleRepository.findAll();
            /** 拼装成dtree需要的树节点  */
            List<TreeData> treeDatas = new ArrayList<>();
            for (Module m : modules) {
                TreeData data = new TreeData();
                data.setId(m.getCode());
                data.setName(m.getName());
                // 长度为4的编号的父节点是0
                // 其余节点的父节点是从开始位置一直截取到总长度减去步长的位置。 00010001的父节点是0001
                String pid = m.getCode().length() == OaContants.CODE_LEN ? "0" : m.getCode().substring(0, m.getCode().length() - OaContants.CODE_LEN);
                data.setPid(pid);
                treeDatas.add(data);
            }
            return treeDatas;
        } catch (Exception e) {
            throw new OaException("加载模块树异常", e);
        }
    }

    @Override
    public void addModule(String parentCode, Module module) {
        try {
            /** 维护编号:通用工具类(给你一个父节点,给你一张表,给你那个字段,
             *  找出该字段该父节点下的下一个儿子节点的编号 ) */
            module.setCode(getNextSonCode(parentCode, OaContants.CODE_LEN));
            module.setCreateDate(new Date());
            module.setCreater(UserHolder.getCurrentUser());
            moduleRepository.save(module);
        } catch (Exception e) {
            throw new OaException("添加子菜单异常", e);
        }
    }

    private String getNextSonCode(String parentCode, int codeLen) {
        /** 判断父节点是否为null */
        parentCode =  parentCode==null?"":parentCode;
        /** 1.查询出当前父节点下的最大儿子节点编号 */
        String maxSonCode = moduleRepository.findUniqueEntity(parentCode+"%" , parentCode.length()+codeLen);
        String nextSonCode = ""; // 保存最终的下一个儿子节点编号
        /** 4.判断最大儿子节点编号是否存在 ,因为极有可能父节点此时一个子节点都没有 */
        if(StringUtils.isEmpty(maxSonCode)){
            /** 儿子节点编号不存在 */
            String preSuffix = "" ; // 0 需要拼接多少个0
            for(int i = 0 ; i < codeLen - 1; i++ ){
                preSuffix+="0";
            }
            nextSonCode = parentCode+preSuffix+1;
        }else{
            /** 儿子节点编号存在  */
            /** 截取出当前儿子节点编号的步长出来  */
            String currentMaxSonCode = maxSonCode.substring(parentCode.length());
            /** 得到儿子节点步长编号的整形形式   */
            int maxSonCodeInt = Integer.valueOf(currentMaxSonCode);
            maxSonCodeInt++;
            /** 判断编号是否越界了 */
            if((maxSonCodeInt+"").length() > codeLen){
                throw new OaException("编号越界了！");
            }else{
                /** 没有越界 */
                String preSuffix = "" ; // 0 需要拼接多少个0
                for(int i = 0 ; i< codeLen-(maxSonCodeInt+"").length() ; i++){
                    preSuffix+="0";
                }
                nextSonCode = parentCode+preSuffix+maxSonCodeInt;
            }
        }
        return nextSonCode;
    }


    public User getUserById(String userId) {
        try {
            User user = userRepository.findById(userId).get();
            if (user != null) {
                // 获取延迟加载的属性
                if (user.getDept() != null) user.getDept().getName();
                if (user.getJob() != null) user.getJob().getName();
                if (user.getCreater() != null) user.getCreater().getName();
                if (user.getModifier() != null) user.getModifier().getName();
                if (user.getChecker() != null) user.getChecker().getName();
                return user;
            }
            return null;
        } catch (Exception e) {
            throw new OaException("查询用户失败了", e);
        }
    }

    private Map<String, List<String>> getUserAllOperasPopedomUrls() {
        try {
            /** 查询用户所拥有的所有操作权限编号
             * */
            List<String> userAllPopedomOperasCodes = popedomRepository.getUserPopedomOperasCodes(UserHolder.getCurrentUser().getUserId());

            if (userAllPopedomOperasCodes != null && userAllPopedomOperasCodes.size() > 0) {
                Map<String, List<String>> userAllOperasPopedomUrls = new HashMap<>();
                String moduleUrl = "";
                List<String> moduleOperaUrls = null;
                for (String operaCode : userAllPopedomOperasCodes) {
                    /** 先得到模块的编号   */
                    String parentModuleCode = operaCode.substring(0, operaCode.length() - OaContants.CODE_LEN);
                    /** 父模块地址 */
                    moduleUrl = getModuleByCode(parentModuleCode).getUrl();
                    /** 判断map集合中是否已经存在该父模块地址 */
                    if (!userAllOperasPopedomUrls.containsKey(moduleUrl)) {
                        moduleOperaUrls = new ArrayList<String>();
                        userAllOperasPopedomUrls.put(moduleUrl, moduleOperaUrls);
                    }
                    moduleOperaUrls.add(getModuleByCode(operaCode).getUrl());
                }
                return userAllOperasPopedomUrls;
            }
            return null;
        } catch (Exception e) {
            throw new OaException("登录查询用户的操作权限出现异常", e);
        }

    }

    public Module getModuleByCode(String code) {
        try {
            return moduleRepository.findById(code).get();
        } catch (Exception e) {
            throw new OaException("查询模块异常", e);
        }
    }

    public List<UserModule> getUserPopedomModules() {
        try {
            /**查询当前用户的权限模块 ：先查用户所有的角色,再查这些角色拥有的所有权限模块  */
            List<String> popedomModuleCodes = popedomRepository.getUserPopedomModuleCodes("admin");
            if (popedomModuleCodes != null && popedomModuleCodes.size() > 0) {

                /** 定义一个Map集合用于保存用户的权限模块
                 *  Map<Module,List<Module>>
                 *  {系统管理=[用户管理,角色管理] , 假期模块=[查询信息,用户请假]}
                 *  */
                Map<Module, List<Module>> userModulesMap = new LinkedHashMap<>();
                Module fistModule = null;
                List<Module> secondModules = null;
                for (String moduleCode : popedomModuleCodes) {
                    /** 截取当前模块的一级模块编号 */
                    String fistCode = moduleCode.substring(0, OaContants.CODE_LEN);
                    /** 查询出一级模块对象 */
                    fistModule = getModuleByCode(fistCode);
                    fistModule.setName(fistModule.getName().replaceAll("-", ""));
                    /**如果map集合中没有包含当前一级模块的key,说明是第一次添加一级模块 */
                    if (!userModulesMap.containsKey(fistModule)) {
                        secondModules = new ArrayList<Module>();
                        userModulesMap.put(fistModule, secondModules);
                    }
                    Module secondModule = getModuleByCode(moduleCode);
                    secondModule.setName(secondModule.getName().replaceAll("-", ""));
                    secondModules.add(secondModule);
                }

                List<UserModule> userModules = new ArrayList<>();
                for (Entry<Module, List<Module>> entry : userModulesMap.entrySet()) {
                    Module key = entry.getKey();
                    List<Module> value = entry.getValue();
                    UserModule userModule = new UserModule();
                    userModule.setFirstModule(key);
                    userModule.setSecondModules(value);
                    userModules.add(userModule);
                }
                return userModules;

            }
            return null;

        } catch (Exception e) {
            throw new OaException("查询当前用户的权限模块", e);
        }
    }
}
