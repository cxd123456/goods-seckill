package com.miaosha.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	
	public static Connection getConn() throws Exception{
		String url = "jdbc:mysql://192.168.25.201:3306/miaosha";
		String username = "root";
		String password = "root9918";
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver);
		return DriverManager.getConnection(url,username, password);
	}
}
