package springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration//就相当于springmvc.xml文件，与SpringSecurity关系不大
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //当进入"/"根路径后，会自动跳转到/login-view地址，也就是customLogin.jsp页面
        registry.addViewController("/").setViewName("redirect:/login-view");
        registry.addViewController("/login-view").setViewName("customLogin");

    }

}
