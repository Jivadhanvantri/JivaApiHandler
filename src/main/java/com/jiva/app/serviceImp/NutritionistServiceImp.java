package com.jiva.app.serviceImp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiva.app.reposistory.NutritionistRepo;
import com.jiva.app.service.NutritionistService;


@Service
public class NutritionistServiceImp implements NutritionistService {

	@Autowired
	private NutritionistRepo nutriRepo;
	

	@Override
	public List<Map<String, Object>> dispositionByForm_service_get(Map<String, Object> reqParam) {
		// TODO Auto-generated method stub
		return nutriRepo.dispositionByForm_repo_get(reqParam);
	}


	@Override
	public List<Map<String, Object>> subDispositionByForm_service_get(Map<String, Object> reqParam) {
		// TODO Auto-generated method stub
		return nutriRepo.subDispositionByForm_repo_get(reqParam);
	}


	@Override
	public Map<String, Object> callbackdetails_service_get(Map<String, Object> reqParam) {
		// TODO Auto-generated method stub
		return nutriRepo.callbackdetails_repo_get(reqParam);
	}

}
