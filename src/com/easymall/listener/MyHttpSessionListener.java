package com.easymall.listener;

import java.util.HashMap;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.easymall.bean.Product;

public class MyHttpSessionListener implements HttpSessionListener{
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute("cartmap", new HashMap<Product, Integer>());
		
	}
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {}
}


