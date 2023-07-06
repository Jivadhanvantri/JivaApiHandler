package com.jiva.app.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jiva.app.dtos.AppointmetStatusData;
import com.jiva.app.dtos.CaseNotesDto;
import com.jiva.app.dtos.ClinicAppointmentDto;
import com.jiva.app.dtos.ClinicApptRequestDto;
import com.jiva.app.dtos.ClinicFreshCallDto;
import com.jiva.app.dtos.CreateContactDto;
import com.jiva.app.dtos.IperformanceObDto;
import com.jiva.app.dtos.OutboundDetailsDto;
import com.jiva.app.dtos.PatientDataRequest;
import com.jiva.app.dtos.PatientDataResponse;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.dtos.SMSDetails;
import com.jiva.app.dtos.ShareChatRequestDto;
import com.jiva.app.dtos.WacDto;
import com.jiva.app.dtos.WacVCRequestDto;
import com.jiva.app.service.SynchronyService;

import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(value = "*")
public class SynchronyController {

	
	@Autowired
	private SynchronyService service;
	
	@GetMapping("/getPatientData")
	@ApiOperation("Get patient details via patientrequest dto. (Used in crm)")
	ResponseEntity<PatientDataResponse> getPatientData(@RequestBody PatientDataRequest requestData){
		PatientDataResponse response = service.getPatientDataList(requestData);
		return new ResponseEntity<PatientDataResponse>(response,HttpStatus.OK);
	}
    
	@GetMapping("/getClinicUserName")
	@ApiOperation("Get the clinic username via the clinic group. (Used in crm)")
	ResponseEntity<String> getClinicUserNameByCode(@Valid @RequestParam("groupName") @NotBlank(message = "Group name should not be empty.") String groupName){
		return new ResponseEntity<String>(service.getClinicUserNameByCode(groupName),HttpStatus.OK);
	}
	
	@GetMapping("/getApptCountByGroup")
	@ApiOperation("Get the appointmentcount with a dhan Id and clinic group. (Used in crm)")
	ResponseEntity<Integer> getApptCountByGroup(@Valid @RequestBody ClinicAppointmentDto apptDto){
		return new ResponseEntity<Integer>(service.getClinicApptByGroupName(apptDto),HttpStatus.OK);
	}
	
	@PostMapping("/saveClinicAppointment")
	@ApiOperation("Save clinic appointment in dhanvantai. (Used in crm)")
	ResponseEntity<String> saveClinicAppointment(@Valid @RequestBody ClinicApptRequestDto request){
		return new ResponseEntity<String>(service.createClinicAppt(request),HttpStatus.OK);
	}
	
	@GetMapping("/getConsultationStatus")
	@ApiOperation("Get consultation form status. (Used in crm)")
	ResponseEntity<String> getConsultFormStatus(@Valid @RequestParam("dhanId") String dhanId){
		return new ResponseEntity<String>(service.getConsultationFormStatus(dhanId),HttpStatus.OK);
	}
	
	@PostMapping("/saveShareChat")
	ResponseEntity<ResponseMessage> saveShareChat(@RequestBody ShareChatRequestDto requestDto){
		return new ResponseEntity<ResponseMessage>(service.getSahreChatData(requestDto),HttpStatus.OK);
	}
	
	@PostMapping("/getIPOutbound")
	ResponseEntity<ResponseMessage> getIPOutbound(@Valid @RequestBody IperformanceObDto requestDto){
		return new ResponseEntity<ResponseMessage>(service.getIPerformanceOutbound(requestDto),HttpStatus.OK);
	}
	
	@PostMapping("/saveSms")
	@ApiOperation(value = "Save sms details in dhanvantari.  (Used in crm)", notes = " ")
	ResponseEntity<String> saveSmsDetails(@Valid @RequestBody SMSDetails details){
		return new ResponseEntity<String>(service.saveDhanSms(details),HttpStatus.OK);
	}
	
	@PostMapping("/createDhanId")
	@ApiOperation("Create new contact for World ayurbeda congress program based in mobile. (Used for WAC)")
	ResponseEntity<ResponseMessage> createDhanId(@RequestBody WacDto wacDto){
		return new ResponseEntity<ResponseMessage>(service.createDhanvantariid(wacDto),HttpStatus.OK);
	}
	
	@PostMapping("/createVC")
	@ApiOperation("Send Video consultation sms on whatsapp to patient and doctor. (Used for WAC)")
	ResponseEntity<ResponseMessage> createVC(@RequestBody WacVCRequestDto requestDto){
		return new ResponseEntity<ResponseMessage>(service.createVideoLink(requestDto),HttpStatus.OK);
	}
	
	@GetMapping("/updateBotPayment")
	ResponseEntity<ResponseMessage> updateBotPayment(@RequestParam("botid") String botid,@RequestParam("amount") String amount,@RequestParam("status") String status){
		return new ResponseEntity<ResponseMessage>(service.updateBotPayment(botid, amount, status),HttpStatus.OK);
	}
	
	@GetMapping("/getBotPaymentStatus")
	@ApiOperation("Get the status of a bot's payments using phone number and bot ID. (Used in JivaReportingPanel)")
	ResponseEntity<ResponseMessage> getPaymentStatus(@RequestParam("phone") String phone,@RequestParam("botId") String botId){
		return new ResponseEntity<ResponseMessage>(service.getBotPaymentStatus(phone,botId),HttpStatus.OK);
	}
	
	@GetMapping("/getAppointmentStatus")
	@ApiOperation("Get the status of an appointment with a Dhan ID")
	ResponseEntity<List<AppointmetStatusData>> getAppointmentStatusList(@Valid @RequestParam("dhanId") @NotEmpty(message = "Dhanvantari Id should not be empty.") String dhanId){
		List<AppointmetStatusData> resultList = service.getAppointmentStatus(dhanId);
		return new ResponseEntity<List<AppointmetStatusData>>(resultList,HttpStatus.OK);
 	}
	
	@GetMapping("/getDhanPayUser")
	@ApiOperation("Get username via dhanvantariid. (Used in crm)")
	ResponseEntity<String>  getDhanPayUsername(@RequestParam("dhanId") String dhanId,@RequestParam("userId") String userId,@RequestParam("type") String type){
		return new ResponseEntity<String>(service.getDhanPaymentUser(dhanId, userId, type),HttpStatus.OK);
	}
	
	@GetMapping("/createGenInternalList")
	@ApiOperation("Create internal list based in dhan id and list name")
	ResponseEntity<String> createInternalList(@RequestParam("dhanId") String dhanId,@RequestParam("listName") String listName){
		return new ResponseEntity<String>(service.addGenericInternalList(dhanId, listName),HttpStatus.OK);
	}
	
	@GetMapping("/getDisposition")
	@ApiOperation("Get disposition via dhan Id and used Id. (Used by Dialer vendor)")
	ResponseEntity<ResponseMessage> getDhanDisposition(@RequestParam("dhanId") String dhanId,@RequestParam("userId") String userId){
		return new ResponseEntity<ResponseMessage>(service.getDispositionByUserId(dhanId, userId),HttpStatus.OK);
	}
	
	@GetMapping("/getDispostionBySession")
	@ApiOperation("Get disposition via session and used Id. (Used by Dialer vendor)")
	ResponseEntity<ResponseMessage> getDhanDispostionBySession(@RequestParam("sessionId") String sessionId,@RequestParam("userId") String userId){
		return new ResponseEntity<ResponseMessage>(service.getDispositionBySessionId(sessionId, userId),HttpStatus.OK);
	}
	
	@GetMapping("/getOutboundDetails")
	@ApiOperation("Get Outbound details via date range. (Used by marketing team)")
	ResponseEntity<ResponseMessage> getOutboundDetails(@Valid @RequestParam("fromDate") @NotEmpty(message = "From should not be empty.")  String fromDate,@Valid @RequestParam("toDate") @NotEmpty @NotBlank @NotNull(message = "To Date should not be empty.")  String toDate){
		return new ResponseEntity<ResponseMessage>(service.getOutboundDetails(fromDate, toDate),HttpStatus.OK);
	}
	
	@GetMapping("/getInboundDetails")
	@ApiOperation("Get Inbound details via date range. (Used by marketing team)")
	ResponseEntity<ResponseMessage> getInboundDetails(@Valid@RequestParam("fromDate") @NotEmpty(message = "From should not be empty.")  String fromDate,@Valid @RequestParam("toDate") @NotEmpty @NotBlank @NotNull(message = "To Date should not be empty.")  String toDate){
		return new ResponseEntity<ResponseMessage>(service.getInboundDetails(fromDate, toDate),HttpStatus.OK);
	}
	
	@GetMapping("/getRddCityDetails")
	@ApiOperation("Get lead create date and relief status via dhan id. (Used in crm)")
	ResponseEntity<List<Map<String, Object>>> getRddCityDetails(@Valid@RequestParam("dhanId") @NotEmpty(message = "Dhanvantariid should not be empty.")  String dhanId){
		return new ResponseEntity<List<Map<String, Object>>>(service.getRddCityData(dhanId),HttpStatus.OK);
	}
	
	@PostMapping("/createContact")
	@ApiOperation("Create new contact based in mobile number. (Used in crm)")
	String createNewContact(@RequestBody CreateContactDto contactDto){
		return service.createNewContact(contactDto);
	}
	

	@PostMapping("/saveCaseNotes")
	@ApiOperation("Save notes in dhanvantari. (Used in crm)")
	String saveCaseNotes(@RequestBody CaseNotesDto notesDto) {
		if(notesDto.getCaseId() == null || notesDto.getCaseId().length() == 0) {
			return "Case id is required";
		}else {
			return service.addCaseNotes(notesDto);
		}
	}
	
	@PostMapping("/moveClinicFreshCall")
	@ApiOperation("Send fresh call to adroid. (Used in crm)")
	public ResponseEntity<ResponseMessage> moveClinicFreshToAdroid(@Valid @RequestBody ClinicFreshCallDto callDto) {
		return new ResponseEntity<ResponseMessage>(service.saveClinicCallToAdroid(callDto),HttpStatus.OK);
	}	
	
	
}
