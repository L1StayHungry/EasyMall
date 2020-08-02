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

import com.easymall.bean.Product;
import com.easymall.utils.JDBCUtils;

/**
 * Servlet implementation class ProdInfoServlet
 */
@WebServlet("/ProdInfoServlet")
public class ProdInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProdInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 1.获取商品的id
		String pid = request.getParameter("pid");
		// 2.根据ID查询商品信息
		Product prod = new Product();
				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					conn = JDBCUtils.getConn();
					String sql = "select * from products where id=?";
					
					ps = conn.prepareStatement(sql);
					ps.setString(1, pid);
					rs = ps.executeQuery();
					if(rs.next()){
						prod.setId(rs.getString("id"));
						prod.setName(rs.getString("name"));
						prod.setCategory(rs.getString("category"));
						prod.setDescription(rs.getString("description"));
						prod.setPrice(rs.getDouble("price"));
						prod.setPnum(rs.getInt("pnum"));
						prod.setImgurl(rs.getString("imgurl"));
						
					}
					// 3.将商品信息存入request域, 并转发带到 商品详情页
					request.setAttribute("prod", prod);
			request.getRequestDispatcher("/prod_info.jsp").forward(request,response);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
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
