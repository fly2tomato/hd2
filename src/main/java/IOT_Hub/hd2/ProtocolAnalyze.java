package IOT_Hub.hd2;

import java.util.ArrayList;

import javax.swing.plaf.PanelUI;

import org.apache.log4j.LogSF;
import org.json.JSONObject;

public class ProtocolAnalyze {
	
	String payload;
	String iotContent;
	String id;
	
	
	
	public void setMsg2Device(String payload) {
		this.payload = payload;
	}
	
	public void setGps(String iotContent) {
		this.iotContent = iotContent;
		//this.iotContent = "A700130000000900131292F4C948584B5E0070";
	}
	
	//return gps gernaral info in 8 bits binary string
	public String getGpsGernaral() {
		String gpsGernaral;
		String tmp;
		//iotContent = "A700130000000900C0000000000000000000C3";
		
		gpsGernaral = iotContent.substring(16, 18);
		tmp = getBinaryStr(gpsGernaral);
		gpsGernaral = getByteForm(tmp);
		System.out.println("GPS Gernaral: "+gpsGernaral);
		
		return gpsGernaral;
	}
	
	

	public String getLatitude() {
		String latitudeStr;
		String latitude;
		String lat1;
		String lat2;
		String lat3;
		String lat4;
		String tmp;
		
		latitudeStr = iotContent.substring(18, 26);
		lat1 = getByteForm(getBinaryStr(latitudeStr.substring(0,2)));
		lat2 = getByteForm(getBinaryStr(latitudeStr.substring(2,4)));
		lat3 = getByteForm(getBinaryStr(latitudeStr.substring(4,6)));
		lat4 = getByteForm(getBinaryStr(latitudeStr.substring(6,8)));
		System.out.println("lat1: "+lat1);
		System.out.println("lat2: "+lat2);
		System.out.println("lat3: "+lat3);
		System.out.println("lat4: "+lat4);
		
		System.out.println("latitude String: "+latitudeStr);
		System.out.println("latitude String in binary: "+lat1+lat2+lat3+lat4);
		
		tmp = String.valueOf(Integer.parseInt(lat1+lat2+lat3+lat4,2));
		latitude = tmp.substring(0,tmp.length()-7)+"."+String.valueOf(Integer.parseInt(tmp.substring(tmp.length()-7,tmp.length()-1))/60);

		System.out.println("latitude is: "+latitude);
		
		return latitude;
	}

	

	public String getLongitude() {
		String longitudeStr;
		String longitude;
		String lon1;
		String lon2;
		String lon3;
		String lon4;
		String tmp;
		String degree;
		String minute;
		
		longitudeStr = iotContent.substring(26, 34);
		lon1 = getByteForm(getBinaryStr(longitudeStr.substring(0,2)));
		lon2 = getByteForm(getBinaryStr(longitudeStr.substring(2,4)));
		lon3 = getByteForm(getBinaryStr(longitudeStr.substring(4,6)));
		lon4 = getByteForm(getBinaryStr(longitudeStr.substring(6,8)));
		System.out.println("lon1: "+lon1);
		System.out.println("lon2: "+lon2);
		System.out.println("lon3: "+lon3);
		System.out.println("lon4: "+lon4);
		
		System.out.println("longitude String: "+longitudeStr);
		System.out.println("longitude String in binary: "+lon1+lon2+lon3+lon4);
		
		tmp = String.valueOf(Integer.parseInt(lon1+lon2+lon3+lon4,2));
		longitude = tmp.substring(0,tmp.length()-7)+"."+String.valueOf(Integer.parseInt(tmp.substring(tmp.length()-7,tmp.length()-1))/60);
		
		
		System.out.println("longitude is: "+longitude);
		
		return longitude;
	}
	
	public String getIsGpsValid() {
		String isGpsValid;
		String tmp;
		
		
		tmp = getGpsGernaral().substring(7);
		System.out.println("tmp: "+ tmp);
		if (tmp.equals("1")) {
			isGpsValid = "YES";
		}else {
			isGpsValid = "NO";
		}
		
		System.out.println("GPS valid: "+isGpsValid);
		
		return isGpsValid;
	}
	
	public String getIsSN(){
		String isSN;
		String tmp;
		
		
		System.out.println("GPS binaray: "+getGpsGernaral());
		tmp = getGpsGernaral().substring(6,7);
		if (tmp.equals("1")) {
			isSN = "N";
		}else {
			isSN = "S";
		}
		
		return isSN;
	}
	
	public String getIsEW() {
		String isEW;
		String tmp;
		
		tmp = getGpsGernaral().substring(5,6);
		if (tmp.equals("1")) {
			isEW = "W";
		}else {
			isEW = "E";
		}
		
		return isEW;
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
	
	// input hex string, return binary string
	public String getBinaryStr(String hexStr) {
		String binaryStr;
		
		binaryStr = Integer.toBinaryString(Integer.valueOf(hexStr,16));
		
		return binaryStr;
	}
	
	//eg. input "11", return "00000011"
		private String getByteForm(String tmp) {
			// TODO Auto-generated method stub
			String byteForm;
			
			byteForm = String.format("%08d",Integer.parseInt(tmp));
			
			return byteForm;
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
			chkSum = Integer.toHexString(sum).substring(chkSum.length()-1, chkSum.length()+1);// chkSum can be more than 3 digits, we only need the last two digits
		}
		System.out.println("chksum is: "+chkSum);
		return chkSum;
	}

	public void setTime(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getTime() {
		// TODO Auto-generated method stub
		String tmp = "";
		//id = "59BF923DF";
		MathProcess mp = new MathProcess();
		
		tmp = id.substring(0, id.length()-1);
		tmp = String.valueOf(mp.hex2int(tmp));
		tmp = mp.stamp2Date(tmp);
		System.out.println("ctime is: "+tmp);
		
		return tmp;
	}
	
	
}
