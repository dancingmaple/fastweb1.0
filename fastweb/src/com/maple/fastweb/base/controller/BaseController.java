package com.maple.fastweb.base.controller;

import com.maple.fastweb.base.setting.Setting;
import com.maple.fastweb.base.setting.SystemUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author zsj
 * @date 2017/9/26
 */
public class BaseController {
    public String getServerRoute(){
        String fileSeperator = File.separator;
        String classPath = this.getClass().getClassLoader()
                .getResource(fileSeperator).getPath();
        try {
            classPath = URLDecoder.decode(classPath, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] strPath = classPath.split("/");
        String path = fileSeperator;
        for (int i = 0; i < strPath.length; i++) {
            if (i > 0 && i <= 3) {
                path = path + strPath[i] + fileSeperator;
            }
        }
        System.out.println(this.getClass().getResource("/").getPath());
        return path ;
    }

    /**
     * 异常处理
     *
     * @param exception
     *            异常
     * @param response
     *            HttpServletResponse
     * @return 数据视图
     */
    @ExceptionHandler
    public ModelAndView exceptionHandler(Exception exception, HttpServletResponse response) {
            Setting setting = SystemUtils.getSetting();
            ModelMap model = new ModelMap();
            if (setting.getIsDevelopmentEnabled()) {
                model.addAttribute("exception", exception);
                exception.printStackTrace();
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ModelAndView("exception", model);
        }

    /**
     * 数据绑定
     * 将参数中的String date，转换成Date.class
     *
     * @param binder
     * WebDataBinder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class,"date", new CustomDateEditor(dateFormat,true));
    }
}
