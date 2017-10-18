<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
//	String captchaId = UUID.randomUUID().toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'login.jsp' starting page</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">


    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/easyui/demo.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>


    <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
    <style>
        input,img{
            vertical-align:middle;
        }
        body {
            text-align: center
        }
        .div-center {
            margin: 100 auto;
            width: 400px;
            height: 150px;
        }

        #sub,#sub1 {
            width: 100px;
            height: 35px;
            line-height: 35px;
            border: none;
        }
        /* css注释：为了观察效果设置宽度 边框 高度等样式 */
    </style>

</head>

<body onload="javascript:onload();">

<div class="div-center">
    <div class="easyui-panel" title="k6 后台登陆${failureMessage}"

         style="width:400px;text-align: center" align="center">
        <div style="padding:10px 60px 20px 60px">
            <form id="ff" method="post" action="admin/login">
                <table cellpadding="5">
                    <tr>
                        <td>Name:</td>
                        <td><input class="easyui-textbox" type="text" name="username" id="username"
                                   data-options="required:true"></input></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input class="easyui-textbox" type="password" name="password" id="password"
                                   data-options="required:true,validType:['text','length[3,20]']"></input>
                        </td>
                    </tr>
                     <tr>
                        <td>验证码:</td>
                        <td>
                        <input type="text" id="captcha" name="captcha" style="width: 70px" class="easyui-textbox"
                        data-options="required:true"/>
                            <img id="captchaImage" class="captchaImage"  <%--align="absmiddle"--%> src="admin/captcha?captchaId=${captchaId}"
                         onclick="changeCaptacha()"
                         />
                         <input type="hidden" name="captchaId" value="${captchaId}" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input type="checkbox" id="isRememberUsername" name="rememberMe"  />
                                记住用户
                            </label>
                        </td>
                    </tr>

                </table>
                <div style="text-align:center;padding:5px">
                    <input type="submit" class="easyui-linkbutton" id="sub"  value="submit" />
                    <button type="button"  class="easyui-linkbutton" id="sub1" onclick="clearForm()">Clear</button>
                </div>
            </form>
            <%--<div style="text-align:center;padding:5px">
                <a href="javascript:void(0)" class="easyui-linkbutton"
                   onclick="$('#ff').form('submit');">Submit</a> <a href="javascript:void(0)"
                                                        class="easyui-linkbutton" onclick="clearForm()">Clear</a>
            </div>--%>
        </div>
    </div>
</div>
<script>
    function onload() {
        var $username = $("#username");
        var $password = $("#password");
        var $isRememberUsername = $("#isRememberUsername");
        // 记住用户名
        if(getCookie("adminUsername") != null) {
            $isRememberUsername.prop("checked", true);
            $username.textbox('setValue',getCookie("adminUsername"));
            $password.focus();
        } else {
            $isRememberUsername.prop("checked", false);
            $username.focus();
        }
        $("#ff").submit(function (e) {
            if(!$(this).form('validate')){
                e.preventDefault();
            }
            if ($isRememberUsername.prop("checked")) {
                setCookie("adminUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
            } else {
                delCookie("adminUsername");
            }
        });
    }
   /* function submitForm() {
        $('#ff').form('submit')
        /!*{
            success: {
                window.location.href = "admin/main"
            }
        }*!/
        ;
    }*/
    function setCookie(name,value)
    {
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days*24*60*60*1000);
        document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
    }
    function getCookie(name)
    {
        var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr=document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    }
    function delCookie(name)
    {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval=getCookie(name);
        if(cval!=null)
            document.cookie= name + "="+cval+";expires="+exp.toGMTString();
    }
    function clearForm() {
        $('#ff').form('clear');
    }
     function changeCaptacha(){
        $('#captchaImage').attr("src", "<%=path%>/admin/captcha?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
    }
</script>
</body>
</html>
