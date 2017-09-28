package IOT_Hub.hd2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MathProcess {
	
	public int hex2int(String hexNum) {
		int intNum;
		
		try {
			intNum = Integer.parseInt(hexNum,16);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("16进制数转10进制数出错：被转换的字符串非16进制");
			intNum = 0;
		}
		return intNum;
	}
	
	public String stamp2Date(String stampStr) {
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    Long time=new Long(stampStr+"000");  
	    String ctime = format.format(time);  
	    try {
			Date date=format.parse(ctime);
			System.out.println("Format To String(Date):"+ctime);  
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return ctime;
	}
}
