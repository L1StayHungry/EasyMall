package com.easymall.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easymall.bean.Product;
import com.easymall.utils.JDBCUtils;

public class AjaxUpdateBuyNumServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.获取商品id和购买数量
				String pid = request.getParameter("pid");
				int buyNum = Integer.parseInt(request.getParameter("buyNum"));
				// 2根据商品id查询商品信息
				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				Product prod = new Product();
				try {
					conn = JDBCUtils.getConn();
					String sql = "select * from products where id =?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, pid);
					rs = ps.executeQuery();
					if (rs.next()) {
						prod.setId(rs.getString("id"));
						prod.setName(rs.getString("name"));
						prod.setPrice(rs.getDouble("price"));
						prod.setCategory(rs.getString("category"));
						prod.setPnum(rs.getInt("pnum"));
						prod.setImgurl(rs.getString("imgurl"));
						prod.setDescription(rs.getString("description"));
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException();
				} finally {
					JDBCUtils.close(conn, ps, rs);
				}
				// 3.将cartmap中该商品的购买数量修改为buyNum
				Map<Product, Integer> map = (Map<Product, Integer>) request
						.getSession().getAttribute("cartmap");
				map.put(prod, buyNum);
				// 4.做出响应
				response.getWriter().write("修改成功!");
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
	}

}
