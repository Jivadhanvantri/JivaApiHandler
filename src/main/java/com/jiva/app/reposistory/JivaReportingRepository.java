package com.jiva.app.reposistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jiva.app.utils.helper;

@Repository
public class JivaReportingRepository {

	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyDbTemplate;

	public Page<?> getAllInfertilityReferralCase(String startDt,String endDt,String status,Pageable page) {
		
		String extraParam="";
		
		if(status.equals("1")) {
			extraParam=" AND DISPOSITION='converted' ";
		}else if(status.equals("2")) {
			extraParam=" AND DISPOSITION='nonconverted' ";
		}else if(status.equals("3")) {
			extraParam=" AND SUB_DISPOSITION='Closed' ";
		}

		String sql="SELECT CASE_ID,PATIENT_NAME,CLINIC_NAME,DOC_NAME,DATE_FORMAT(CREATE_DT,'%d %b %Y') AS CREATE_DT ,DISPOSITION,SUB_DISPOSITION FROM SCC_JIVA_INFERTILITY_REFERRAL_T WHERE CREATE_DT >= '"+startDt +"' and CREATE_DT<='"+endDt+"' "+extraParam+" LIMIT "+page.getPageSize()
		           + " OFFSET "+page.getOffset();
		

		List<Map<String, Object>> responseDto=synchronyDbTemplate.queryForList(sql);
		
		return new PageImpl<>(responseDto, page,getAllInfertilityReferralCaseCount(startDt,endDt,extraParam) );
	}
	
	private int getAllInfertilityReferralCaseCount(String startDt,String endDt,String extraParam) {
		
		String sql="SELECT COUNT(ID)  FROM SCC_JIVA_INFERTILITY_REFERRAL_T WHERE CREATE_DT >='"+startDt+"' AND CREATE_DT <='"+endDt+"' "+extraParam;
		
		return synchronyDbTemplate.queryForObject(sql,new Object[] {},Integer.class);
	}
	
	public Map<String,Object> getInfertilityReferralCase(String caseId){
		
		String sql="SELECT * FROM SCC_JIVA_INFERTILITY_REFERRAL_T WHERE CASE_ID='"+caseId+"'";
		
		List<Map<String,Object>> list=synchronyDbTemplate.queryForList(sql);
		Map<String,Object> responseDto=new HashMap<String,Object>();
		
		if(list.size()>0) {
			sql="SELECT MIDDLE_NAME AS AGE,SSN AS WEIGHT,MOM_MAIDEN_NAME AS HEIGHT,IFNULL(CONTACT_TITLE,'.') AS THYROID,"
					+ "(CASE WHEN GENDER='M' THEN 'Male' WHEN GENDER='F' THEN 'Female' WHEN GENDER='U' THEN 'Other' END) as Gender,"
					+ "SITE_USER_NAME AS BP,IFNULL(SITE_PASSWORD,'NA') as Suger FROM SCC_CASE_CONTACT_T A, SCC_CONTACT_T C, SCC_PERSON_T P ,SCC_CONTACT_EXTENTION_T E "
					+ "WHERE C.PERSON_ID=P.PERSON_ID AND C.CONTACT_ID=E.CONTACT_ID AND A.CONTACT_ID=C.CONTACT_ID AND A.CASE_ID='"+caseId+"'";
			
			List<Map<String, Object>> responseDto2=synchronyDbTemplate.queryForList(sql);
			
			responseDto.putAll(list.get(0));
			responseDto.putAll(responseDto2.get(0));
			
			sql="SELECT ABS(SUM(CASE WHEN BILLING_TYPE='Billing' THEN ADV_AMOUNT ELSE 0 END)) AS Adjustment, "
					+ "ABS(SUM(CASE WHEN BILLING_TYPE='Receipt' THEN ADV_AMOUNT ELSE 0 END)) as AmountPaid, "
					+ "ABS((ABS(SUM(CASE WHEN BILLING_TYPE='Billing' THEN ADV_AMOUNT ELSE 0 END))-ABS(SUM(CASE WHEN BILLING_TYPE='Receipt' THEN ADV_AMOUNT ELSE 0 END)))) as Balanced "
					+ "FROM SCC_JIVA_ADVANCE_PAYMENT_T WHERE CASE_ID='"+caseId+"'";
			
			List<Map<String,Object>> responseDto3=synchronyDbTemplate.queryForList(sql);
			responseDto.putAll(responseDto3.get(0));
		}
		
		// Calculate BMI
	    Long bmi=helper.calculateBMI(responseDto.get("HEIGHT").toString(),responseDto.get("WEIGHT").toString());
	    responseDto.put("BMI", bmi);
			
		return responseDto;
	}


	public String updateInfertilityStatus(Map<String, String> reqParam) {
		
	   String sql="UPDATE SCC_JIVA_INFERTILITY_REFERRAL_T SET DISPOSITION=?, SUB_DISPOSITION=?,DOC_ID=? WHERE CASE_ID=?";
		
		synchronyDbTemplate.update(sql,new Object[] { reqParam.get("disposition"),reqParam.get("subDisposition"),reqParam.get("docId"),reqParam.get("caseId") });
		
		return "Sucessfully Updated";
	}
	
	public Map<String,Object> getInfertilityCase(String caseId){
		Map<String,Object> responseDto=new HashMap<String,Object>();
		
		String sql="";
		sql="SELECT A.*,B.DISPOSITION,B.DOC_ID,B.SUB_DISPOSITION from SCC_JIVA_INFERTILITY_T A,SCC_JIVA_INFERTILITY_REFERRAL_T B WHERE A.CASE_ID=B.CASE_ID AND A.CASE_ID="+caseId;
		List<Map<String,Object>> obj1=synchronyDbTemplate.queryForList(sql);
		
		if(obj1.size()>0) {
			sql="SELECT IFNULL(CONTACT_TITLE,'.') AS THYROID,"
					+ "SITE_USER_NAME AS BP,IFNULL(SITE_PASSWORD,'NA') as SUGER FROM SCC_CASE_CONTACT_T A, SCC_CONTACT_T C, SCC_PERSON_T P ,SCC_CONTACT_EXTENTION_T E "
					+ "WHERE C.PERSON_ID=P.PERSON_ID AND C.CONTACT_ID=E.CONTACT_ID AND A.CONTACT_ID=C.CONTACT_ID AND A.CASE_ID='"+caseId+"'";
			
			List<Map<String, Object>> responseDto2=synchronyDbTemplate.queryForList(sql);
			
			responseDto.putAll(obj1.get(0));
			responseDto.putAll(responseDto2.get(0));
			
			sql="SELECT ABS(SUM(CASE WHEN BILLING_TYPE='Billing' THEN ADV_AMOUNT ELSE 0 END)) AS Adjustment, "
					+ "ABS(SUM(CASE WHEN BILLING_TYPE='Receipt' THEN ADV_AMOUNT ELSE 0 END)) as AmountPaid, "
					+ "ABS((ABS(SUM(CASE WHEN BILLING_TYPE='Billing' THEN ADV_AMOUNT ELSE 0 END))-ABS(SUM(CASE WHEN BILLING_TYPE='Receipt' THEN ADV_AMOUNT ELSE 0 END)))) as Balanced "
					+ "FROM SCC_JIVA_ADVANCE_PAYMENT_T WHERE CASE_ID='"+caseId+"'";
			
			List<Map<String,Object>> responseDto3=synchronyDbTemplate.queryForList(sql);
			responseDto.putAll(responseDto3.get(0));
			
			sql="SELECT (CASE WHEN ADDR_TYPE_ID = 1001 THEN EADDRESS ELSE '' END) AS 'Phone1',"
					+ "(CASE WHEN ADDR_TYPE_ID = 1021 THEN EADDRESS ELSE '' END) AS 'Phone2',"
					+ "(CASE WHEN ADDR_TYPE_ID = 1041 THEN EADDRESS ELSE '' END) AS 'Phone3',(CASE WHEN ADDR_TYPE_ID = 3 THEN EADDRESS ELSE '' END) AS 'EmailId' FROM SCC_CONTACT_EADDRESS_T A,SCC_CASE_CONTACT_T B WHERE A.CONTACT_ID=B.CONTACT_ID AND\r\n"
					+ " B.CASE_ID = 34401 AND ADDR_TYPE_ID IN (1001, 1021, 1041,3) ORDER BY ADDR_TYPE_ID";
			
			List<Map<String,Object>> responseDto4=synchronyDbTemplate.queryForList(sql);
			
			responseDto4.get(0).entrySet().stream()
		      .forEach(e -> {
		    	  responseDto.put(e.getKey(),e.getValue());
		      });
	
			// Calculate BMI
		    Long bmi=helper.calculateBMI(responseDto.get("HEIGHT").toString(),responseDto.get("WEIGHT").toString());
		    responseDto.put("BMI", bmi);
		}
		
		
		return responseDto;
	}

	public String updateInfertilityRecord(Map<String, String> reqParam) {
		String caseId="",names="",values="",sql="",extraParams="";
		
		for (String key : reqParam.keySet()) {
			
		    if(key.equalsIgnoreCase("CASE_ID")) {
		    	caseId=reqParam.get(key);
		    	continue;
		    }
			if(extraParams.equals("")) {
				extraParams+=key+"='"+reqParam.get(key)+"'";		
			}else {
				extraParams+=","+key+"='"+reqParam.get(key)+"'";	
			}   
		    
		}
		
	
		sql="UPDATE SCC_JIVA_INFERTILITY_T SET "+extraParams+" WHERE CASE_ID="+caseId;
		
		synchronyDbTemplate.update(sql,new Object[] { });
		
		return "Sucessfully Updated";	
		
	}

	public Page<?> getAllInfertilityCase(Map<String, String> reqParam, Pageable page) {
		
		String extraParam="";
		
		if(reqParam.get("status").equals("1")) {
			extraParam=" AND DISPOSITION='converted' ";
		}else if(reqParam.get("status").equals("2")) {
			extraParam=" AND DISPOSITION='nonconverted' ";
		}else if(reqParam.get("status").equals("3")) {
			extraParam=" AND SUB_DISPOSITION='Closed' ";
		}
		
		String sql="SELECT B.CASE_ID AS 'CASE_ID',B.PATIENT_NAME,B.HLTH_COACH_NAME,DATE_FORMAT(B.CREATE_DT,'%d %b %Y') AS CREATE_DT,DISPOSITION,SUB_DISPOSITION "
				+ " FROM SCC_JIVA_INFERTILITY_REFERRAL_T A,SCC_JIVA_INFERTILITY_T B WHERE A.CASE_ID=B.CASE_ID AND DOC_ID='"+reqParam.get("docId")+"' AND B.CREATE_DT >= '"+reqParam.get("sDt") +"' and B.CREATE_DT<='"+reqParam.get("eDt")+"' "+extraParam+" LIMIT "+page.getPageSize()
        + " OFFSET "+page.getOffset();

		List<Map<String, Object>> responseDto=synchronyDbTemplate.queryForList(sql);
		
		return new PageImpl<>(responseDto, page,getAllInfertilityCaseCount(reqParam,extraParam) );

	}
	
	private int getAllInfertilityCaseCount(Map<String,String> reqParam,String extraParam) {
		
		String sql="SELECT COUNT(B.ID) FROM SCC_JIVA_INFERTILITY_REFERRAL_T A,SCC_JIVA_INFERTILITY_T B WHERE A.CASE_ID=B.CASE_ID AND DOC_ID='"+reqParam.get("docId")+"' "+extraParam+" AND B.CREATE_DT >= '"+reqParam.get("sDt") +"' and B.CREATE_DT<='"+reqParam.get("eDt")+"' ";
	
		return synchronyDbTemplate.queryForObject(sql,new Object[] {},Integer.class);
	}
	
	
	public Page<?> getAllInfertilityFollowupCase(Map<String, String> reqParam, Pageable page) {
		String sql="SELECT CASE_ID,FOLLOW_UP_NOTES,GETUSERNAME(CREATE_ID) AS CREATE_BY,DATE_FORMAT(CREATE_DT,'%d %b %Y') AS CREATE_DT FROM SCC_JIVA_INFERTILITY_FOLLOWUP_T S WHERE CASE_ID='"+reqParam.get("caseId")+"' LIMIT "+page.getPageSize()
        + " OFFSET "+page.getOffset();

		List<Map<String, Object>> responseDto=synchronyDbTemplate.queryForList(sql);
		
		return new PageImpl<>(responseDto, page,getAllInfertilityFollowupCaseCount(reqParam) );

	}
	
	private int getAllInfertilityFollowupCaseCount(Map<String,String> reqParam) {
		
		String sql="SELECT COUNT(ID) FROM SCC_JIVA_INFERTILITY_FOLLOWUP_T WHERE CASE_ID ='"+reqParam.get("caseId")+"' ";
	
		return synchronyDbTemplate.queryForObject(sql,new Object[] {},Integer.class);
	}
	
}
