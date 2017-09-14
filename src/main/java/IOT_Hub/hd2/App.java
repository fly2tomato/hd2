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
            
            payload = recvAndSend.recv();
        	
            ProtocolAnalyze protocolAnalyze = new ProtocolAnalyze();
            protocolAnalyze.setMsg2Device(payload);            
            msg2Device = protocolAnalyze.getMsg2Device();
            
            recvAndSend.send(msg2Device);
            
            sqlOperator.insert(payload);
            
            
    	}
        
    }
}
