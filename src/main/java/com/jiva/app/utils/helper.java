package com.jiva.app.utils;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


public class helper {

	public  static String setDateFormat(String sDate) throws Exception{
		 java.text.DateFormat dateformat = new java.text.SimpleDateFormat("dd-MMM-yyyy");
		 Date date1 = dateformat.parse(sDate);
		 java.text.DateFormat dateformat1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		 String fDate = dateformat1.format(date1);
		 return fDate;
	}
	
	public static long getUnixDate(String mDate) {
		long epoch=0l;
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date sDate = dateFormat.parse(mDate);
			epoch = sDate.getTime();
			epoch =(int)(epoch/1000);
		}catch(Exception s) {
		}
		return epoch;
	}
	
	public static Long calculateBMI(String ht,String weight) {
		
		 ht=NullChecker(ht,"0.0");
		 weight=NullChecker(weight,"0");
		 
		 if(ht.equals("VC LEAD")) {
			 return (long) 0;
		 }
		 
		 if(ht.equals("")) {
			ht="0.0"; 
		 }
		
		 String[] height=ht.split("\\.");
	     String heightInFt=height[0];
	     String heightInInc=height[1];
	     Long heightInCm=Math.round((Float.parseFloat(heightInFt)*12 + Float.parseFloat(heightInInc))* 2.54)/100;
	     long bmi=Math.round(Float.parseFloat(weight) / ( heightInCm * heightInCm));
	     return bmi;
	}
	
	public static Long calculateBMR(String ht,String weight,String age,String gender) {
		 long bmr=0;
		 ht=NullChecker(ht,"0.0");
		 weight=NullChecker(weight,"0");
		 
		 System.out.println(age);
		 if(age==null || age.equalsIgnoreCase("null")) {
			 age="0";
		 }
		 
		 if(ht.equals("VC LEAD")) {
			 return (long) 0;
		 }
		 
		 if(ht.equals("")) {
			ht="0.0"; 
		 }
		
		 String[] height=ht.split("\\.");
	     String heightInFt=height[0];
	     String heightInInc=height[1];
	     Long heightInCm=Math.round((Float.parseFloat(heightInFt)*12 + Float.parseFloat(heightInInc))* 2.54)/100;
	    
	     if(gender.equalsIgnoreCase("M")){
	       bmr= Math.round(88.362 + (13.397 * Float.parseFloat(weight)) + (4.799 * heightInCm) - (5.677 * Integer.parseInt(age)));
	     }else if(gender.equalsIgnoreCase("F")) {
	       bmr= Math.round(447.593 + (9.247 * Float.parseFloat(weight)) + (3.098 * heightInCm) - (4.330 * Integer.parseInt(age))); 
	     }

	     return bmr;
	}

	public static String NullChecker(String val,String defaultVal) {
		if(val==null) {
			return defaultVal;
		}else if(val.equals("")) {
			return defaultVal;
		}		
		return val;
	}
	
	public static Map<String,Object> mapListToObject(Map<String,Object> obj,List<Map<String,Object>> list) {
		if(list.size()>0) {
			obj.putAll(list.get(0));
		}
		return obj;
	}
	
	public static String increaseTime(String time,int minute,String mode) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:00");
		Calendar cal = Calendar.getInstance();		
		Date d = df.parse(time);
	    cal.setTime(d);
	    if(mode.equals("PM")) {
	    	cal.set(Calendar.AM_PM, Calendar.PM); 
	    }else if(mode.equals("AM")) {
	    	cal.set(Calendar.AM_PM, Calendar.AM); 
	    }    
        cal.add(Calendar.MINUTE, minute);
        return df.format(cal.getTime());
	}
	
	public static String convert24to12HourFormat(String time) throws ParseException {
		  try {
		        DateFormat inFormat = new SimpleDateFormat( "HH:mm:ss");
		        DateFormat outFormat = new SimpleDateFormat( "hh:mm a");
		        Date date = inFormat.parse(time);
		        String output= outFormat.format(date);	        
		        return output;
		    }catch (Exception e){
				return null;
		    }      
	}
	
	public static int generatorRandomOTP() {		
		 Random generator = new Random();
		 generator.setSeed(System.currentTimeMillis());
		 int num = generator.nextInt(899999) + 100000;
		 return num;
	}
	
	 public static Map<String,Object> toGupSupSmsResponse(String result) {
		 Map<String,Object> response = new HashMap();
		 try {
			 String status = result.split(" | ")[0];
	         String transId = result.split(" | ")[4].split("-")[0];
	         if(transId.equals("The")){
	             transId= "0";
	         }
	         
	         response.put("status", "200");
	         response.put("transactionId", transId);
	       
		 }catch (Exception e) {
				e.printStackTrace();
		 }
		 return response;
	}
	
	public static String toSmsConfigWithGupSup(String mText, String mobile) {
		String uri = "";
		try {
			String data = "method=sendMessage";
	    
	        data += "&userid=2000221498"; // your loginId
	    	data += "&password=" + URLEncoder.encode("C7!92kCR$*S", "UTF-8"); // your password
	    	data += "&msg=" + URLEncoder.encode(mText, "UTF-8");
	        data += "&send_to=" + URLEncoder.encode(mobile, "UTF-8"); // a valid 10 digit phone no.
	    	data += "&v=1.1" ;
	    	data += "&format=text";
	    	data += "&principalEntityId=1001544008404053918";
	    	data += "&dltTemplateId=1707167240256357522";
	    	data += "&mask=MYJIVA";
	    	data += "&auth_scheme=PLAIN";
	    	data += "&linkTrakingEnabled=TRUE";
	    	
	        uri = "http://enterprise.smsgupshup.com/GatewayAPI/rest?" + data;
	        	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uri;
	}
	
}
