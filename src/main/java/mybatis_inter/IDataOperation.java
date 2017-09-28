package mybatis_inter;

import mybatis_model.IOTData;

public interface IDataOperation {
	
	public IOTData selectIOTDataByID(String id); //this method will be realized in DataOperationMapper.xml, where id="selectIOTDataByID"
	public void insert2gps_new(IOTData iotData);
	public void insert2onejson(IOTData iotData);

}
