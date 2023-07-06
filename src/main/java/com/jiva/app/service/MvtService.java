package com.jiva.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.jiva.app.dtos.MvtDto;
import com.jiva.app.dtos.ResponseHandler;

public interface MvtService {

	  ResponseHandler saveMvtForm(MvtDto mvt);
	  ResponseHandler saveMvtFormByDoctor(MvtDto mvt);
	  List<MvtDto> getMvtDataById(Long createBy);
	  Page<?> getAllMvtUsers(String startDt, String endDt, int pageIndex, int pageSize,String id,String status) throws Exception;
	  List<Map<String,Object>> getAllMvtFiles(String caseId);	 
	    
}
