package com.jiva.app.serviceImp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jiva.app.reposistory.JivaReportingRepository;
import com.jiva.app.service.JivaReportingService;
import com.jiva.app.utils.helper;

@Service
public class JivaReportingServiceImp implements JivaReportingService {

	@Autowired
	private JivaReportingRepository reportingRepo;

	@Override
	public Page<?> getAllInfertilityReferralCase(String startDt, String endDt, int pageIndex, int pageSize, String status) throws Exception {
		// TODO Auto-generated method stub
		
		String sDt = helper.setDateFormat(startDt)+ " 00:00:00";
		String eDt = helper.setDateFormat(endDt) + " 23:59:59";
		Pageable paging=PageRequest.of(pageIndex, pageSize);
		Page<?> pageResult = null;

		pageResult= reportingRepo.getAllInfertilityReferralCase(sDt, eDt, status, paging);
		return pageResult;
	}

	@Override
	public Map<String, Object> getInfertilityReferralCase(String caseId) {
		// TODO Auto-generated method stub
		return reportingRepo.getInfertilityReferralCase(caseId);
	}


	@Override
	public String updateInfertilityReferralStatus(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return reportingRepo.updateInfertilityStatus(reqParam);
	}
	
	@Override
	public Map<String, Object> getInfertilityCase(String caseId) {
		// TODO Auto-generated method stub
		return reportingRepo.getInfertilityCase(caseId);
	}

	@Override
	public String updateInfertilityRecord(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		return reportingRepo.updateInfertilityRecord(reqParam);
	}

	@Override
	public Page<?> getAllInfertilityCase(Map<String, String> reqParam) throws Exception {
		
		String sDt = helper.setDateFormat(reqParam.get("startDt"))+ " 00:00:00";
		String eDt = helper.setDateFormat(reqParam.get("endDt")) + " 23:59:59";
		Pageable paging=PageRequest.of(Integer.parseInt(reqParam.get("pageIndex")), Integer.parseInt(reqParam.get("pageSize")));
		Page<?> pageResult = null;
		reqParam.put("sDt", sDt);
		reqParam.put("eDt", eDt);

		pageResult= reportingRepo.getAllInfertilityCase(reqParam, paging);
		return pageResult;
	}
	
	@Override
	public Page<?> getAllInfertilityFollowupCase(Map<String, String> reqParam) throws Exception {
	
		Pageable paging=PageRequest.of(Integer.parseInt(reqParam.get("pageIndex")), Integer.parseInt(reqParam.get("pageSize")));
		Page<?> pageResult = null;

		pageResult= reportingRepo.getAllInfertilityFollowupCase(reqParam, paging);
		return pageResult;
	}

}
