<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>欢迎注册EasyMall</title>
  <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" href="css/regist.css"/>
  <script>
   function changeImage(thisobj){
    thisobj.src = "${app}/ValiImageServlet?time="+new Date().getTime();
   }
  </script>
 </head>
 <body>
  <form action="<%=request.getContextPath()%>/RegistServlet" method="POST">
   
   <h1>欢迎注册EasyMall</h1>
   <table>
    <tr>
     <td class="tds">用户名：</td>
     <td>
      <input type="text" name="username" 
      value="<%=request.getParameter("username") == null ? "" : request.getParameter("username")%>"/>
     </td>
    </tr>
    <tr>
     <td class="tds">密码：</td>
     <td>
      <input type="password" name="password"/>
     </td>
    </tr>
    <tr>
     <td class="tds">确认密码：</td>
     <td>
      <input type="password" name="password2"/>
     </td>
    </tr>
    <tr>
     <td class="tds">昵称：</td>
     <td>
      <input type="text" name="nickname" />
     </td>
    </tr>
    <tr>
     <td class="tds">邮箱：</td>
     <td>
      <input type="text" name="email" />
     </td>
    </tr>
    <tr>
     <td class="tds">验证码：</td>
     <td>
       <input type="text" name="valistr" />
       <img onclick="changeImage(this)" src="<%=request.getContextPath()%>/ValiImageServlet" />
     </td>
     
    </tr>
    <tr>
     <td class="sub_td" colspan="2" class="tds">
      <input type="submit" value="注册用户"/>
     </td>
    </tr>
    
<!-- 在注册表单所在的table中添加一行, 利用java代码取出request域中保存的提示消息, 进行提示!--> 
    <tr>
      <td class="sub_td" colspan="2" class="tds">${msg}</td>
    </tr>
   </table>
  </form>

 </body>
</html>