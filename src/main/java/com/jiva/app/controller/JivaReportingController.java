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

import com.jiva.app.dtos.ResponseHandler;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.service.JivaReportingService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/")
public class JivaReportingController {
	@Autowired
	private JivaReportingService reportingService;
	@GetMapping("/infertility/referral/all")
	@ApiOperation("Get all infertility referral case. ( Used in JivaReporting Pannel)")
	public ResponseEntity<Page<?>> getInfertilityReferralAllCase(@RequestParam(name="startDt") String startDt,@RequestParam(name="endDt") 
	String endDt,@RequestParam(name="pageIndex") int pageIndex,@RequestParam(name="pageSize") int pageSize,@RequestParam(name="status",required=false) String status) throws Exception{
			
		Page<?> paging=reportingService.getAllInfertilityReferralCase(startDt, endDt, pageIndex, pageSize, status);
		return ResponseEntity.ok().body(paging);
	}
	
	@GetMapping("/infertility/referral/record/edit")
	@ApiOperation("Get infertility referral case via caseId. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> getInfertilityReferralRecord(@RequestParam("caseId") String caseId){
		
		Map<String,Object> list=reportingService.getInfertilityReferralCase(caseId);
		
		return ResponseEntity.ok().body(list);
	}
	
	@PostMapping("/infertility/referral/disposition/update")
	@ApiOperation("Update infertility referral record via caseId. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> updateInfertilityReferralRecordStatus(@RequestParam Map<String,String> reqParam){
		
		String status=reportingService.updateInfertilityReferralStatus(reqParam);
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Record Update Successfully.");
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
		
	}
	
	@GetMapping("/infertility/record/edit")
	@ApiOperation("Get infertility record via caseId. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> getInfertilityRecord(@RequestParam("caseId") String caseId){
	  
		Map<String,Object> list=reportingService.getInfertilityCase(caseId);
		
		return ResponseEntity.ok().body(list);
	}
	
	@PostMapping("/infertility/disposition/update")
	@ApiOperation("Update infertility Record disposition via caseId. ( Used in JivaReporting Pannel)")
	public ResponseEntity<?> updateInfertilityRecord(@RequestBody Map<String,String> reqParam){
		
		if(reqParam.get("CASE_ID").toString().equalsIgnoreCase("170725701")) {
			
			String status=reportingService.updateInfertilityRecord(reqParam);
		}	
		
		ResponseMessage responseHandler = new ResponseMessage();
		responseHandler.setStatusCode(200);
		responseHandler.setMessage("Record Update Successfully.");
		
		return new ResponseEntity<ResponseMessage>(responseHandler, HttpStatus.OK);
		
	}
	
	@GetMapping("/infertility/all")
	@ApiOperation("Get infertility Record via caseId. ( Used in JivaReporting Pannel)")
	public ResponseEntity<Page<?>> getInfertilityAllCase(@RequestParam Map<String,String> reqParam) throws Exception{
			
		Page<?> paging=reportingService.getAllInfertilityCase(reqParam);
		return ResponseEntity.ok().body(paging);
	}
	
	@GetMapping("/infertility/followup/all")
	@ApiOperation("Get infertility Followup record via caseId. ( Used in JivaReporting Pannel)")
	public ResponseEntity<Page<?>> getAllInfertilityFollowupCase(@RequestParam Map<String,String> reqParam) throws Exception{
			
		Page<?> paging=reportingService.getAllInfertilityFollowupCase(reqParam);
		return ResponseEntity.ok().body(paging);
	}
	
}
