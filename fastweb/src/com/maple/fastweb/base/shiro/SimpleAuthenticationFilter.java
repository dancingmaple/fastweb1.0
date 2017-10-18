package com.maple.fastweb.base.shiro;

import com.alibaba.druid.util.StringUtils;
import com.maple.fastweb.service.AdminService;
import com.maple.fastweb.service.RSAService;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/12.
 */
public class SimpleAuthenticationFilter extends FormAuthenticationFilter {

    /** "登录令牌"Cookie名称 */
    public static final String LOGIN_TOKEN_COOKIE_NAME = "adminLoginToken";
    /** 默认"加密密码"参数名称 */
    private static final String DEFAULT_EN_PASSWORD_PARAM = "enPassword";

    /** 默认"验证ID"参数名称 */
    private static final String DEFAULT_CAPTCHA_ID_PARAM = "captchaId";

    /** 默认"验证码"参数名称 */
    private static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    /** "加密密码"参数名称 */
    private String enPasswordParam = DEFAULT_EN_PASSWORD_PARAM;

    /** "验证ID"参数名称 */
    private String captchaIdParam = DEFAULT_CAPTCHA_ID_PARAM;

    /** "验证码"参数名称 */
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    /**
     * 创建令牌
     *
     * @param servletRequest
     *            ServletRequest
     * @param servletResponse
     *            ServletResponse
     * @return 令牌
     */
    @Override
    protected org.apache.shiro.authc.AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String username = getUsername(servletRequest);
        String password = getPassword(servletRequest);
        String captchaId = getCaptchaId(servletRequest);
        String captcha = getCaptcha(servletRequest);
        boolean rememberMe = isRememberMe(servletRequest);
        String host = getHost(servletRequest);
        return new SimpleAuthenticationToken(username, password, captchaId, captcha, rememberMe, host);
    }

    /**
     * 拒绝访问处理
     *
     * @param servletRequest
     *            ServletRequest
     * @param servletResponse
     *            ServletResponse
     * @return 是否继续处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (StringUtils.equalsIgnoreCase(request.getHeader("X-Requested-With"), "XMLHttpRequest")) {
            response.addHeader("loginStatus", "accessDenied");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        /*String loginToken = com.maple.fastweb.util.shopxx.WebUtils.getCookie(request, LOGIN_TOKEN_COOKIE_NAME);
        if (!StringUtils.equalsIgnoreCase(loginToken, adminService.getLoginToken())) {
            WebUtils.issueRedirect(request, response, "/");
            return false;
        }*/
        return super.onAccessDenied(request, response);
    }

    /**
     * 登录成功处理
     *
     * @param token
     *            令牌
     * @param subject
     *            Subject
     * @param servletRequest
     *            ServletRequest
     * @param servletResponse
     *            ServletResponse
     * @return 是否继续处理
     */
   /* @Override
    protected boolean onLoginSuccess(org.apache.shiro.authc.AuthenticationToken token, Subject subject, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Session session = subject.getSession();
        Map<Object, Object> attributes = new HashMap<Object, Object>();
        Collection<Object> keys = session.getAttributeKeys();
        for (Object key : keys) {
            attributes.put(key, session.getAttribute(key));
        }
        session.stop();
        session = subject.getSession();
        for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
        return super.onLoginSuccess(token, subject, servletRequest, servletResponse);
    }*/

    /**
     * 获取密码
     *
     * @param servletRequest
     *            ServletRequest
     * @return 密码
     */
   /* @Override
    protected String getPassword(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String password = rsaService.decryptParameter(enPasswordParam, request);
        rsaService.removePrivateKey(request);
        return password;
    }*/

    /**
     * 获取验证ID
     *
     * @param servletRequest
     *            ServletRequest
     * @return 验证ID
     */
    protected String getCaptchaId(ServletRequest servletRequest) {
        String captchaId = WebUtils.getCleanParam(servletRequest, captchaIdParam);
        if (captchaId == null) {
            captchaId = ((HttpServletRequest) servletRequest).getSession().getId();
        }
        return captchaId;
    }

    /**
     * 获取验证码
     *
     * @param servletRequest
     *            ServletRequest
     * @return 验证码
     */
    protected String getCaptcha(ServletRequest servletRequest) {
        return WebUtils.getCleanParam(servletRequest, captchaParam);
    }

    /**
     * 获取"加密密码"参数名称
     *
     * @return "加密密码"参数名称
     */
    public String getEnPasswordParam() {
        return enPasswordParam;
    }

    /**
     * 设置"加密密码"参数名称
     *
     * @param enPasswordParam
     *            "加密密码"参数名称
     */
    public void setEnPasswordParam(String enPasswordParam) {
        this.enPasswordParam = enPasswordParam;
    }

    /**
     * 获取"验证ID"参数名称
     *
     * @return "验证ID"参数名称
     */
    public String getCaptchaIdParam() {
        return captchaIdParam;
    }

    /**
     * 设置"验证ID"参数名称
     *
     * @param captchaIdParam
     *            "验证ID"参数名称
     */
    public void setCaptchaIdParam(String captchaIdParam) {
        this.captchaIdParam = captchaIdParam;
    }

    /**
     * 获取"验证码"参数名称
     *
     * @return "验证码"参数名称
     */
    public String getCaptchaParam() {
        return captchaParam;
    }

    /**
     * 设置"验证码"参数名称
     *
     * @param captchaParam
     *            "验证码"参数名称
     */
    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }
}
