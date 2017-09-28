package mybatis_model;

public class IOTData {
	
	private String id;
	private String tboxname;
	private String uploadtype;
	private String payload;
	private String gpsValid;
	private String east_west;
	private String longitude;
	private String north_south;
	private String latitude;
	private String ctime;
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setTboxname(String tboxname) {
		this.tboxname = tboxname;
	}
	
	public String getTboxname() {
		return tboxname;
	}
	
	public void setUploadtype(String uploadtype) {
		this.uploadtype = uploadtype;
	}
	
	public String getUploadtype() {
		return uploadtype;
	}
	
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public String getPayload() {
		return payload;
	}

	public void setGpsValod(String gpsValid){
		this.gpsValid = gpsValid;
	}
	
	public String getGpsValid(){
		return gpsValid;
	}
	
	public void setEW(String east_west){
		this.east_west = east_west;
	}
	
	public String getEW(){
		return east_west;
	}
	
	public void setLongitude(String longitude){
		this.longitude = longitude;
	}
	
	public String getLongitude(){
		return longitude;
	}
	
	public void setNS(String north_south){
		this.north_south = north_south;
	}
	
	public String getNS(){
		return north_south;
	}
	
	public void setLatitude(String latitude){
		this.latitude = latitude;
	}
	
	public String getLatitude(){
		return latitude;
	}
	
	public void setCTime(String ctime){
		this.ctime = ctime;
	}
	
	public String getCTime(){
		return ctime;
	}
}
