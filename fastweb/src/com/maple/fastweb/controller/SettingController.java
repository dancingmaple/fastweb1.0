package com.maple.fastweb.controller;

import com.maple.fastweb.base.pojo.Result;
import com.maple.fastweb.base.setting.Setting;
import com.maple.fastweb.base.setting.SystemUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zsj
 * @date 2017/10/17
 * setting.config的测试类
 */
@Controller
@RequestMapping("setting")
public class SettingController {

    @RequestMapping("get")
    public @ResponseBody Result getSetting(){
        Result result = new Result();
        result.setResult(SystemUtils.getSetting());
        return result;
    }

    @RequestMapping("update")
    public @ResponseBody Result updateSetting(Setting setting){
        Result result = new Result();
        SystemUtils.setSetting(setting);
        result.setResult(SystemUtils.getSetting());
        return result;
    }

}
