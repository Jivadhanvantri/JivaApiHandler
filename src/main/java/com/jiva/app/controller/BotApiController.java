package com.jiva.app.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiva.app.dtos.BotAvailableSlotDto;
import com.jiva.app.dtos.BotPaymentLinkGeneratorDto;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.dtos.WacDto;
import com.jiva.app.service.BotApiService;
import com.jiva.app.service.SynchronyService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/bot")
public class BotApiController {

	@Autowired
	private BotApiService botApiService;
	
	@Autowired
	private SynchronyService service;	
	
	@ApiOperation("Summary :  Api to check IS user register in Jiva or not || "
			+ "  Description: API to check if the user is register patient or not via phone number. ||"
			+ "  Params : Phone number * || "
			+ "  Developer : @pawan  ")
	@GetMapping("/check/isregister/{phoneno}")
	public ResponseEntity<ResponseMessage> checkIsRegister(@PathVariable("phoneno") String phoneno) {
		ResponseMessage response=botApiService.checkIsRegister_service(phoneno);
		       
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary :  API to get the nearby clinics with the help of PIN code || "
			+ "  Description: This api fetch nearby jiva clinics with th help of PIN code ||"
			+ "  Params : PinCode * || "
			+ "  Developer : @pawan  ")
	@GetMapping("/clinicbypincode/{pincode}")
	public ResponseEntity<ResponseMessage> clinicbypincode(@PathVariable("pincode") String pincode) {
		ResponseMessage response=botApiService.clinicbypincode_service(pincode);
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary :  API to get available time slots for a given date. || "
			+ "  Description: This api fetch clinic available time slots for booked appointment via given appointment Date ||"
			+ "  Params : groupName * ,  slotDate *  || "
			+ "  Developer : @pawan  ")
	@GetMapping("/availableSlot")
	public ResponseEntity<ResponseMessage> availableSlot(@RequestParam String groupName,@RequestParam String slotDate) throws Exception {
		ResponseMessage response=botApiService.availableSlot_service(groupName,slotDate);
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary :  API to get the diseases list || "
			+ "  Description: This api fetch all available dieseases list ||"
			+ "  Params : -- || "
			+ "  Developer : @pawan  ")
	@GetMapping("/diseaselist")
	public ResponseEntity<ResponseMessage> getDiseaselist() throws Exception {
		ResponseMessage response=botApiService.getDiseaselist_service();
	
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary :  API to get the State list || "
			+ "  Description: This api fetch all available clinic state list ||"
			+ "  Params : -- || "
			+ "  Developer : @pawan  ")
	@GetMapping("/clinic-state")
	public ResponseEntity<ResponseMessage> getClinicState() throws Exception {
		ResponseMessage response=botApiService.getClinicState_service();
	
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary :  API to get the City list || "
			+ "  Description: This api fetch all available clinic city list via state ||"
			+ "  Params : clinicState * || "
			+ "  Developer : @pawan  ")
	@GetMapping("/clinic-city")
	public ResponseEntity<ResponseMessage> getClinicCity(@RequestParam String state) throws Exception {
		ResponseMessage response=botApiService.getClinicCity_service(state);
	
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary : API to get the Clinic By City name || "
			+ "  Description: This api fetch all available jiva clinic via city lname ||"
			+ "  Params : city name * || "
			+ "  Developer : @pawan  ")
	@GetMapping("/clinicbycity")
	public ResponseEntity<ResponseMessage> getClinicByCityName(@RequestParam String city) throws Exception {
		ResponseMessage response=botApiService.getClinicByCityName_service(city);
	
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary : API to cancel the appointment || "
			+ "  Description: This api used to cancel clinic appointment. ||"
			+ "  Params : appointmentId * || "
			+ "  Developer : @pawan  ")
	@PostMapping("/cancelappointment")
	public ResponseEntity<ResponseMessage> cancelAppointment(@RequestParam String appointmentId) throws Exception {
		String agentId="138201"; // bot ID
		ResponseMessage response=botApiService.cancelAppointment_service(agentId,appointmentId);
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary : API to register a new patient and giving a patientId || "
			+ "  Description: This api register new user in Jiva and give register details. ||"
			+ "  Params : Refer to WacDto * || "
			+ "  Developer : @pawan  ")
	@PostMapping("/registerUser")
	ResponseEntity<ResponseMessage> registerUser(@RequestBody WacDto wacDto){
		return new ResponseEntity<ResponseMessage>(service.createDhanvantariid(wacDto),HttpStatus.OK);
	}
	
	@ApiOperation("Summary : API to send OTP || "
			+ "  Description: This api send OTP to register number if available. ||"
			+ "  Params : phoneno * || "
			+ "  Developer : @pawan  ")	
	@PostMapping("/sendOtp")
	ResponseEntity<ResponseMessage> sendOtp(@RequestParam String phoneno){
        ResponseMessage response=botApiService.sendOtp_service(phoneno);
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary : API to verify OTP || "
			+ "  Description: This api verify OTP send on register number. ||"
			+ "  Params : phoneno *, opt * || "
			+ "  Developer : @pawan  ")	
	@PostMapping("/verifyOtp")
	ResponseEntity<ResponseMessage> verifyOtp(@RequestParam String phoneno,@RequestParam String otp){
		 ResponseMessage response=botApiService.verifyOtp_service(phoneno, otp);
			
	     return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary : API to get Patient list by phone number/case id. || "
			+ "  Description: This api verify OTP send on register number. ||"
			+ "  Params : phoneno *, opt * || "
			+ "  Developer : @pawan  ")	
	@GetMapping("/patientlist")
	public ResponseEntity<ResponseMessage> getPatientlistByParam(@RequestParam(value="phoneno",required=false) String phoneno,
			@RequestParam(value="caseId",required=false) String caseId) {
		ResponseMessage response=botApiService.getPatientlistByParam_service(phoneno,caseId);
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary : API to get Patient Appointment list by phone number/case id. || "
			+ "  Description: This api all clinic Appointment list by phone number/case id. ||"
			+ "  Params : phoneno / caseId  || "
			+ "  Developer : @pawan  ")
	@GetMapping("/booked/appointmentlist")
	public ResponseEntity<ResponseMessage> getPatientAppointmentlist(@RequestParam(value="phoneno",required=false) String phoneno,
			@RequestParam(value="caseId",required=false) String caseId) {		
		ResponseMessage response=botApiService.getPatientAppointmentlist_service(phoneno,caseId);
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary : API to booked TC Appointment. || "
			+ "  Description: This api booked appointment for Consultation Type TC. ||"
			+ "  Params : caseId *  || "
			+ "  Developer : @pawan  ")	
	@PostMapping("/booked/mrcTC/appointment")
	public ResponseEntity<ResponseMessage> mrcTCAppointment_service(@RequestParam(value="caseId") String caseId) {		
		ResponseMessage response=botApiService.mrcTCAppointment_service(caseId);
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation("Summary : API to booked IMRC Appointment. || "
			+ "  Description: This api fetch booked appointment for IMRC VC. ||"
			+ "  Params : caseId *  || "
			+ "  Developer : @pawan  ")	
	@PostMapping("/booked/imrc/appointment")
	public ResponseEntity<ResponseMessage> bookedIMRCAppointment_service(@RequestParam(value="caseId") String caseId) throws Exception {		
		ResponseMessage response=botApiService.bookedIMRCAppointment_service(caseId);
		
		return ResponseEntity.ok().body(response);
	}
	
}
