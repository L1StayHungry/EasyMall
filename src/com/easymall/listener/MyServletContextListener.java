package com.easymall.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent e) {				
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		//在ServletContext对象创建之后将web应用的url存入该域中
		//获取ServletContext域
		ServletContext context = e.getServletContext();				
		//获取web应用的虚拟路径
		String contextPath = context.getContextPath();
				
		//存入到ServletContext域中
		context.setAttribute("app", contextPath);
		
	}
}
