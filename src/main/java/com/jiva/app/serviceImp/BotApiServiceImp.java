package com.jiva.app.serviceImp;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiva.app.dtos.BotAvailableSlotDto;
import com.jiva.app.dtos.BotPaymentLinkGeneratorDto;
import com.jiva.app.dtos.ResponseHandler;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.reposistory.BotApiRepository;
import com.jiva.app.service.BotApiService;
import com.jiva.app.service.SMSService;
import com.jiva.app.service.SynchronyService;
import com.jiva.app.utils.helper;

@Service
public class BotApiServiceImp implements BotApiService {

	@Autowired
	private BotApiRepository botApiRepository;


	@Autowired
	private SMSService smsService;
	
	@Autowired
	private SynchronyService synchronyService;
	
	
	@Override
	public ResponseMessage checkIsRegister_service(String phoneno) {
		// TODO Auto-generated method stub
		Map<String,Object> resDto=new HashMap();
		ResponseMessage responseHandler = new ResponseMessage();
		List<Map<String,Object>> list= botApiRepository.checkIsRegister_repo(phoneno);
		
		if(list.size()>0) {
			resDto=helper.mapListToObject(resDto, list);
			responseHandler.setStatusCode(200);
			responseHandler.setMessage("Data processing successfull.");
			responseHandler.setObj(resDto);
			
		}else {
			responseHandler.setStatusCode(404);
			responseHandler.setMessage("User not found.");
			responseHandler.setObj(resDto);	
		}
		
		return responseHandler;
	}

	@Override
	public ResponseMessage clinicbypincode_service(@NotBlank @Max(10) String pincode) {
		// TODO Auto-generated method stub
		ResponseMessage responseHandler = new ResponseMessage();
		List<Map<String,Object>> list= botApiRepository.clinicbypincode_repo(pincode);
		
		if(list.size()>0) {		
			responseHandler.setStatusCode(200);
			responseHandler.setMessage("Data processing successfull.");
			responseHandler.setObj(list);
			
		}else {
			responseHandler.setStatusCode(404);
			responseHandler.setMessage("Clinic not found.");
			responseHandler.setObj(list);	
		}
		
		return responseHandler;
		
	}

	@Override
	public ResponseMessage availableSlot_service(String groupName, String slotDate) throws Exception {
		// TODO Auto-generated method stub
		ResponseMessage responseHandler= botApiRepository.availableSlot_repo(groupName,slotDate);
		
		return responseHandler;
	}

	@Override
	public ResponseMessage generatePaymentLink_service(BotPaymentLinkGeneratorDto availableDto) {
		// TODO Auto-generated method stub
		 ResponseMessage responseHandler = new ResponseMessage();
		 botApiRepository.generatePaymentLink_repo(availableDto);
		 
		 responseHandler.setStatusCode(200);
		 responseHandler.setMessage("Data processing successfull.");
		 responseHandler.setObj("");
		 return null;
	}

	@Override
	public ResponseMessage getDiseaselist_service() {
		// TODO Auto-generated method stub
		 ResponseMessage responseHandler = new ResponseMessage();
		 List<Map<String,Object>> list= botApiRepository.getDiseaselist_repo();
		
		 responseHandler.setStatusCode(200);
		 responseHandler.setMessage("Data processing successfull.");
		 responseHandler.setObj(list);
		 return responseHandler;
	}

	@Override
	public ResponseMessage cancelAppointment_service(String agentId, String appointmentId) {
		 ResponseMessage responseHandler = new ResponseMessage();
		 
		 botApiRepository.cancelAppointment_repo(agentId,appointmentId);
		
		 responseHandler.setStatusCode(200);
		 responseHandler.setMessage("Appointment cancel successfull.");
		 return responseHandler;
	}

	@Override
	public ResponseMessage getClinicState_service() {
		
		 ResponseMessage responseHandler = new ResponseMessage();
		 List<Map<String,Object>> list= botApiRepository.getClinicState_repo();
		
		 responseHandler.setStatusCode(200);
		 responseHandler.setMessage("Data processing successfull.");
		 responseHandler.setObj(list);
		 return responseHandler;
	}

	@Override
	public ResponseMessage getClinicCity_service(String state) {
		 ResponseMessage responseHandler = new ResponseMessage();
		 List<Map<String,Object>> list= botApiRepository.getClinicCity_repo(state);
		
		 if(list.size()==0) {
			 responseHandler.setStatusCode(404);
			 responseHandler.setMessage("City not found.");
			 responseHandler.setObj(null);
		 }else {
			 responseHandler.setStatusCode(200);
			 responseHandler.setMessage("Data processing successfull.");
			 responseHandler.setObj(list); 
		 }
		 return responseHandler;
	}

	@Override
	public ResponseMessage getClinicByCityName_service(String city) {
		 ResponseMessage responseHandler = new ResponseMessage();
		 List<Map<String,Object>> list= botApiRepository.getClinicByCityName_repo(city);
		 
		 System.out.println(list.size());
		 if(list.size()==0) {
			 responseHandler.setStatusCode(404);
			 responseHandler.setMessage("Clinic not found.");
			 responseHandler.setObj(null);
		 }else {
			 responseHandler.setStatusCode(200);
			 responseHandler.setMessage("Data processing successfull.");
			 responseHandler.setObj(list); 
		 }
		
		
		 return responseHandler;
	}

	@Override
	public ResponseMessage sendOtp_service(String phoneno) {
	   
	   ResponseMessage responseHandler = new ResponseMessage();
	   int num=helper.generatorRandomOTP();
	   String mText =   num+" is the One Time Password (OTP) for discount. Please enter this OTP for validation. Regards, Jiva Ayurveda Clinic";

		String uri = helper.toSmsConfigWithGupSup(mText,phoneno);
		String result="";
		try {
			 String res = smsService.sendSmsWithGupSup(uri);
			 Map<String,Object> response=helper.toGupSupSmsResponse(res);
			 if(String.valueOf(response.get("status")).equals("200")) {
				 botApiRepository.saveOTP(phoneno,num);
				 responseHandler.setStatusCode(200);
				 responseHandler.setMessage("OTP send successfull.");	 
			 }else {
				 responseHandler.setStatusCode(401);
				 responseHandler.setMessage("OTP not sent.");	 
			 }
			 
		}catch(Exception e) {
			e.printStackTrace();
			responseHandler.setStatusCode(403);
			responseHandler.setMessage("Bad Request.");	
		}

		return responseHandler;
	}

	@Override
	public ResponseMessage verifyOtp_service(String phoneno,String otp) {
		// TODO Auto-generated method stub
		
		ResponseMessage responseHandler=botApiRepository.verifyOtp_repo(phoneno,otp);
		return responseHandler;
	}

	@Override
	public ResponseMessage getPatientlistByParam_service(String phoneno, String caseId) {
		ResponseMessage responseHandler = new ResponseMessage();
		List<Map<String,Object>> resDto= botApiRepository.getPatientlistByParam_repo(phoneno,caseId);
		
		if(resDto.size()>0) {
			responseHandler.setStatusCode(200);
			responseHandler.setMessage("Data processing successfull.");
			responseHandler.setObj(resDto);		
		}else {
			responseHandler.setStatusCode(404);
			responseHandler.setMessage("User not found.");
			responseHandler.setObj(resDto);	
		}
		
		return responseHandler;
	}

	@Override
	public ResponseMessage getPatientAppointmentlist_service(String phoneno, String caseId) {
		ResponseMessage responseHandler = new ResponseMessage();
		List<Map<String,Object>> resDto= botApiRepository.getPatientAppointmentlist_service(phoneno,caseId);
		
		if(resDto.size()>0) {
			responseHandler.setStatusCode(200);
			responseHandler.setMessage("Data processing successfull.");
			responseHandler.setObj(resDto);			
		}else {
			responseHandler.setStatusCode(200);
			responseHandler.setMessage("No data found.");
			responseHandler.setObj(resDto);	
		}
		
		return responseHandler;
	}

	@Override
	public ResponseMessage mrcTCAppointment_service(String caseId) {
		ResponseMessage responseHandler = new ResponseMessage();
		
		synchronyService.addGenericInternalList(caseId, "DIGITAL_GOOGLE_FACEBOOK");
		
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Booked successfully.");
	
		return responseHandler;
	}

	@Override
	public ResponseMessage bookedIMRCAppointment_service(String caseId) throws Exception {
		ResponseMessage responseHandler = new ResponseMessage();
		
		List<Map<String,Object>> list=botApiRepository.bookedIMRCSlot_repo(caseId);
		
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Booked appointment list.");
		responseHandler.setObj(list);
	
		return responseHandler;
	}
	
	
}
