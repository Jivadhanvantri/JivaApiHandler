package com.jiva.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiva.app.service.NutritionistService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/")
public class NutritionistController {

	@Autowired
	private NutritionistService nutriService;
	
	@GetMapping("/form/disposition/get")
	public ResponseEntity<?> dispositionByForm_get(@RequestParam Map<String,Object> reqPram){
		return ResponseEntity.ok(nutriService.dispositionByForm_service_get(reqPram));
	}
	
	@GetMapping("/form/subDisposition/get")
	public ResponseEntity<?> subDispositionByForm_get(@RequestParam Map<String,Object> reqPram){
		return ResponseEntity.ok(nutriService.subDispositionByForm_service_get(reqPram));
	}
	
	@GetMapping("/form/callbackdetails/get")
	public ResponseEntity<?> callbackdetails_get(@RequestParam Map<String,Object> reqPram){
		return ResponseEntity.ok(nutriService.callbackdetails_service_get(reqPram));
	}
	
}
