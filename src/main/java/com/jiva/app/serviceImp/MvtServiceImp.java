package com.jiva.app.serviceImp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jiva.app.dtos.MvtDto;
import com.jiva.app.dtos.ResponseHandler;
import com.jiva.app.reposistory.MvtRepository;
import com.jiva.app.service.MvtService;
import com.jiva.app.utils.helper;

@Service
public class MvtServiceImp implements MvtService{

	
	@Autowired
	private MvtRepository mvt_Repo;
	
	
	@Override
	public ResponseHandler saveMvtForm(MvtDto mvt) {
		// TODO Auto-generated method stub
		ResponseHandler responseHandler = new ResponseHandler();
		mvt_Repo.saveAgent(mvt);
		responseHandler.setStatusCode(false);
		responseHandler.setStatus(200);
		responseHandler.setMessage("Successfully created.");
		
		return responseHandler;
	}
	
	@Override
	public List<MvtDto> getMvtDataById(Long id) {
		// TODO Auto-generated method stub
		return mvt_Repo.findById(id);
	}
	
	
	@Override
	public Page<?> getAllMvtUsers(String startDt, String endDt, int pageIndex, int pageSize,String id,String status)
			throws Exception {
		String sDt = helper.setDateFormat(startDt) + " 00:00:00";
		String eDt = helper.setDateFormat(endDt) + " 23:59:59";
		Pageable paging = PageRequest.of(pageIndex, pageSize);
		Page<?> pageResult = null;
		
		pageResult = mvt_Repo.findAllMvtUsers(sDt, eDt,paging,id,status);
		return pageResult;
	}


	
	@Override
	public ResponseHandler saveMvtFormByDoctor(MvtDto mvt) {
		 //  mvtRepo.saveMvtByDoctor(mvt.getPatientName());
		
		ResponseHandler responseHandler = new ResponseHandler();

		 mvt_Repo.saveMvtByDoctor(mvt.getId().toString(),mvt.getPatientName(),mvt.getAge(),mvt.getPhoneNo(),mvt.getEmail(),mvt.getCountry(),
		    		mvt.getState(),mvt.getRemarks(),mvt.getLookingFor(),mvt.getPatientType(),mvt.getGender(),mvt.getRecommendation(),mvt.getDocRemarks(),mvt.getAppDocCreateId());
		 
		responseHandler.setStatusCode(false);
		responseHandler.setStatus(200);
		responseHandler.setMessage("Successfully created.");
		
		return responseHandler;
	    
	}

	@Override
	public List<Map<String,Object>> getAllMvtFiles(String caseId) {
		// TODO Auto-generated method stub
		ResponseHandler responseHandler = new ResponseHandler();
		List<Map<String,Object>> response= mvt_Repo.getAllFiles(caseId);		
		return response;
	}


}
