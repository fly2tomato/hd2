package IOT_Hub.hd2;

import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import mybatis_inter.IDataOperation;
import mybatis_model.IOTData;

public class Test {
	
	private static SqlSessionFactory sqlSessionFactory;
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
	
    public static SqlSessionFactory getSession(){
        return sqlSessionFactory;
    }
    
    private void getIOTData() {
		// TODO Auto-generated method stub
    	SqlSession session = sqlSessionFactory.openSession();
    	try {
			IDataOperation dataOperation = session.getMapper(IDataOperation.class);
			IOTData iotdata = dataOperation.selectIOTDataByID("59B88FD5F");
			System.err.println("ID: "+iotdata.getId()+"\ntboxname: "+iotdata.getTboxname()+"\nuploadtype: "+iotdata.getUploadtype()+"\npayload: "+iotdata.getPayload());
		} finally {
			// TODO: handle finally clause
			session.close();
		}
	}
    
    private void insert() {
    	IOTData iotData = new IOTData();
    	iotData.setId("123456789");
    	iotData.setTboxname("tbox1");
    	iotData.setUploadtype("mybatis");
    	iotData.setPayload("add by xj with using mybatis");
    	iotData.setCTime("1970-01-18 18:21:02");
    	iotData.setLatitude("1");
    	iotData.setNS("N");
    	iotData.setLongitude("2");
    	iotData.setEW("E");
    	iotData.setGpsValod("YES");
    	
		SqlSession session = sqlSessionFactory.openSession();
		try {
			IDataOperation dataOperation = session.getMapper(IDataOperation.class);
			dataOperation.insert2onejson(iotData);
			session.commit();
		} finally {
			session.close();
		}
	}
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test test = new Test();
		test.getIOTData();
		test.insert();
	}


}
