package com.jiva.app.reposistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jiva.app.utils.helper;

@Repository
public class NutritionistRepo {

	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyDbTemplate;

	@Autowired
	@Qualifier("jivaTemplate")
	private JdbcTemplate jivaTemplate;
	
	
	public List<Map<String,Object>> dispositionByForm_repo_get(Map<String,Object> reqParam){
		
		if(reqParam.get("formId").toString().equalsIgnoreCase("MRCDOCTOR")) {
			reqParam.put("formId","nutritionist");
		}
		
		String sql="SELECT B.* FROM SCC_JIVA_ADD_FORM_T A,SCC_JIVA_ADD_FORM_DISPOSTION_T B WHERE A.STATUS=1 AND A.FORM_NAME='"+reqParam.get("formId")+"' "
				+ " AND A.FORM_ID=B.FORM_ID AND B.STATUS=1";
		return synchronyDbTemplate.queryForList(sql); 
		
	}

	public List<Map<String, Object>> subDispositionByForm_repo_get(Map<String, Object> reqParam) {
		String sql="SELECT * FROM SCC_JIVA_ADD_FORM_SUB_DISPOSTION_T WHERE DIS_ID="+reqParam.get("dispositionId")+" AND STATUS=1";
		
		return synchronyDbTemplate.queryForList(sql); 
	}
	

	public Map<String, Object> callbackdetails_repo_get(Map<String,Object> reqParam) {
		// TODO Auto-generated method stub
		Map<String,Object> responseDto=new HashMap();
		String sql="SELECT COUNT(CALLBACK_ID) as CALLBACK_COUNT FROM SCC_CALLBACK_T WHERE USER_ID=" + reqParam.get("userId") + " AND CALLBACK_PRIORITY=1 AND CALLBACK_STATE=2";
		responseDto=helper.mapListToObject(responseDto, synchronyDbTemplate.queryForList(sql));
		
		String chatname=String.valueOf(reqParam.get("chatname"));
		
		if (chatname.equalsIgnoreCase("nutritionist") || chatname.equalsIgnoreCase("yogatherapist") || chatname.equalsIgnoreCase("mindwellbeingcoach")) {
			sql="SELECT SHIFT_MONTH,SHIFT_TIMING FROM SCC_JIVA_PROGRAM_DOCTOR_LIST_T WHERE DOC_ID="+reqParam.get("userId");
			responseDto=helper.mapListToObject(responseDto, synchronyDbTemplate.queryForList(sql));
		} else {
			sql="SELECT CONCAT(SHIFT_TIME_FROM,' ',SHIFT_TIME_FROM_AM_PM,' to ',SHIFT_TIME_TO,' ',SHIFT_TIME_TO_AM_PM) as SHIFT_TIMING,DATE_FORMAT(NOW(),'%b-%Y') AS SHIFT_MONTH FROM SCC_JIVA_TARGET_T WHERE USER_ID="+reqParam.get("userId")
			+" AND MONTH_FILTER=DATE_FORMAT(NOW(),'%b-%Y')";
			responseDto=helper.mapListToObject(responseDto, synchronyDbTemplate.queryForList(sql));
		}
		
		
		sql="SELECT GETUSERNAME(A.USER_ID) as ASSIGN_TO, "
				+ "DATE_FORMAT(A.DUE_DT,'%d %b %Y %h:%i %p') AS DUE_DT "
				+ " FROM SCC_CALLBACK_T A,SCC_USER_T U  WHERE U.USER_ID=A.USER_ID AND A.CASE_ID ="+reqParam.get("caseId")+"  AND A.CALLBACK_STATE IN (0, 2) "
				+ " ORDER BY A.CALLBACK_ID DESC";
		List<Map<String,Object>> duelist=synchronyDbTemplate.queryForList(sql);
		responseDto.put("duelist", duelist);
		return responseDto;
		
	}
	
}
