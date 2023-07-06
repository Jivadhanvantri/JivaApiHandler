package com.jiva.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import com.jiva.app.dtos.MvtDto;
import com.jiva.app.dtos.ResponseHandler;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.dtos.WacDto;
import com.jiva.app.service.MvtService;
import com.jiva.app.service.SynchronyService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class MvtController {
	
	
	@Autowired
	private MvtService service;
	
	
	@Autowired
	private SynchronyService synchronyService;	
	
	@Autowired
    private Environment env;

	@PostMapping("/mvt/new")
	public ResponseEntity<ResponseHandler> enquiryForm(MvtDto mvt ) {
		
		try {
			
			if(mvt.getId()!=null) {
			   // Form Saved By Doctor
			
		      return new ResponseEntity<ResponseHandler>(service.saveMvtFormByDoctor(mvt), HttpStatus.OK);
			   		  
			}else {
			   // Form Saved By Agent
			  
			   return new ResponseEntity<ResponseHandler>(service.saveMvtForm(mvt), HttpStatus.OK);			  
			}
				
		  
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
       
	}
	
	@GetMapping("/mvt/edit")
	public ResponseEntity<?> getMvtData(@RequestParam("id") Long createBy){
		try {
			List<MvtDto> user=service.getMvtDataById(createBy);
			return ResponseEntity.ok().body(user);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/mvt/allusers")
	public ResponseEntity<Page<?>> getAllMvtUsers(@RequestParam(name="startDt") String startDt,@RequestParam(name="endDt") 
	String endDt,@RequestParam(name="pageIndex") int pageIndex,@RequestParam(name="pageSize") int pageSize,@RequestParam(name="id",required=false) String id,@RequestParam(name="status",required=false) String status){
		try {
			
			return new ResponseEntity<Page<?>>(service.getAllMvtUsers(startDt,endDt,pageIndex,pageSize,id,status),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/mvt/files")
	public List<Map<String,Object>> getAllMvtFiles(@RequestParam(name="caseId") String caseId){
		try {	
			return service.getAllMvtFiles(caseId);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@PostMapping("/registerUser")
	ResponseEntity<ResponseMessage> registerUser(@RequestBody WacDto wacDto){
		return new ResponseEntity<ResponseMessage>(synchronyService.createCustomDhanvantariid(wacDto),HttpStatus.OK);
	}
	
}
