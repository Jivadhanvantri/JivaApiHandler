package com.jiva.app.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.service.AppointmentService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/")
public class AppointmentController {
	@Autowired
	private AppointmentService appointmentService;

	@GetMapping("getNutritionAppointment")
	@ApiOperation("Get all appointment for login user. ( Used in JivaReporting Pannel)")
	public ResponseEntity<Page<?>> getNutritionistAppointment(@RequestParam Map<String,String> reqParam) throws Exception{
		Page<?> paging=appointmentService.getNutritionistAppointment(reqParam);
		return ResponseEntity.ok().body(paging);
	}
	
	@GetMapping("getAppointmentInitDetails")
	@ApiOperation("Get appointment details against AppointmentId. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> getAppointmentInitDetails(@RequestParam Map<String,String> reqParam) throws Exception{
		Map<String,Object> data =appointmentService.getAppointmentInitDetails_service(reqParam);
		return ResponseEntity.ok().body(data);
	}
	
	
	@GetMapping("nutritionConsultationForm/edit")
	@ApiOperation("Get Nutrition Consultation Form default Data. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> nutritionConsultationForm_edit(@RequestParam Map<String,String> reqParam) throws Exception{
		Map<String,Object> nutriForm=appointmentService.nutritionConsultationForm_edit(reqParam);
		return ResponseEntity.ok().body(nutriForm);
	}
  
	@GetMapping("nutritionConsultationForm/planHistory")
	@ApiOperation("Get Nutrition Consultation Form Plan History. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> nutritionConsultationPlanHistory(@RequestParam Map<String,String> reqParam) throws Exception{
		List<Map<String, Object>> nutriForm=appointmentService.nutritionConsultationPlanHistory(reqParam);
		return ResponseEntity.ok().body(nutriForm);
	}
	
	@GetMapping("nutritionConsultationForm/weightHistory")
	@ApiOperation("Get Nutrition Consultation Form Weight History. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> nutritionConsultationWeightHistory(@RequestParam Map<String,String> reqParam) throws Exception{
		List<Map<String, Object>> nutriForm=appointmentService.nutritionConsultationWeightHistory(reqParam);
		return ResponseEntity.ok().body(nutriForm);
	}
	
	@PostMapping("nutritionConsultationForm/save")
	@ApiOperation("Add/Update Nutrition Consultation Form. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> nutritionConsultation_save(@RequestBody Map<String,String> reqParam) throws Exception{
		String  status=appointmentService.nutritionConsultation_save(reqParam);
		
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Record Update Successfully.");
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}
	
	@GetMapping("entrollment/details")
	@ApiOperation("Get Entrollment Details. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> entrollment_details(@RequestParam Map<String,String> reqParam) throws Exception{
		Map<String, Object> nutriForm=appointmentService.entrollment_details(reqParam);
		return ResponseEntity.ok().body(nutriForm);
	}
	
	@GetMapping("notes/view")
	@ApiOperation("Get Notes via chatName. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> notes_view(@RequestParam Map<String,String> reqParam) throws Exception{
		List<Map<String, Object>> notes_view=appointmentService.notes_view(reqParam);
		return ResponseEntity.ok().body(notes_view);
	}
	@GetMapping("consultationform/view")
	@ApiOperation("Get Consultation Form Details. (Used in JivaReporting Pannel)")
	public ResponseEntity<?> consultationform_view(@RequestParam Map<String,String> reqParam) throws Exception{
		Map<String, List<Map<String, Object>>> consultationform_view=appointmentService.consultationform_view(reqParam);
		return ResponseEntity.ok().body(consultationform_view);
	}
	@GetMapping("blockAppointment/initDetails")
	public ResponseEntity<?> blockAppointmentInitDetails(@RequestParam Map<String,String> reqParam) throws Exception{
		Map<String,Object> obj1= appointmentService.blockAppointmentInitDetails(reqParam);
		return ResponseEntity.ok().body(obj1);
	}

	@GetMapping("blockAppointment/getAppointments")
	public ResponseEntity<?> blockAppointmentgetAll(@RequestParam Map<String,String> reqParam) throws Exception{
		Page<?> obj1= appointmentService.blockAppointmentgetAll(reqParam);
		return ResponseEntity.ok().body(obj1);
	}
	

	@PostMapping("blockAppointment/updateAppointment")
	public ResponseEntity<?> blockAppointmentupdateAll(@RequestBody Map<String,String> reqParam) throws Exception{
		ResponseMessage  responseHandler=appointmentService.blockAppointmentupdateAll(reqParam);
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}
	
	@PostMapping("blockAppointment/deleteAppointment")
	public ResponseEntity<?> blockAppointmentDelete(@RequestBody Map<String,String> reqParam) throws Exception{
		String  status=appointmentService.blockAppointmentDelete_service(reqParam);
		
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setMessage(status);
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}
	
	@PostMapping("blockAppointment/updateShift")
	public ResponseEntity<?> updateShift(@RequestBody Map<String,String> reqParam) throws Exception{
		
		
		ResponseMessage  responseHandler=appointmentService.updateShift(reqParam);
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}
	
	@GetMapping("yoga/form/edit")
	public ResponseEntity<?> editForm_yoga(@RequestParam Map<String,String> reqParam) throws Exception{
		Map<String,Object> nutriForm=appointmentService.editForm_yoga_service(reqParam);
		return ResponseEntity.ok().body(nutriForm);
	}
	
	@GetMapping("yoga/stresslvl/history")
	public ResponseEntity<?> stressLvlHist_yoga(@RequestParam Map<String,String> reqParam) throws Exception{
		List<Map<String, Object>> nutriForm=appointmentService.stressLvlHist_yoga_service(reqParam);
		return ResponseEntity.ok().body(nutriForm);
	}
	
	@PostMapping("yoga/form/save")
	public ResponseEntity<?> saveForm_yoga(@RequestBody Map<String,String> reqParam) throws Exception{
		String  status=appointmentService.saveForm_yoga_service(reqParam);
		
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Record Update Successfully.");
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}
	
	@GetMapping("getProgramOrderDetails")
	public ResponseEntity<?> getProgramOrderDetails(@RequestParam Map<String,String> reqParam) throws Exception{
		Map<String,Object> obj=appointmentService.getProgramOrderDetails_service(reqParam);
		
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setObj(obj);
		responseHandler.setMessage("Successfully Retrive.");
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}
	
	@PostMapping("disposition/update")
	public ResponseEntity<?> updateDisposition(@RequestBody Map<String,String> reqParam) throws Exception{
		String  status=appointmentService.updateDisposition(reqParam);
		
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Disposition Update Successfully.");
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}
	
	@PostMapping("session/update")
	public ResponseEntity<?> updateStartSession(@RequestBody Map<String,String> reqParam) throws Exception{
		String  status=appointmentService.updateStartSession(reqParam);
		
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Session Update Successfully.");
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}
	
	@GetMapping("mwb/form/edit")
	public ResponseEntity<?> editForm_mwb(@RequestParam Map<String,String> reqParam) throws Exception{
		Map<String,Object> nutriForm=appointmentService.editForm_mwb_service(reqParam);
		return ResponseEntity.ok().body(nutriForm);
	}
	
	@GetMapping("mwb/stresslvl/history")
	public ResponseEntity<?> stressLvlHist_mwb(@RequestParam Map<String,String> reqParam) throws Exception{
		List<Map<String, Object>> nutriForm=appointmentService.stressLvlHist_mwb_service(reqParam);
		return ResponseEntity.ok().body(nutriForm);
	}
	
	@PostMapping("mwb/form/save")
	public ResponseEntity<?> saveForm_mwb(@RequestBody Map<String,String> reqParam) throws Exception{
		String  status=appointmentService.saveForm_mwb_service(reqParam);
		
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Record Update Successfully.");
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
	}

}
