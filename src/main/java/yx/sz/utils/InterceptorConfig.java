package yx.sz.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author dlei(徐磊)
 * @Tel  18665616520
 * @Email dlei0009@163.com
 * @QQ 251425887
 * @Date 2017年10月28日下午2:19:30
 * @From http://www.fkjava.org 疯狂软件
 * @Version 1.0
 * 加入拦截器的配置 ：用于拦截用户的请求，判断是否登录或者判断是否拥有了权限
 * 如果拥有了权限就进行请求的放行
 * 亟待解决的问题：jsp中的静态资源应该放在哪里？
 *             如果直接对这些静态资源进行访问    
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	// 登录拦截器的创建
	// 本注解用于创建Spring MVC的拦截器对象
	@Bean
	public LoginInterceptor getLoginInteceptor(){
		return new LoginInterceptor();
	}
	
	// 权限拦截器的创建 
	@Bean
	public PopedomInterceptor getPopedomInterceptor(){
		return new PopedomInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	
		// 对系统进行拦截器的创建
		InterceptorRegistration login = registry.addInterceptor(getLoginInteceptor());
		// 加入对应的匹配路径 同时将不拦截的路径进行放行 
		login.addPathPatterns(new String[]{"/*/*" , "/*/*/*"});
		// 配置排除的路径 
		login.excludePathPatterns("/oa/login" , "/css/**", "/docs/**", "/fonts/**","/images/**","/resources/**");
		
		// 注册权限拦截器 
		InterceptorRegistration popedm = registry.addInterceptor(getPopedomInterceptor());
		// 配置拦截的地址规则 
		popedm.addPathPatterns("/*/*/*");
		//排除不拦截的部分
		popedm.excludePathPatterns("/oa/login" , "/css/**", "/docs/**", "/fonts/**","/images/**","/resources/**");

	}
	
}



