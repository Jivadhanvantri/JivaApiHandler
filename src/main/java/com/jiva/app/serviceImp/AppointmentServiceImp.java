package com.jiva.app.serviceImp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jiva.app.dtos.CaseNotesDto;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.dtos.WhatsappRequest;
import com.jiva.app.dtos.WhatsappResponse;
import com.jiva.app.reposistory.AppointmentRepo;
import com.jiva.app.reposistory.GlobalRepo;
import com.jiva.app.reposistory.SynchronyReposistory;
import com.jiva.app.service.AppointmentService;
import com.jiva.app.service.SynchronyService;

@Service
public class AppointmentServiceImp implements AppointmentService {
	
	Logger logger = LoggerFactory.getLogger(SynchronyServiceImp.class);
	
	@Autowired
	private AppointmentRepo appointRepo;
	
	@Autowired
	private WhatsappServiceImp whatsappService;
	
	@Autowired
	private SynchronyReposistory syncRepo;
	
	@Autowired
	private GlobalRepo globalRepo;
	
	@Override
	public Page<?> getNutritionistAppointment(Map<String, String> reqParam) throws Exception {

		Page<?> pageResult =appointRepo.getNutritionistAppointment(reqParam);
		return pageResult;
	}

	@Override
	public Map<String, Object> nutritionConsultationForm_edit(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.nutritionConsultationForm_edit(reqParam);
	}

	@Override
	public List<Map<String, Object>> nutritionConsultationPlanHistory(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.nutritionConsultationPlanHistory(reqParam);
	}

	@Override
	public List<Map<String, Object>> nutritionConsultationWeightHistory(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.nutritionConsultationWeightHistory(reqParam);
	}

	@Override
	public String nutritionConsultation_save(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.nutritionConsultation_save(reqParam);
	}

	@Override
	public Map<String, Object> entrollment_details(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.entrollment_details(reqParam);
	}

	@Override
	public List<Map<String, Object>> notes_view(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.notes_view(reqParam);
	}

	@Override
	public Map<String, Object> getAppointmentInitDetails_service(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.getAppointmentInitDetails_repo(reqParam);
	}

	@Override
	public Map<String, List<Map<String, Object>>> consultationform_view(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.consultationform_view(reqParam);
	}
	
	@Override
	public Map<String, Object> blockAppointmentInitDetails(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.blockAppointmentInitDetails(reqParam);
	}

	@Override
	public Page<?> blockAppointmentgetAll(Map<String, String> reqParam) throws Exception {
		Page<?> pageResult =appointRepo.blockAppointmentgetAll(reqParam);
		return pageResult;
	}

	@Override
	public ResponseMessage blockAppointmentupdateAll(Map<String, String> reqParam) throws Exception {
		// TODO Auto-generated method stub
		ResponseMessage responseHandler = new ResponseMessage();
		
		String startTimestamp=reqParam.get("startDt").toString()+" "+reqParam.get("startHr") + ":" + reqParam.get("startMin")+" "+reqParam.get("startMode");
		String endTimestamp=reqParam.get("endDt").toString()+" "+reqParam.get("endHr") + ":" + reqParam.get("endMin")+" "+reqParam.get("endMode");
		
		if(new Date(startTimestamp).after(new Date(endTimestamp))) {
			// Start timestamp before End timestamp.
			responseHandler.setStatusCode(402);
 			responseHandler.setMessage("Start date not greater than end date.");
			
		}else {
			responseHandler =appointRepo.blockAppointmentAll_save(reqParam);				
		}
		
		return responseHandler;
	}

	@Override
	public ResponseMessage updateShift(Map<String, String> reqParam) throws Exception {
		// TODO Auto-generated method stub
		ResponseMessage responseHandler = new ResponseMessage();
		
		String startTimestamp="02-Mar-2023 "+reqParam.get("startHr") + ":" + reqParam.get("startMin")+" "+reqParam.get("startMode");
		String endTimestamp="02-Mar-2023 "+reqParam.get("endHr") + ":" + reqParam.get("endMin")+" "+reqParam.get("endMode");
		
		if(new Date(startTimestamp).after(new Date(endTimestamp))) {
			// Start timestamp before End timestamp.
			responseHandler.setStatusCode(402);
 			responseHandler.setMessage("Start date not greater than end date.");
			
		}else {
			String message=appointRepo.updateShift(reqParam);
			responseHandler.setStatusCode(200);
			responseHandler.setMessage(message);
		}
		
		return responseHandler;
		
	}

	@Override
	public Map<String, Object> editForm_yoga_service(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.editForm_yoga_repo(reqParam);
	}

	@Override
	public String saveForm_yoga_service(Map<String, String> reqParam) throws Exception {
		// TODO Auto-generated method stub
		return appointRepo.saveForm_yoga_repo(reqParam);
	}

	@Override
	public String blockAppointmentDelete_service(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.blockAppointmentDelete_repo(reqParam);
	}

	@Override
	public Map<String, Object> getProgramOrderDetails_service(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.getProgramOrderDetails_repo(reqParam);
	}

	@Override
	public List<Map<String, Object>> stressLvlHist_yoga_service(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.stressLvlHist_yoga_repo(reqParam);
		
	}

	@Override
	public String updateDisposition(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		appointRepo.updateDisposition(reqParam);
		
		// 16 for consulation done.
		if(reqParam.get("disposition").equalsIgnoreCase("16") && reqParam.get("formName").equalsIgnoreCase("YOGATHERAPIST")) {
			sendYogaSessionAlert(reqParam.get("caseId"));
		}
		
		return "Success";
	}

	@Override
	public String updateStartSession(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.updateStartSession(reqParam);
	}

	@Override
	public Map<String, Object> editForm_mwb_service(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.editForm_MWB_repo(reqParam);
	}

	@Override
	public String saveForm_mwb_service(Map<String, String> reqParam) throws Exception {
		// TODO Auto-generated method stub
		return appointRepo.saveForm_MWB_repo(reqParam);
	}
	
	@Override
	public List<Map<String, Object>> stressLvlHist_mwb_service(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return appointRepo.stressLvlHist_MWB_repo(reqParam);
		
	}

    public void sendYogaSessionAlert(String dhanId) {
    	 WhatsappResponse whatsResponse = null;
    	 
	    try {
	    	
	    	 Map<String,Object> patientDetails=globalRepo.whatsappPatientDetails(dhanId);
	    	 String name=String.valueOf(patientDetails.get("patientName"));
	    	 String phoneNo=String.valueOf(patientDetails.get("whatsappNo"));
	    	
	    	 
	    	 String patientName=URLEncoder.encode(name, "UTF-8");
	    	 String clicklink=URLEncoder.encode("https://ayurveda.jiva.com/yoga-therapy-program-feedback?dhanid="+dhanId, "UTF-8");
	    	 String mediaUrl="http://ayurveda.jiva.com/whatsappsms/Yoga-Sessions-Even-Better.jpg";
	    	 
	    	 String mText= "Dear+%2A"+patientName+"%2A%2C%0A%0AWe+hope+you%27re+enjoying+your+yoga+experience+at+%2AJiva+Ayurveda%21%2A+%F0%9F%98%8A%0A%0AWe%27re+always+looking+for+ways+to+improve%2C+and+your+feedback+is+incredibly+valuable+to+us.+%0APlease+take+a+few+minutes+to+share+your+thoughts+and+suggestions+by+clicking+on+the+link+below%F0%9F%91%87%F0%9F%8F%BB.%0A"
	    	 +clicklink+"%2F%0A%0AWe+greatly+appreciate+your+input%2C+as+it+helps+us+to+continually+improve+our+services+and+meet+your+needs+more+effectively.%0A%0A_Thank+you+for+being+a+part+of+the+Jiva+Ayurveda+community%21_%0A%0ACaring+for+you%2C+%F0%9F%99%8F%0A%2AJiva+Ayurveda%2A%0Awww.jiva.com";
		     whatsResponse =  whatsappService.sendWhatsappImageText(phoneNo, mText, mediaUrl);
		     logger.info(mText);
		     if (whatsResponse != null && whatsResponse.getResponseStatus().equalsIgnoreCase("success")) {
					mText = mText.replace("'", "");
					WhatsappRequest request = new WhatsappRequest();
					request.setOrderCode("0");
					request.setDhanId(dhanId);
					request.setWhatsappId(whatsResponse.getWhatsappId());
					request.setMobileNo(phoneNo);
					request.setStatus(whatsResponse.getResponseStatus());
					request.setSentFor("YOGA_SESSION");
					request.setGrpId("0");
					request.setSmsText(mText);
					syncRepo.saveWhatsappResponse(request);
				}
	    }catch(Exception e) {
	    	logger.error("Error in YogaSessionAlert ->"+e.getMessage());
	    }
    }
  
}
