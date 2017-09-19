package IOT_Hub.hd2;



import java.sql.DriverManager;
import java.sql.SQLException;

import org.json.JSONObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class SqlOperator {
	
	static final String sqlDatabase = "UAES";
	//static final String sqlTable = "onejson";
	String url = "jdbc:mysql://localhost:3306/"+sqlDatabase+"?useSSL=false";
	String rootName = "root";
	String pwd = "123456";
	ProtocolAnalyze pa = new ProtocolAnalyze();
	
	public SqlOperator() {
		// TODO Auto-generated constructor stub
	}
	
	public void insert(String payload, String sqlTable){
		String id;
		String tboxname;
		String uploadData;
		String data2Db;
		
		
		JSONObject jsonObject = new JSONObject(payload);
		
		if (sqlTable.equals("onejson")) {
			String uploadType;
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
		}else if (sqlTable.equals("gps")) {
			String isGpsValid;
			String sn = "";
			String ew = "";
			String lat = "";
			String log = "";
			
			id = jsonObject.getString("ID");
			tboxname = jsonObject.getString("tboxname");
			uploadData = jsonObject.getString("IOT");
			
			pa.setGps(uploadData);
			isGpsValid = pa.getIsGpsValid();
			if (isGpsValid.equals("YES")) {
				sn = pa.getIsSN();
				ew = pa.getIsEW();
				lat = pa.getLatitude();
				log = pa.getLongitude();
			}
			
			data2Db = "\""+id+"\",\""+tboxname+"\",\""+isGpsValid+"\",\""+sn+"\",\""+ew+"\",\""+lat+"\",\""+log+"\"";
		} else {
			data2Db = "";
		}
			
		
		
		
		
		
		try {
			//call Class.forName() to load the driver
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("load MySQL driver: success!\n");
			
			Connection conn = null;
			Statement stmt = null;
			
			try {
				//establish connection
				//call object DriverManager getConnection(), obtain a Connection object
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
