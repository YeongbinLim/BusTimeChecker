import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;

import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BusTimeChecker {
	
	private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
	private final static int MAX=71;
	
	
	
	
	
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
			
			try {
				
				BusTimeChecker b = new BusTimeChecker();
				ArrayList<BusTimeInfo> binfo = b.requesutData("http://its.wonju.go.kr/busroute/selectCityScheduleView.do?rn=",34);
				
				JSONObject result= b.getNextBus(binfo);
				String nextBusTime = result.get("NextBusTime").toString();
				int preBusTimeDiff = (int) result.get("PreBusTimeDiff");

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println(str);

			
			

		
		
	}
	public static ArrayList<BusTimeInfo> requesutData(String url,int bus_num) throws IOException{
		String connUrl = url+bus_num;
		
		Connection conn = Jsoup
             .connect(connUrl)
             .header("Content-Type", "application/json;charset=UTF-8")
             .userAgent(USER_AGENT)
             .method(Connection.Method.GET)
             .ignoreContentType(true);
		ArrayList<String> yArrayList = new ArrayList<String>();
		ArrayList<String> hArrayList = new ArrayList<String>();

		Document doc = conn.get();
		ArrayList<BusTimeInfo> busList = new ArrayList<BusTimeInfo>();
		for (int i=0;i<MAX;i++)
		{
			
			//String number_bus_time=doc.select("div.txt_cont01").eq(i).text();
			String h_bus_time=doc.select("div.txt_cont02").eq(i).text();
			String y_bus_time=doc.select("div.txt_cont03").eq(i).text();

			BusTimeInfo bus = new BusTimeInfo(i+1+"");
			bus.setFrom_jangyanri(h_bus_time);
			bus.setFrom_yonsei(y_bus_time);
			
			busList.add(bus);
			
			
			
		}
		return busList;
		
	}
	
	public static JSONObject getNextBus(ArrayList<BusTimeInfo> busList) {
		
			
		long t = System.currentTimeMillis(); 

		SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");

		Date currentDate = new Date(t);
		String currentTime = dayTime.format(t);
		StringTokenizer stnz = new StringTokenizer(currentTime, ":");
		int hour_current = Integer.parseInt(stnz.nextToken());
		int minutes_current = Integer.parseInt(stnz.nextToken());
		
		hour_current=21;
		minutes_current=50;

		for(int j=0;j<MAX;j++)
		{
			try {
			StringTokenizer stnzz = new StringTokenizer(busList.get(j).getFrom_yonsei(), ":");
			int hour = Integer.parseInt(stnzz.nextToken());
			int minutes = Integer.parseInt(stnzz.nextToken());
			
			if(hour_current <= hour) {
				if(minutes_current <= minutes) {
					//다음 차 표시
					
					String gone_bus_time="";
					// "-" 인경우  "-"가 연속일 경우를 생각하지 않음
					if(j!=0 && busList.get(j-1).getFrom_yonsei().equals("-")) {
						if(j>1)
							gone_bus_time=busList.get(j-2).getFrom_yonsei();
					}
					else if(j!=0)							
						gone_bus_time=busList.get(j-1).getFrom_yonsei();
					else
						continue;
									
					System.out.println(busList.get(j).getFrom_yonsei());
					int diff_result=-1;
					//전차와 지금의 시간차를 계산하는 메소드   **1시간 이상 차이는 고려하지 않음
					diff_result=findDiff(gone_bus_time,hour_current,minutes_current);
					if(diff_result<=10)
						System.out.println("전차는 "+diff_result+"전에 출발 했습니다.");
					
					JSONObject result=new JSONObject();
					result.put("NextBusTime", busList.get(j).getFrom_yonsei());
					result.put("PreBusTimeDiff", diff_result);
					
					
					return result;				}
				
			}
			}
			
			catch(NumberFormatException e) {
				
			}
		}
		return null;
		
	}
	
	
	public static int findDiff(String gone_time,int real_hour,int real_minutes) {
		
		int result=0;
		StringTokenizer gone_bus = new StringTokenizer(gone_time, ":");
		int gone_hour = Integer.parseInt(gone_bus.nextToken());
		int gone_minutes = Integer.parseInt(gone_bus.nextToken());
		
		
		if(gone_hour==real_hour) {
			result=real_minutes-gone_minutes;
		}
		else {
			result=real_minutes+60-gone_minutes;
		}

		return result;
	}

}
