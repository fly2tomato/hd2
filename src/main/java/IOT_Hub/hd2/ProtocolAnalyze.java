package IOT_Hub.hd2;

import java.util.ArrayList;

import javax.swing.plaf.PanelUI;

import org.json.JSONObject;

public class ProtocolAnalyze {
	
	String payload;
	
	
	
	public void setMsg2Device(String payload) {
		this.payload = payload;
	}

	
	public String getMsg2Device() {
		String timeStamp;
		String uuid;
		String msg2Device = "";
		String payloadContent;
		
		JSONObject jo = new JSONObject(payload);
		
		if (payload.contains("IOT")) {
			payloadContent = jo.getString("IOT");
		}else if (payload.contains("A8")) {
			payloadContent = jo.getString("A8");
		}else {
			payloadContent = "";
		}
		
		
		if (payloadContent.equals("E60004EA")) {
			timeStamp = "76000B00112233445566";
			msg2Device = timeStamp+getCheckSum(timeStamp);
		}else if (payloadContent.equals("E5000400E9")) {
			uuid = "75000b0174ac5fde4b34"; //uuid need to be modified
			msg2Device = uuid+getCheckSum(uuid);
		}else{// upload content is car data
			if (payload.contains("\"IOI\"")) { //upload type is IOT
				// TBC
			}else if (payload.contains("\"A8\"")) { //upload type is A8
				// TBC
			}else {
				msg2Device = "";
			}
		}
		
		return msg2Device;
	}
	
	
	public String getCheckSum(String num) {
		String chkSum = "00";
		ArrayList<String> newItemList = new ArrayList<String>();
		MathProcess mathProcess = new MathProcess();
		int sum = 0;
		
		
		for (int i = 0; i < num.length(); i++) {
			String newItem = "";
			if (i%2 == 0){
				newItem = num.substring(i, i+2);
				newItemList.add(newItem);
			}
		}
		System.out.println(newItemList);
		
		if (num.length()%2 != 0) {
			System.out.println("To calc the checksum, the count item should be even!\n");
		}else {
			for (String  item : newItemList) {
				sum += mathProcess.hex2int(item);
			}
			chkSum = Integer.toHexString(sum).substring(chkSum.length()-1, chkSum.length()+1);
		}
		System.out.println("chksum is: "+chkSum);
		return chkSum;
	}
}
