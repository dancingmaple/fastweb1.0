package com.maple.fastweb.controller;

import com.maple.fastweb.base.pojo.Log;
import com.maple.fastweb.base.pojo.Result;
import com.maple.fastweb.base.shiro.SimpleAuthenticationFilter;
import com.maple.fastweb.base.shiro.SimpleAuthenticationToken;
import com.maple.fastweb.mybatis.model.Commonlog;
import com.maple.fastweb.service.AdminService;
import com.maple.fastweb.service.CacheService;
import com.maple.fastweb.service.CaptchaService;
import com.maple.fastweb.util.shopxx.WebUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static com.maple.fastweb.base.shiro.SimpleAuthenticationFilter.LOGIN_TOKEN_COOKIE_NAME;

/**
 * Created by Administrator on 2017/10/12.
 */
@Controller
@RequestMapping("admin")
public class ShiroController {

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource
    CacheService cacheService;


    @RequestMapping("login")
    public String loginPage(ModelMap model, HttpServletRequest request) {

       /* String loginToken = WebUtils.getCookie(request, LOGIN_TOKEN_COOKIE_NAME);
        if (!StringUtils.equalsIgnoreCase(loginToken, adminService.getLoginToken())) {
            return "redirect:/";
        }*/
        if (adminService.isAuthenticated()) {
            return "redirect:main";
        }

        String  failureMessage = "";
        String loginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (StringUtils.isNotEmpty(loginFailure)) {
            if (loginFailure.endsWith("IncorrectCaptchaException")) {
                failureMessage="admin.captcha.invalid";
            } else if (loginFailure.endsWith("org.apache.shiro.authc.UnknownAccountException")) {
                failureMessage="admin.login.unknownAccount";
            } else if ("org.apache.shiro.authc.DisabledAccountException".equals(loginFailure)) {
                failureMessage="admin.login.disabledAccount";
            } else if ("org.apache.shiro.authc.LockedAccountException".equals(loginFailure)) {
                failureMessage="admin.login.lockedAccount";
            } else if ("org.apache.shiro.authc.IncorrectCredentialsException".equals(loginFailure)) {
                failureMessage="admin.login.incorrectCredentials";
            } else if (loginFailure.endsWith(".IncorrectLicenseException")) {
                failureMessage="admin.login.incorrectLicense";
            } else if ("org.apache.shiro.authc.AuthenticationException".equals(loginFailure)) {
                failureMessage="admin.login.authentication";
            }
        }

        model.addAttribute("captchaId", UUID.randomUUID().toString());
        model.addAttribute("failureMessage", failureMessage);
        return "admin/login";
    }

    /**
     * 验证码
     */
    @RequestMapping(value = "captcha", method = RequestMethod.GET)
    public void captcha(String captchaId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(captchaId)) {
            captchaId = request.getSession().getId();
        }
        String pragma = new StringBuilder().append("yB").append("-").append("der").append("ewoP").reverse().toString();
        String value = new StringBuilder().append("ten").append(".").append("xxp").append("ohs").reverse().toString();
        response.addHeader(pragma, value);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");


        OutputStream outputStream = response.getOutputStream();
        BufferedImage bufferedImage = captchaService.buildImage(captchaId);
        ImageIO.write(bufferedImage, "jpg", outputStream);
        outputStream.flush();
    }

    @RequiresPermissions("main")
    @RequestMapping
    public  String  index() {
        return "redirect:admin/main";
    }

    @RequiresPermissions("main")
    @RequestMapping("main")
    public @ResponseBody
    Result testLog() {
        Result result = new Result();
        result.setResult("login success");
        return result;
    }

    @RequestMapping("valid")
    public @ResponseBody  String testCaptcha(String id,String cap){
        Result result = new Result();
        result.setResult(captchaService.isValid(null,id,cap));
        return result.toJson();
    }
    //@RequestMapping("logout")   //在systemLogoutFilter中进行退出操作
    public  String  logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:admin/login";
    }
    @RequestMapping("user")
    public  @ResponseBody  Result  user() {
        Result result = new Result();
        result.setResult("user permission");
        return result;
    }
    @RequestMapping("user1")
    public  @ResponseBody  Result  user1() {
        Result result = new Result();
        result.setResult("user1 permission");
        return result;
    }
    @RequestMapping("user2")
    public  @ResponseBody  Result  user2() {
        Result result = new Result();
        result.setResult("user2 permission");
        return result;
    }

    @RequestMapping("unauthorized")
    public  @ResponseBody  Result  unauthorized() {
        Result result = new Result();
        result.setResult("sorry you dont have the permission");
        return result;
    }

    /**
     * 清除缓存
     */
    @RequestMapping(value = "cache/clear", method = RequestMethod.GET)
    public @ResponseBody  Result  clear(RedirectAttributes redirectAttributes) {

        Result result = new Result();

        result.setResult("缓存清除成功"+"cacheSize= "+cacheService.getCacheSize()
                + "cachePath = " + cacheService.getDiskStorePath() );
        cacheService.clear();
        return result;
    }

}
