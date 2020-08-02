package com.easymall.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easymall.bean.Product;
import com.easymall.utils.JDBCUtils;

/**
 * Servlet implementation class ProdListServlet
 */
@WebServlet("/ProdListServlet")
public class ProdListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProdListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 1.获取参数(商品名称、商品分类、最低价格、最高价格)
				String name = request.getParameter("name");
				String category = request.getParameter("category");
				String minPrice = request.getParameter("minprice");
				String maxPrice = request.getParameter("maxprice");
				// 2.为搜索条件设置默认值, 并检查条件是否合法
				String _name = "";
				String _category = "";
				double _minPrice = 0;
				double _maxPrice = Double.MAX_VALUE;

				// 3.检查搜索条件是否合法
				if (name != null && !"".equals(name.trim())) {
					_name = name;
				}
				if (category != null && !"".equals(category.trim())) {
					_category = category;
				}
				String reg = "^\\d+$";
				if (minPrice != null && !"".equals(minPrice.trim())
						&& minPrice.matches(reg)) {
					_minPrice = Double.parseDouble(minPrice);
				}
				if (maxPrice != null && !"".equals(maxPrice.trim())
						&& maxPrice.matches(reg)) {
					// 最高价格如果大于等于最低价格
					if (Double.parseDouble(maxPrice) >= _minPrice) {
						_maxPrice = Double.parseDouble(maxPrice);
					}
				}
				List<Product> list = new ArrayList<Product>();
				
				
				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				
				try {
					conn = JDBCUtils.getConn();
					String sql = "select * from products where name like '%" + _name
							+ "%' and category like '%" + _category
							+ "%' and price >= " + _minPrice + " and price <= "
							+ _maxPrice + "";
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					while (rs.next()) {
						Product p = new Product();
						p.setId(rs.getString("id"));
						p.setName(rs.getString("name"));
						p.setCategory(rs.getString("category"));
						p.setDescription(rs.getString("description"));
						p.setPrice(rs.getDouble("price"));
						p.setPnum(rs.getInt("pnum"));
						p.setImgurl(rs.getString("imgurl"));
						// System.out.println(p);
						list.add(p);
					}
					// System.out.println(list.size());
					// 将所有商品list存入request域, 通过转发带到商品列表页面
					request.setAttribute("list", list);
					request.getRequestDispatcher("/prod_list.jsp").forward(request,
							response);
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
