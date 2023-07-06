package com.jiva.app.serviceImp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiva.app.reposistory.GlobalRepo;
import com.jiva.app.reposistory.NutritionistRepo;
import com.jiva.app.service.GlobalService;

@Service
public class GlobalServiceImp implements GlobalService {

	@Autowired
	private GlobalRepo globalRepo;

	@Override
	public List<Map<String, Object>> userNameByChatname_service_get(Map<String, Object> reqParam) {
		// TODO Auto-generated method stub
		return globalRepo.getUserByChatName(reqParam.get("chatname"));
	}

	@Override
	public Map<String, Object> supportDetails_service_get(Map<String, Object> reqParam) {
		// TODO Auto-generated method stub
		return globalRepo.supportDetails_repo_get(reqParam);
	}
	
	

}
