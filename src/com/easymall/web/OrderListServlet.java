package com.easymall.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easymall.bean.Order;
import com.easymall.bean.OrderInfo;
import com.easymall.bean.OrderItem;
import com.easymall.bean.Product;
import com.easymall.bean.User;
import com.easymall.utils.JDBCUtils;

public class OrderListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 解决请求乱码
		ServletContext context = this.getServletContext();
		String encode = context.getInitParameter("encode");
		request.setCharacterEncoding(encode);
		// 解决响应乱码
		response.setContentType("text/html;charset=" + encode);
		// 1.获取当前登陆用户
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.getWriter().write(
					"<a href='" + request.getContextPath()
							+ "/login.jsp'>请先登录！</a>");
		} else {
			// 2.根据用户id查询该用户的所有订单信息
			List<OrderInfo> orderInfoList = findOrderInfoByUserId(user.getId());
			// 3.将该用户的所有订单信息的list集合存入request域中, 转发到order_list.jsp中显示
			request.setAttribute("list", orderInfoList);
			request.getRequestDispatcher("/order_list.jsp").forward(request,
					response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	public List<OrderInfo> findOrderInfoByUserId(int userId) {
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		// 1.根据用户id查询该用户的所有订单信息，查询orders表
		List<Order> orderList = findOrderByUserId(userId);
		// 2.遍历每一个订单, 通过订单id查询当前订单中包含的所有订单项信息
		for (Order order : orderList) {
			String orderId = order.getId();
			// 根据用户order_id查询该订单号的所有订单项信息，查询orderitem表
			List<OrderItem> orderItems = findOrderItemByOrderId(orderId);
			// 3.遍历每一个订单项, 通过订单项获取商品信息及商品的购买数量
			Map<Product, Integer> map = new HashMap<Product, Integer>();
			for (OrderItem orderItem : orderItems) {
				// 3.1.获取商品id, 通过商品id查询商品信息, 返回Product对象
				Product product = finProductById(orderItem.getProduct_id());
				// 3.2.将商品信息和购买数量存入map中
				map.put(product, orderItem.getBuynum());
			}
			// 4.将订单信息和所有的订单项信息存入OrderInfo中
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrder(order);
			orderInfo.setMap(map);
			// 5.将一个完整的订单信息存入List集合中
			orderInfoList.add(orderInfo);
		}
		return orderInfoList;
	}

	public List<Order> findOrderByUserId(int userId) {
		List<Order> orderList = new ArrayList<Order>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "select * from orders where user_id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Order order = new Order();
				order.setId(rs.getString("id"));
				order.setMoney(rs.getDouble("money"));
				order.setReceiverinfo(rs.getString("receiverinfo"));
				order.setPaystate(rs.getInt("paystate"));
				order.setUser_id(rs.getInt("user_id"));
				order.setOrdertime(rs.getTimestamp("ordertime"));
				orderList.add(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, rs);
		}
		return orderList;
	}

	public List<OrderItem> findOrderItemByOrderId(String orderId) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "select * from orderItem where order_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			while (rs.next()) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder_id(orderId);
				orderItem.setProduct_id(rs.getString("product_id"));
				orderItem.setBuynum(rs.getInt("buynum"));
				orderItems.add(orderItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, rs);
		}
		return orderItems;
	}

	public Product finProductById(String id) {
		Product product = new Product();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "select * from products where id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				product.setId(id);
				product.setName(rs.getString("name"));
				product.setCategory(rs.getString("category"));
				product.setDescription(rs.getString("description"));
				product.setImgurl(rs.getString("imgurl"));
				product.setPnum(rs.getInt("pnum"));
				product.setPrice(rs.getDouble("price"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, rs);
		}
		return product;
	}
}
