import org.json.simple.JSONObject;

public class BusTimeInfo {
	private String bus_seq;
	private String from_jangyanri;
	private String from_yonsei;
	
	public BusTimeInfo(String s) {
		bus_seq=s;
	}
	
	public void Bus_h_bus(String s) {
		from_jangyanri =s;
	}
	public void Bus_y_bus(String s) {
		from_yonsei =s;
	}
	
	public String getBus_seq() {
		return bus_seq;
	}

	public String getFrom_jangyanri() {
		return from_jangyanri;
	}

	public void setFrom_jangyanri(String from_jangyanri) {
		this.from_jangyanri = from_jangyanri;
	}

	public String getFrom_yonsei() {
		return from_yonsei;
	}

	public void setFrom_yonsei(String from_yonsei) {
		this.from_yonsei = from_yonsei;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return toJsonString();
	}
	
	public String toJsonString() {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("bus_seq", bus_seq);
		jsonObject.put("from_jangyanri", from_jangyanri);
		jsonObject.put("from_yonsei", from_yonsei);
		
		return jsonObject.toJSONString();
		
	}
	
	
}
