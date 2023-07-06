package com.jiva.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.jiva.app.dtos.ResponseMessage;

public interface AppointmentService {
   
	Page<?> getNutritionistAppointment(Map<String,String> reqParam) throws Exception;

	Map<String, Object> nutritionConsultationForm_edit(Map<String, String> reqParam);

	List<Map<String, Object>> nutritionConsultationPlanHistory(Map<String, String> reqParam);

	List<Map<String, Object>> nutritionConsultationWeightHistory(Map<String, String> reqParam);

	String nutritionConsultation_save(Map<String, String> reqParam);

	Map<String, Object> entrollment_details(Map<String, String> reqParam);

	List<Map<String, Object>> notes_view(Map<String, String> reqParam);

	Map<String, Object> getAppointmentInitDetails_service(Map<String, String> reqParam);

	Map<String, List<Map<String, Object>>> consultationform_view(Map<String, String> reqParam);
	Map<String, Object> blockAppointmentInitDetails(Map<String, String> reqParam);

 	Page<?> blockAppointmentgetAll(Map<String, String> reqParam) throws Exception;

	ResponseMessage blockAppointmentupdateAll(Map<String, String> reqParam) throws Exception;

	ResponseMessage updateShift(Map<String, String> reqParam) throws Exception;

	Map<String, Object> editForm_yoga_service(Map<String, String> reqParam);

	String saveForm_yoga_service(Map<String, String> reqParam) throws Exception;

	String blockAppointmentDelete_service(Map<String, String> reqParam);

	Map<String,Object> getProgramOrderDetails_service(Map<String, String> reqParam);

	List<Map<String, Object>> stressLvlHist_yoga_service(Map<String, String> reqParam);
	
	String updateDisposition(Map<String, String> reqParam);

	String updateStartSession(Map<String, String> reqParam);

	Map<String, Object> editForm_mwb_service(Map<String, String> reqParam);

	List<Map<String, Object>> stressLvlHist_mwb_service(Map<String, String> reqParam);

	String saveForm_mwb_service(Map<String, String> reqParam) throws Exception;

	
}
