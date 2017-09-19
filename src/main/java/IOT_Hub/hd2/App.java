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
	static String payload;
	static String msg2Device;
	
    public static void main( String[] args )
    {	
    	while (true){
    		String accessKey = ServiceSettings.getMNSAccessKeyId();
            String accessSecret = ServiceSettings.getMNSAccessKeySecret();
            String endPoint = ServiceSettings.getMNSAccountEndpoint();
            
            RecvAndSend recvAndSend = new RecvAndSend(accessKey, accessSecret, endPoint);
            SqlOperator sqlOperator = new SqlOperator();
            
            //recv msg from device and insert into database
            payload = recvAndSend.recv();
            
            //A7001300000009 contains gps's latitude and longitude 
            if (payload.contains("A7001300000009") && payload.contains("IOT")) {
				sqlOperator.insert(payload, "gps");
			} else if (payload.contains("IOT") || payload.contains("A8")) {
				sqlOperator.insert(payload,"onejson");
			} else {
				
			}

            ProtocolAnalyze protocolAnalyze = new ProtocolAnalyze();
            protocolAnalyze.setMsg2Device(payload);            
            msg2Device = protocolAnalyze.getMsg2Device();
            System.out.println("msg2Dev: "+msg2Device);
            
            if (msg2Device != "") {
            	recvAndSend.send(msg2Device);
			}
            
            
    	}
        
    }
}
