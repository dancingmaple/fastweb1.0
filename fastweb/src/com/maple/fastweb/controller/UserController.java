package com.maple.fastweb.controller;

import com.maple.fastweb.base.controller.BaseController;
import com.maple.fastweb.base.pojo.Log;
import com.maple.fastweb.base.pojo.PageInfo;
import com.maple.fastweb.base.pojo.Result;
import com.maple.fastweb.mybatis.mapper.UserMapper;
import com.maple.fastweb.mybatis.model.Commonlog;
import com.maple.fastweb.mybatis.model.User;
import com.maple.fastweb.service.LogService;
import com.maple.fastweb.service.MailService;
import com.maple.fastweb.service.UserService;
import com.maple.fastweb.util.validator.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by zsj on 2017/9/26.
 */

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    LogService logService;
    @Autowired
    MailService mailService;

    @RequestMapping("/add")
    public @ResponseBody Result createUser(User user){
        Result result = new Result();
        userService.addOne(user);
        result.setResult(user);
        return result;
    }

    @RequestMapping("/test")
    public  @ResponseBody Result test(HttpServletResponse response) {
        Result result = new Result();
        response.setHeader("content-type", "application/json;charset=UTF-8");
        com.maple.fastweb.po.User user = new com.maple.fastweb.po.User(1, 19, "11", "222@qq.com","www   eeee");
        result.setErr(ValidateUtil.validate(user));
        Log.i(result.toJson());
        return result;
    }
    @RequestMapping("/trybean")
    public ModelAndView tryBean(){
        ModelAndView view = new ModelAndView();
        com.maple.fastweb.po.User user = new com.maple.fastweb.po.User(1, 19, "11", "222@qq.com","www   eeee");
        view.setViewName("testbean");
//        view.setViewName("${test}");
        view.addObject("user",user);

        return view;
    }
    @RequestMapping("/testlog")
    public  @ResponseBody Result testLog(HttpServletResponse response) {
        Result result = new Result();
        logService.addLog(new Commonlog(null,System.currentTimeMillis(),"this ia logs from /testlog"));
        Log.i(result.toJson());
        return result;
    }
    @RequestMapping("/testpage")
    public  @ResponseBody Result testPage(HttpServletResponse response) {
        Result result = new Result();
        PageInfo<User> page = userService.findByPage
                (new PageInfo<User>(0,10,
                        " id > 40 or true  ",
                        /*"1=1;delete * from user;select * from user where 1=1",*/
                        "id asc",0,null));
       result.setResult(page);
        /**
         * email.server.port.new=25
         email.server.host.new=smtp.qiye.163.com
         #email.server.host=smtp.ym.163.com
         #邮件发送帐号信息配置
         email.username.new=service@wan-ka.com
         email.password.new=wanka.098cl
         */
      /* mailService.sendTestSmtpMail("smtp.qiye.163.com",25,"service@wan-ka.com",
               "wanka.098cl",false,"service@wan-ka.com","shaojun.zhong@ce-link.com"
               );*/
      float z = 100/0;
        return result;
    }
}
