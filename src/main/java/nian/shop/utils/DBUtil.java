package nian.shop.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
* @author created by NianTianlei
* @createDate on 2018年5月24日 上午11:47:20
*/
public class DBUtil {

	private static Properties props;
	
	static {
		try {
			InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application.properties");
			props = new Properties();
			props.load(in);
			in.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConn() throws Exception{
		String url = props.getProperty("spring.datasource.url");
		String username = props.getProperty("spring.datasource.username");
		String password = props.getProperty("spring.datasource.password");
		String driver = props.getProperty("spring.datasource.driver-class-name");
		Class.forName(driver);
		return DriverManager.getConnection(url,username, password);
	}
}
