package com.jiva.app.reposistory;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jiva.app.dtos.BotAvailableSlotDto;
import com.jiva.app.dtos.BotPaymentLinkGeneratorDto;
import com.jiva.app.dtos.ResponseHandler;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.utils.helper;

@Repository
public class BotApiRepository {

	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyDbTemplate;

	@Autowired
	@Qualifier("jivaTemplate")
	private JdbcTemplate jivaTemplate;

	public List<Map<String,Object>> checkIsRegister_repo(String phoneno) {
		// TODO Auto-generated method stub
		String sql="SELECT CASE_ID as caseId,EADDRESS as phoneno FROM SCC_CONTACT_EADDRESS_T A,SCC_CASE_CONTACT_T B WHERE A.CONTACT_ID=B.CONTACT_ID AND "
				+ " EADDRESS='"+phoneno+"' ORDER BY EADDR_ID DESC LIMIT 1";
		List<Map<String,Object>> list=synchronyDbTemplate.queryForList(sql);

		return list;
	}

	public List<Map<String,Object>> clinicbypincode_repo(@NotBlank @Max(10) String pincode) {
		// TODO Auto-generated method stub
		ResponseMessage responseHandler = new ResponseMessage();
		String sql="select PIN_CODE,CLINIC_1, CLINIC_2 from SCC_JIVA_NEAREST_CLINIC_T A where PIN_CODE="+pincode;
		return synchronyDbTemplate.queryForList(sql);
		
	}

	public ResponseMessage availableSlot_repo(String groupName, String slotDate) throws Exception {
		ResponseMessage responseHandler = new ResponseMessage();
		Map<String,Object> availableSlot=new HashMap();

		String sql="select clinic_shift,name,(CASE WHEN STR_TO_DATE(substring_index(clinic_shift,'To',1),'%l:%i %p') < DATE_FORMAT(NOW() + INTERVAL 60 MINUTE,'%l:%i %p') THEN DATE_FORMAT(NOW() + INTERVAL 60 MINUTE,'%l:%i %p') ELSE "
				+ " substring_index(clinic_shift,'To',1) END) startTime from user_group A where A.desc LIKE '%"+groupName+"%'";
		Map<String,Object> obj2Holder=new HashMap();
		List<Map<String,Object>> obj2=jivaTemplate.queryForList(sql);
		
		// No Clinic found.
		if(obj2.size()==0) {
			responseHandler.setStatusCode(404);
			responseHandler.setMessage("Clinic Not Found.");
			return responseHandler;
		}
		
		obj2Holder=helper.mapListToObject(obj2Holder, obj2);
		
		if(String.valueOf(obj2Holder.get("clinic_shift")).equals("")) {
			responseHandler.setStatusCode(402);
			responseHandler.setMessage("You are not allowed to booked appointment for this clinic.");
			return responseHandler;
		}
		
		sql="SELECT DATE_FORMAT(APPOINTMENT_DT, '%H:%i:00') as time, COUNT(APPOINT_ID) as count FROM SCC_JIVA_APPOINTMENT_T WHERE "
				+ " DATE(APPOINTMENT_DT)='"+slotDate+"' AND CLINIC_GRP='"+String.valueOf(obj2Holder.get("name"))+"' AND STATE NOT IN (3,4) "
				+ " GROUP BY DATE_FORMAT(APPOINTMENT_DT, '%H-%i:00')";
		List<Map<String,Object>> obj1=synchronyDbTemplate.queryForList(sql);
		Map<String,Object> bookedSlot=new HashMap();
		
		for (Map<String, Object> el : obj1) {
			bookedSlot.put(el.get("time").toString(), el.get("count"));
		}
		
		String[] shiftChunk=String.valueOf(obj2Holder.get("clinic_shift")).split(" ");
		String[] startShiftChunk=String.valueOf(obj2Holder.get("startTime")).split(" ");
    	String sf=helper.increaseTime(shiftChunk[0]+":00", 0, shiftChunk[1]);
		String st=helper.increaseTime(shiftChunk[3]+":00",0, shiftChunk[4]);
		String uf=helper.increaseTime(startShiftChunk[0]+":00", 0, startShiftChunk[1]);
		
		LocalTime shiftfrom = LocalTime.parse(sf);
		LocalTime shiftto = LocalTime.parse(st);
		LocalTime shiftuo = LocalTime.parse(uf);
		
	    int i=0;
		do {
		   if(bookedSlot.get(sf)==null && bookedSlot.get(helper.increaseTime(sf, 15,""))==null) {
			 
			   
			   if(shiftuo.isBefore(LocalTime.parse(sf))) {
				   i++;
				   availableSlot.put(i+"",helper.convert24to12HourFormat(sf));  
			   }
			   		
			   sf=helper.increaseTime(sf, 15,"");
		   }
		   
		   sf=helper.increaseTime(sf, 15,"");
		   shiftfrom=LocalTime.parse(sf);
		}while(shiftfrom.isBefore(shiftto));
		
		Map<String, Object> sortedlist = new TreeMap<>(availableSlot);
        
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Data processing successfull.");
		responseHandler.setObj(sortedlist);
		
	    return responseHandler;
	}

	public Map<String, Object> generatePaymentLink_repo(BotPaymentLinkGeneratorDto availableDto) {
		// TODO Auto-generated method stub
		
		return null;
	}

	public List<Map<String,Object>> getDiseaselist_repo() {

		String sql="SELECT DISEASE_WHAT_NAME as disease FROM SCC_JIVA_DISEASE_WHAT_T WHERE DISEASE_WHAT_ID!=14";
		List<Map<String,Object>> list=synchronyDbTemplate.queryForList(sql);
		return list;
		
	}

	public void cancelAppointment_repo(String agentId,String appointmentId) {
		
		Integer increaseId=Integer.parseInt(appointmentId)+1;
		String ids=appointmentId+","+String.valueOf(increaseId);

		String sql="UPDATE SCC_JIVA_APPOINTMENT_T SET STATE = 4, MODIFY_ID =" + agentId +", MODIFY_DT = NOW() WHERE APPOINT_ID IN ( " + ids + " )";
		synchronyDbTemplate.update(sql, new Object[] {});	 
	}

	public List<Map<String, Object>> getClinicState_repo() {
		
		String sql="select State as state from user_group A where isactive=1 and State!='' group by State ORDER By State ";
		List<Map<String,Object>> list=jivaTemplate.queryForList(sql);
		return list;
		
	}

	public List<Map<String, Object>> getClinicCity_repo(String state) {
		String sql="select geo_city as city from user_group A where isactive=1 and State='"+state+"' group by geo_city ORDER By geo_city ";
		List<Map<String,Object>> list=jivaTemplate.queryForList(sql);
		return list;
	}

	public List<Map<String, Object>> getClinicByCityName_repo(String city) {
		String sql="select A.desc as clinic_name from user_group A where A.geo_city LIKE '%"+city+"%' and A.isactive=1 group by name ORDER By clinic_name";
		List<Map<String,Object>> list=jivaTemplate.queryForList(sql);
		
		return list;
	}

	public ResponseMessage verifyOtp_repo(String phoneno, String otp) {
		 // TODO Auto-generated method stub
		 ResponseMessage responseHandler = new ResponseMessage();
		 responseHandler.setStatusCode(403);
		 responseHandler.setMessage("Invalid OTP.");
		 
		String sql="SELECT COUNT(ID) FROM SCC_JIVA_BOT_OTP_T WHERE PHONE_NO='"+phoneno+"' AND OTP_CODE='"+otp+"' AND ENABLE_YN=1";
	    int count=synchronyDbTemplate.queryForObject(sql, new Object[] {},Integer.class);
	    
	    if(count>0) {
	    	sql="UPDATE SCC_JIVA_BOT_OTP_T SET ENABLE_YN=0 , UPDATE_DT=NOW() WHERE PHONE_NO='"+phoneno+"'";
	    	synchronyDbTemplate.update(sql);
	    	 responseHandler.setStatusCode(200);
			 responseHandler.setMessage("Otp Verify Successfully.");
	    }
	    
	    return responseHandler;
	}

	public void saveOTP(String phoneno, int num) {
		String sql="INSERT INTO SCC_JIVA_BOT_OTP_T ( PHONE_NO, OTP_CODE, ENABLE_YN, CREATE_DT) VALUES "
				+ " ('"+phoneno+"','"+num+"',1,NOW())";
				
		synchronyDbTemplate.update(sql, new Object[] {});	 
	}

	public List<Map<String, Object>> getPatientlistByParam_repo(String phoneno, String caseId) {
		// TODO Auto-generated method stub
		String sql="";
		
		if(phoneno!=null) {
			sql="SELECT CASE_ID as caseId,EADDRESS as phoneno,CONCAT(FIRST_NAME_DISPLAY,' ',LAST_NAME_DISPLAY) fullname FROM SCC_CONTACT_EADDRESS_T A,SCC_CASE_CONTACT_T B,SCC_CONTACT_T C, SCC_PERSON_T D"
					+ " WHERE A.CONTACT_ID=B.CONTACT_ID AND B.CONTACT_ID=C.CONTACT_ID AND C.PERSON_ID=D.PERSON_ID AND "
					+ " EADDRESS='"+phoneno+"' ORDER BY EADDR_ID DESC ";
		}else if(caseId!=null) {
			sql="SELECT CASE_ID caseId,EADDRESS as phoneno,CONCAT(FIRST_NAME_DISPLAY,' ',LAST_NAME_DISPLAY) fullname FROM SCC_CONTACT_EADDRESS_T A,SCC_CASE_CONTACT_T B,SCC_CONTACT_T C, SCC_PERSON_T D"
					+ " WHERE A.CONTACT_ID=B.CONTACT_ID AND B.CONTACT_ID=C.CONTACT_ID AND C.PERSON_ID=D.PERSON_ID AND "
					+ " CASE_ID='"+caseId+"' ORDER BY EADDR_ID DESC ";
		}
	
		List<Map<String,Object>> list=null;
		if(!sql.equals(""))
		  list=synchronyDbTemplate.queryForList(sql);

		return list;
	}

	public List<Map<String, Object>> getPatientAppointmentlist_service(String phoneno, String caseId) {
		// TODO Auto-generated method stub
		String sql="",extraParam="";
		List<Map<String,Object>> responselist= new ArrayList<Map<String, Object>>();
		
		if(phoneno!=null) {		
			extraParam="EADDRESS='"+phoneno+"'";
		}else if(caseId!=null) {			
			extraParam="CASE_ID='"+caseId+"'";
		}
		
		sql="SELECT DATE_FORMAT(APPOINTMENT_DT,'%d %b %Y %h:%i %p') appointmentDt,CLINIC_GRP as clinicName,CONCAT(FIRST_NAME,' ',LAST_NAME) AS name,APPOINT_ID as appointmentId "
				+ " FROM SCC_JIVA_APPOINTMENT_T A WHERE "+extraParam+" AND APPOINTMENT_DT >= NOW() AND STATE NOT IN (3,4) GROUP BY DATE(APPOINTMENT_DT) ORDER BY APPOINTMENT_DT ";
		List<Map<String,Object>> list=synchronyDbTemplate.queryForList(sql);
		
		list.forEach(el->{
			Map<String,Object> map=new HashMap<String,Object>();
			String sqlQuery="select A.desc as clinicName from user_group A where name LIKE '%"+el.get("clinicName")+"%'";
			List<Map<String,Object>> listQuery=jivaTemplate.queryForList(sqlQuery);
			map=helper.mapListToObject(el, listQuery);
			
			map.putAll(el);
			responselist.add(map);
		});
		
		return responselist;
	}


	public List<Map<String, Object>> bookedIMRCSlot_repo(String caseId) throws Exception {
			// TODO Auto-generated method stub
			String sql="",extraParam="";		
			
			extraParam="CASE_ID='"+caseId+"'";
			
			sql="SELECT DATE_FORMAT(APPOINTMENT_DT,'%d %b %Y %h:%i %p') appointmentDt,ID as appointmentId "
					+ " FROM SCC_JIVA_IMRC_APPOINTMENT_T A WHERE "+extraParam+" AND APPOINTMENT_DT >= NOW() AND CREATE_ID !=99 AND IS_CANCEL=0  GROUP BY DATE(APPOINTMENT_DT) ORDER BY APPOINTMENT_DT ";
			List<Map<String,Object>> responselist=synchronyDbTemplate.queryForList(sql);
			
			
			return responselist;

	}


}
