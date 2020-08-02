package com.easymall.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easymall.utils.JDBCUtils;

/**
 * Servlet implementation class RegistServlet
 */
@WebServlet("/RegistServlet")
public class RegistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		1.乱码处理
		//String encode=this.getServletContext().getInitParameter("encode");
		request.setCharacterEncoding("utf-8");
		//		④　处理响应参数乱码 
		response.setContentType("text/html;vharset=utf-8");

//     2.获取客户提交的注册信息
		String username=request.getParameter("username").trim();	
		String password=request.getParameter("password").trim();
		String password2=request.getParameter("password2").trim();
		String nickname=request.getParameter("nickname").trim();
		String email=request.getParameter("email").trim();
		String valistr=request.getParameter("valistr").trim();
		
//校检数据
		//		校验用户名
			if(username==null || "".equals(username)){
		//		设置提示消息
				request.setAttribute("msg", "用户名不能为空");
		//转发回注册页面进行提示
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
		
			if(password==null || "".equals(password)){
				request.setAttribute("msg", "密码不能为空！");
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
			
			if(!password.equals(password2)){
				request.setAttribute("msg", "两密码输入不一致!");
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
			if(nickname==null || "".equals(nickname)){
				request.setAttribute("msg", "昵称不能为空！");
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
			if(email==null || "".equals(email)){
				request.setAttribute("msg", "email不能为空！");
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
			if(valistr==null || "".equals(valistr)){
				request.setAttribute("msg", "验证码不能为空！");
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}	
//		校验邮箱格式
		if(!email.matches("^\\w+@\\w+(\\.\\w+)+$")){
			request.setAttribute("msg", "邮箱格式不对！");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		
// 验证码是否正确校验
			String code = (String)request.getSession().getAttribute("code");
			if(!valistr.equalsIgnoreCase(code)){
				request.setAttribute("msg", "验证码输入错误！");
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
				
//检查是否重复提交
//			String token1=request.getSession().getAttribute("token").toString();
//			String token2=request.getParameter("token");
//			if(token1 == null || token2 == null || !token1.equals(token2)){
//				throw new RuntimeException("不要重复提交数据！");
//			}else{
//				request.getSession().removeAttribute("token");
//			}
			
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			try {
				
				conn=JDBCUtils.getConn();
				ps=conn.prepareStatement("select * from userA where username = ?");
				ps.setString(1, username);
				rs = ps.executeQuery();
				if(rs.next()){
					request.setAttribute("msg", "该用户名已经被注册！");
					request.getRequestDispatcher("/regist.jsp").forward(request, response);
					return;
				}else{
					String sql = "insert into userA(username,password,nickname,email) values(?,?,?,?)";
					ps=conn.prepareStatement(sql);
					ps.setString(1, username);
					ps.setString(2, password);
					ps.setString(3, nickname);
					ps.setString(4, email);
					ps.executeUpdate();
					response.setContentType("text/html;vharset=utf-8");
					response.getWriter().write("<h1 style='text-align:center; color:red;'>恭喜您, 注册成功! 3秒之后跳转到首页...</h1>");
					response.setHeader("refresh", "3;url="+request.getContextPath()+"/index.jsp");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				JDBCUtils.close(conn, ps, rs);
			}
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
