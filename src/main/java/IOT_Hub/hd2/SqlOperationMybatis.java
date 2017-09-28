package IOT_Hub.hd2;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.json.JSONObject;

import mybatis_inter.IDataOperation;
import mybatis_model.IOTData;

public class SqlOperationMybatis {
	private static String payload;
	private String id;
	private String tboxname;
	private String uploadData;
	private String ctime;
	private String longitude;
	private String latitude;
	private String ns;
	private String ew;
	private String gpsValid;
	private static SqlSessionFactory sqlSessionFactory;
	ProtocolAnalyze pa = new ProtocolAnalyze();
	private static Reader reader; 
    
    static{
    	try {
			reader = Resources.getResourceAsReader("Configuration.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
	
	
	public SqlOperationMybatis(String payload) {
		JSONObject jsonObject = new JSONObject(payload);
		this.tboxname = jsonObject.getString("tboxname");
		this.id = jsonObject.getString("ID");
		if (payload.contains("IOT")) {
			this.uploadData = "IOT";
		}else if (payload.contains("A8")) {
			this.uploadData = "A8";
		} else {
			this.uploadData = "";
		}
		this.payload = jsonObject.getString("IOT");
	}
	
	public void dataParse() {
		setGpsValid();
		if (gpsValid.equals("YES")) {
			setNorthSouth();
			setEastWest();
			setLongitude();
			setLatitude();
			setCtime();
		}
	}

	private void setCtime() {
		// TODO Auto-generated method stub
		pa.setTime(id);
		ctime = pa.getTime();
	}

	private void setLatitude() {
		// TODO Auto-generated method stub
		latitude = pa.getLatitude();
	}

	private void setLongitude() {
		// TODO Auto-generated method stub
		longitude = pa.getLongitude();
	}

	private void setEastWest() {
		// TODO Auto-generated method stub
		ew = pa.getIsEW();
	}

	private void setNorthSouth() {
		// TODO Auto-generated method stub
		ns = pa.getIsSN();
	}

	private void setGpsValid() {
		// TODO Auto-generated method stub
		pa.setGps(payload);
		gpsValid = pa.getIsGpsValid();
	}

	public void insert2gps_new() {
		// TODO Auto-generated method stub
		IOTData iotData = new IOTData();
		iotData.setId("123456789");
    	iotData.setUploadtype("mybatis");
    	iotData.setPayload("add by xj with using mybatis");

    	iotData.setTboxname(tboxname);
    	iotData.setCTime(ctime);
    	iotData.setLatitude(latitude);
    	iotData.setNS(ns);
    	iotData.setLongitude(longitude);
    	iotData.setEW(ew);
    	iotData.setGpsValod(gpsValid);
    	
    	SqlSession session = sqlSessionFactory.openSession();
		try {
			IDataOperation dataOperation = session.getMapper(IDataOperation.class);
			dataOperation.insert2gps_new(iotData);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void insert2onejson() {
		// TODO Auto-generated method stub
		IOTData iotData = new IOTData();
		iotData.setId(id);
    	iotData.setUploadtype(uploadData);
    	iotData.setPayload(payload);
    	iotData.setTboxname(tboxname);

    	
    	SqlSession session = sqlSessionFactory.openSession();
		try {
			IDataOperation dataOperation = session.getMapper(IDataOperation.class);
			dataOperation.insert2onejson(iotData);
			session.commit();
		} finally {
			session.close();
		}
	}

	
	
}
