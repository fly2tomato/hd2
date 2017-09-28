package IOT_Hub.hd2;


import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.iot.model.v20170420.PubRequest;
import com.aliyuncs.iot.model.v20170420.PubResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * Hello world!
 *
 */
public class App 
{	
	
	static final boolean isTopicMode = true;
	static final boolean isMybatis = true;
	static String payload;
	static String msg2Device;
	static int cnt = 0;
	static String accessKey = ServiceSettings.getMNSAccessKeyId();
    static String accessSecret = ServiceSettings.getMNSAccessKeySecret();
    static String endPoint = ServiceSettings.getMNSAccountEndpoint();
	
    public static void main( String[] args )
    {	
    	//判断是主题模式还是队列模式
        if (isTopicMode) {
        	startHttpEndpoint();
		} else {
			RecvAndSend recvAndSend = new RecvAndSend(accessKey, accessSecret, endPoint);
			while (true) {
				payload = recvAndSend.recv();
				payload = "{\"ID\":\"1234567\",\"tboxname\":\"tbox1\",\"IOT\":\"A70013000000080003010203040506070809C3\"}";
				if (payload != null) {
	            	sqlProcess(payload);
	                send2Dev(payload);
				}
			}
		}
    }

	public static void send2Dev(String payload) {
		RecvAndSend recvAndSend = new RecvAndSend(accessKey, accessSecret, endPoint);
		// TODO Auto-generated method stub
		ProtocolAnalyze protocolAnalyze = new ProtocolAnalyze();
        protocolAnalyze.setMsg2Device(payload);            
        msg2Device = protocolAnalyze.getMsg2Device();
        System.out.println("msg2Dev: "+msg2Device+"\n\n");
        
        if (msg2Device != "") {
        	recvAndSend.send(msg2Device);
		}
	}

	public static void sqlProcess(String payload) {
		System.out.println("payload in sqlProcess: "+payload);
		if (isMybatis) {
			SqlOperationMybatis sqlOperator = new SqlOperationMybatis(payload);
			if (payload.contains("A7001300000009") && payload.contains("IOT")) {
				sqlOperator.dataParse();
				sqlOperator.insert2gps_new();
			} else if (payload.contains("IOT") || payload.contains("A8")) {
				sqlOperator.insert2onejson();
			} else {
			}
		} else {
			SqlOperator sqlOperator = new SqlOperator();
			if (payload.contains("A7001300000009") && payload.contains("IOT")) {
				if (cnt == 1) {
					sqlOperator.insert(payload, "iot_gps");
					cnt = 0;
				}else {
					cnt ++;
				}
			} else if (payload.contains("IOT") || payload.contains("A8")) {
				sqlOperator.insert(payload,"onejson");
			} else {
			}
		}
		// TODO Auto-generated method stub
		//insert into database
        //A7001300000009 contains gps's latitude and longitude 
	}

	private static void startHttpEndpoint() {
		// TODO Auto-generated method stub
		int port = 8100;
        HttpEndpoint httpEndpoint = null;
        try {
            httpEndpoint = new HttpEndpoint(port);
            httpEndpoint.start();
            //Thread.sleep(1000*3600*1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //httpEndpoint.stop();
        }
	}
}
