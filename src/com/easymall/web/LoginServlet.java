package com.easymall.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easymall.bean.User;
import com.easymall.utils.JDBCUtils;
/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//1.解决请求乱码
		ServletContext context = this.getServletContext();
		String encode = context.getInitParameter("encode");
		request.setCharacterEncoding(encode);
		//2.解决响应乱码
		response.setContentType("text/html;charset="+encode);
		
		//3.获取用户提交的用户名 密码
		String username=request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		
		//4.检验数据
		if(username==null||"".equals(username)) {
			request.setAttribute("msg", "用户名不能为空");
			request.getRequestDispatcher("/login.jsp").forward(request, response);//返回
			return;
		}
		if (password== null || "".equals(password)) {
			request.setAttribute("msg", "密码不能为空！");
			request.getRequestDispatcher("/login.jsp").forward(request,response);
			return;
		}
		
		//检查用户是否勾选过记住用户名
				if("true".equals(request.getParameter("remname"))){	
					Cookie cookie =new Cookie("remname",URLEncoder.encode(username,"utf-8"));
					cookie.setMaxAge(3600*24*30);
					cookie.setPath(request.getContextPath()+"/");
					response.addCookie(cookie);
				}else{
					Cookie cookie =new Cookie("remname",URLEncoder.encode(username,"utf-8"));
					cookie.setMaxAge(0);
					cookie.setPath(request.getContextPath()+"/");
					response.addCookie(cookie);
				}
		
		
		//验证用户、密码是否正确
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			conn=JDBCUtils.getConn();
			String sql="select * from userA where username=? and password=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2,password);
			rs = ps.executeQuery();
			if(!rs.next()){
				request.setAttribute("msg", "用户名和密码错误！");
				request.getRequestDispatcher("/login.jsp").forward(request,response);
				return;
			}else{
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				request.getSession().setAttribute("user", user);
				response.sendRedirect(request.getContextPath()+"/index.jsp");				
			}
			} catch (Exception e) {
			// TODO: handle exception
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
