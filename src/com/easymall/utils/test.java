
package com.easymall.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			conn=JDBCUtils.getConn();
				return;
			} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.close(conn, ps, rs);
		}
	}

}
