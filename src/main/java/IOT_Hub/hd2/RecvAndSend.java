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

public class RecvAndSend {
	
	static String accessKey;
    static String accessSecret;
    static String endPoint;
    String payload;
    String decodePayloadStr;
    
    
	public RecvAndSend(String accessKey, String accessSecret, String endPoint) {
		// TODO Auto-generated constructor stub
		this.accessKey = accessKey;
		this.accessSecret = accessSecret;
		this.endPoint = endPoint;	
	}
	
	
	public String recv() {
		CloudAccount account = new CloudAccount(accessKey,accessSecret,endPoint);
       
        //CloudAccount account = new CloudAccount("LTAINSmBv0H9rB0X", "FTR219E78Vh7ovrDNWcIfNBFYTPvMw", "http://1258080874563314.mns.cn-shanghai.aliyuncs.com/");
                
        MNSClient client = account.getMNSClient(); //this client need only initialize once

        // Demo for receive message code
        try{
            CloudQueue queue = client.getQueueRef("aliyun-iot-dJZRNSVOEdS");// replace with your queue name
            //for (int i = 0; i < 1000; i++)
            Message popMsg = queue.popMessage();
            if (popMsg != null){
                System.out.println("message handle: " + popMsg.getReceiptHandle());
                System.out.println("message body: " + popMsg.getMessageBodyAsString());
                System.out.println("message id: " + popMsg.getMessageId());
                System.out.println("message dequeue count:" + popMsg.getDequeueCount());
                
                
                JSONObject jsonObject = new JSONObject(popMsg.getMessageBodyAsString());
                payload = jsonObject.getString("payload");
                decodePayloadStr = new String(Base64.decodeBase64(payload));
                System.out.println("payload: " + decodePayloadStr);
                //<<to add your special logic.>>
                
                //remember to  delete message when consume message successfully.
                queue.deleteMessage(popMsg.getReceiptHandle());
                System.out.println("delete message successfully.\n");
            }
            
        } catch (ServiceException se)
        {
            if (se.getErrorCode().equals("QueueNotExist"))
            {
                System.out.println("Queue is not exist.Please create queue before use");
            } else if (se.getErrorCode().equals("TimeExpired"))
            {
                System.out.println("The request is time expired. Please check your local machine timeclock");
            }
            /*
            you can get more MNS service error code in following link.
            https://help.aliyun.com/document_detail/mns/api_reference/error_code/error_code.html?spm=5176.docmns/api_reference/error_code/error_response
            */
            se.printStackTrace();
        } catch (Exception e)
        {
            System.out.println("Unknown exception happened!");
            e.printStackTrace();
        }

        client.close();
        return decodePayloadStr;
	}
	
	
	// the send function will send the data according to protocol, which will be analyzed from payload 
	public void send(String payload) {
		try {
			DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Iot", "iot.cn-shanghai.aliyuncs.com");
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKey, accessSecret);
        DefaultAcsClient client1 = new DefaultAcsClient(profile); //初始化SDK客户端
        
        PubRequest request = new PubRequest();
        request.setProductKey("dJZRNSVOEdS");
        request.setMessageContent(Base64.encodeBase64String(payload.getBytes()));
        request.setTopicFullName("/dJZRNSVOEdS/xj_hd2_dev0/data");
        request.setQos(0); //目前支持QoS0和QoS1
        PubResponse response;
		try {
			response = client1.getAcsResponse(request);
			System.out.println("Is sending Msg to device successful: "+response.getSuccess());
	        System.out.println("Is there errors in sending msg to device: "+response.getErrorMessage());
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
