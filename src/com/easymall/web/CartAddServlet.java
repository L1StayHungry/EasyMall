package com.easymall.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easymall.bean.Product;
import com.easymall.utils.JDBCUtils;

/**
 * Servlet implementation class CartAddServlet
 */
@WebServlet("/CartAddServlet")
public class CartAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartAddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 1.获取商品的id和购买数量
				String pid = request.getParameter("pid");
				Integer buyNum = Integer.parseInt(request.getParameter("buyNum"));
				System.out.println(buyNum);
				// 2根据商品id查询商品信息
				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					conn = JDBCUtils.getConn();
					String sql = "select * from products where id =?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, pid);
					rs = ps.executeQuery();
					while(rs.next()){
						Product prod = new Product();
						prod.setId(rs.getString("id"));
						prod.setName(rs.getString("name"));
						prod.setPrice(rs.getDouble("price"));
						prod.setCategory(rs.getString("category"));
						prod.setPnum(rs.getInt("pnum"));
						prod.setImgurl(rs.getString("imgurl"));
						prod.setDescription(rs.getString("description"));
						Map<Product,Integer> map = (Map<Product,Integer>)request.getSession().getAttribute("cartmap");
						
						// 将商品及购买数量加入购物车
						/*
						 * 在将商品加入购物车时, 如果该商品在购物车中已经存在了, 那么购买数量应该是 "之前的购买数量"+"现在要加入的购买数量"
						 * 如果是第一次将商品加入购物车, 直接将商品和对应的购买数量加入即可!
						 */
						//如果商品的购买数量小于0 则在购物车中删除该商品
						if(buyNum < 0){
							map.remove(prod);
						}else{
							map.put(prod, map.containsKey(prod) ? map.get(prod) + buyNum : buyNum);
						}
						response.sendRedirect(request.getContextPath() + "/cart.jsp");
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
