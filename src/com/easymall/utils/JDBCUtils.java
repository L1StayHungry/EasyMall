package com.easymall.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCUtils {

	public static Connection getConn(){
		Connection conn;
		try{
			//注册驱动
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//获取连接对象
			String jdbcUrl="jdbc:sqlserver://localhost:1433;DatabaseName=easymall";
			conn = DriverManager.getConnection(jdbcUrl,"sa","g1411414407");
			System.out.println("连接数据库成功");
			return conn;
		}catch (Exception e) {		
			System.out.println("连接不成功");
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	public static void close(Connection conn,Statement smt,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				rs=null;
			}
		}
		if(smt!=null){
			try {
				smt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				smt=null;
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				conn=null;
			}
		}
	}
}
