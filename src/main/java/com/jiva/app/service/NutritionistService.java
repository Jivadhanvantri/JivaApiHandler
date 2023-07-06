package com.jiva.app.service;

import java.util.List;
import java.util.Map;

public interface NutritionistService {

	List<Map<String,Object>> dispositionByForm_service_get(Map<String,Object> reqParam);
	
	List<Map<String,Object>> subDispositionByForm_service_get(Map<String,Object> reqParam);
	
	Map<String,Object> callbackdetails_service_get(Map<String, Object> reqParam);
}
