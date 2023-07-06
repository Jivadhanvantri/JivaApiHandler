package com.jiva.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JivaReportingService {

	Page<?> getAllInfertilityReferralCase(String startDt, String endDt, int pageIndex, int pageSize, String status)
			throws Exception;
	
	Map<String,Object> getInfertilityReferralCase(String caseId);

	String updateInfertilityReferralStatus(Map<String, String> reqParam);

	Map<String, Object> getInfertilityCase(String caseId);

	String updateInfertilityRecord(Map<String, String> reqParam);

	Page<?> getAllInfertilityCase(Map<String, String> reqParam) throws Exception;

	Page<?> getAllInfertilityFollowupCase(Map<String, String> reqParam) throws Exception;

}
