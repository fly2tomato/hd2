package IOT_Hub.hd2;



import java.sql.DriverManager;
import java.sql.SQLException;

import org.json.JSONObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class SqlOperator {
	
	static final String sqlDatabase = "UAES";
	static final String sqlTable = "onejson";
	String url = "jdbc:mysql://localhost:3306/"+sqlDatabase+"?useSSL=false";
	String rootName = "root";
	String pwd = "123456";
	
	
	public SqlOperator() {
		// TODO Auto-generated constructor stub
	}
	
	public void insert(String payload){
		String id;
		String tboxname;
		String uploadType;
		String uploadData;
		String data2Db;
		
		
		JSONObject jsonObject = new JSONObject(payload);
		
		id = jsonObject.getString("ID");
		tboxname = jsonObject.getString("tboxname");
		if (payload.contains("IOT")) {
			uploadType = "IOT";
			uploadData = jsonObject.getString(uploadType);
		}else if (payload.contains("A8")) {
			uploadType = "A8";
			uploadData = jsonObject.getString(uploadType);
		}else {
			uploadType = "";
			uploadData = "";
		}
		
		data2Db = "\""+id+"\",\""+tboxname+"\",\""+uploadType+"\",\""+uploadData+"\"";
		
		
		try {
			//call Class.forName() to load the driver
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("load MySQL driver: success!\n");
			
			
			
			//establish connection
			//call object DriverManager getConnection(), obtain a Connection object
			Connection conn = null;
			//create Statement object
			Statement stmt = null;
			
			try {
				conn = (Connection) DriverManager.getConnection(url,rootName,pwd);
				stmt = (Statement) conn.createStatement();
				System.out.println("link to dababase: success!");
				
				//refresh sql 
				String sql = "insert into "+sqlTable+" values ("+data2Db+");";
				System.out.println(sql);
				
				PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
				pst.executeUpdate();
				
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("load MySQL driver: fail!\n");
			e.printStackTrace();
		}
		
	}
	
	
	
}
