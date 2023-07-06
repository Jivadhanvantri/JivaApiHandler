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
public class GlobalRepo {

	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyDbTemplate;
	
	@Autowired
	private SynchronyReposistory syncRepo;
	
	@Autowired
	@Qualifier("jivaTemplate")
	private JdbcTemplate jivaTemplate;
	
	Map<String,Object> getPatientInfo(String caseId){
		Map<String,Object> responseDto=new HashMap<String,Object>();
		String sql = "SELECT CASE_ID ,GETPATIENTNAME(A.CONTACT_ID) as PATIENT_NAME,MIDDLE_NAME as 'AGE',SSN as 'WEIGHT',MOM_MAIDEN_NAME as 'HEIGHT',GENDER as 'GENDER' "
				+ "FROM SCC_CASE_CONTACT_T A,SCC_CONTACT_T B ,SCC_PERSON_T C WHERE A.CONTACT_ID=B.CONTACT_ID AND B.PERSON_ID=C.PERSON_ID AND CASE_ID = '"
				+ caseId + "'";

		List<Map<String, Object>> objlist1 = synchronyDbTemplate.queryForList(sql);
		responseDto=helper.mapListToObject(responseDto, objlist1);
		
		sql=" SELECT CC_WHERE_NAME as CHIEF_COMPLAINT FROM SCC_JIVA_CONSULT_FORM_CC_T C, SCC_JIVA_CONSULT_FORM_T F, SCC_JIVA_CC_WHERE_T W WHERE "
				+ " C.FORM_ID=F.FORM_ID AND C.CC_WHERE_ID=W.CC_WHERE_ID AND CASE_ID="+caseId;
		
		List<Map<String, Object>> objlist2 = synchronyDbTemplate.queryForList(sql);
		responseDto=helper.mapListToObject(responseDto, objlist2);
		
		sql="SELECT C.DISEASE_WHERE_NAME FROM SCC_JIVA_CONSULT_FORM_T A, SCC_JIVA_CONSULT_FORM_DIS_T B, SCC_JIVA_DISEASE_WHERE_T C,"
				+ "  SCC_JIVA_DISEASE_WHAT_T F WHERE A.FORM_ID=B.FORM_ID "
				+ "  AND B.DIS_WHERE_ID=C.DISEASE_WHERE_ID AND B.DIS_WHAT_ID=F.DISEASE_WHAT_ID AND A.CASE_ID="+caseId;
		
		List<Map<String, Object>> objlist3 = synchronyDbTemplate.queryForList(sql);
		responseDto=helper.mapListToObject(responseDto, objlist3);
		
		return responseDto;
	}
	
	public List<Map<String,Object>> getUserByChatName(Object obj){
		String extraParam = "";
		String chatname=String.valueOf(obj);

			if (chatname.equalsIgnoreCase("doc")) {
				extraParam += " AND CHAT_NAME LIKE '%doc%'";
			} else {
				extraParam += " AND CHAT_NAME IN ('" + chatname + "')";
			}
		
		String sql="SELECT USER_ID,GETUSERNAME(USER_ID) AS USER_NAME,CHAT_NAME FROM SCC_USER_T WHERE ENABLED_YN=1 "+extraParam;
		
		return synchronyDbTemplate.queryForList(sql);

	}

	public Map<String, Object> supportDetails_repo_get(Map<String, Object> reqParam) {
		// TODO Auto-generated method stub
		Map<String,Object> responseDto=new HashMap();
        String sql="SELECT EXTENTION, DOC_PHONE FROM SCC_JIVA_PROGRAM_DOCTOR_LIST_T S WHERE DOC_ID= "+reqParam.get("userId");
		
		List<Map<String,Object>> obj1= synchronyDbTemplate.queryForList(sql);
		responseDto=helper.mapListToObject(responseDto, obj1);
		return responseDto;
		
	}
	
	public Map<String,Object> whatsappPatientDetails(String caseId){
		Map<String,Object> responseDto=new HashMap();
		
		String whatsappNo = getWhatsappNoByDhanId(caseId);
		if(null == whatsappNo || whatsappNo.length() <4) {
			whatsappNo = getPoCustomerNumber(caseId);
		}
		responseDto.put("whatsappNo", whatsappNo);
		
        String sql="SELECT GETPATIENTNAME(CONTACT_ID) as patientName FROM SCC_CASE_CONTACT_T WHERE CASE_ID='"+caseId+"'";
		
		List<Map<String,Object>> obj1= synchronyDbTemplate.queryForList(sql);
		responseDto=helper.mapListToObject(responseDto, obj1);
		return responseDto;
	}


	public String getWhatsappNoByDhanId(String dhanId) {
		try {
			String sql="SELECT ACTUAL_NO FROM SCC_JIVA_WHATSAPP_CONSENT_T WHERE CASE_ID="+dhanId+" ORDER BY ID DESC LIMIT 1";
			return synchronyDbTemplate.queryForObject(sql,new Object[] {},String.class);
		}catch(Exception e) {
			return null;
		}
	}
	
	public String getPoCustomerNumber(String dhanId) {
		try {
			String sql="SELECT (CASE "+
					"WHEN CASE_DESC='1,0,0' THEN (SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T C WHERE C.CONTACT_ID=B.CONTACT_ID AND ADDR_TYPE_ID=1001 ORDER BY EADDR_ID DESC LIMIT 1) "+
					"WHEN CASE_DESC='0,1,0' THEN (SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T C WHERE C.CONTACT_ID=B.CONTACT_ID AND ADDR_TYPE_ID=1021 ORDER BY EADDR_ID DESC LIMIT 1) "+
					"WHEN CASE_DESC='0,0,1' THEN (SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T C WHERE C.CONTACT_ID=B.CONTACT_ID AND ADDR_TYPE_ID=1041 ORDER BY EADDR_ID DESC LIMIT 1) "+
					"WHEN CASE_DESC='' OR CASE_DESC IS NULL OR CASE_DESC='Follow Up Set' THEN (SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T C WHERE C.CONTACT_ID=B.CONTACT_ID AND ADDR_TYPE_ID=1001 ORDER BY EADDR_ID DESC LIMIT 1) "+
					"ELSE '' END) FROM SCC_CASE_T A,SCC_CASE_CONTACT_T B  WHERE A.CASE_ID=B.CASE_ID AND A.CASE_ID="+dhanId;
			return synchronyDbTemplate.queryForObject(sql,new Object[] {},String.class);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
