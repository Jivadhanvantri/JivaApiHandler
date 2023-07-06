package com.jiva.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiva.app.service.GlobalService;
import com.jiva.app.service.NutritionistService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/")
public class GlobalController {

	@Autowired
	private GlobalService globalService;
	
	@GetMapping("/global/usersbychatname/get")
	public ResponseEntity<?> userNameByChatname_get(@RequestParam Map<String,Object> reqPram){
		return ResponseEntity.ok(globalService.userNameByChatname_service_get(reqPram));
	}
	
	@GetMapping("/global/support/details/get")
	public ResponseEntity<?> supportDetails_get(@RequestParam Map<String,Object> reqPram){
		return ResponseEntity.ok(globalService.supportDetails_service_get(reqPram));
	}
	
}
