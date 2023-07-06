package com.jiva.app.reposistory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jiva.app.dtos.MvtDto;
import com.jiva.app.dtos.MvtResponseDto;


@Repository
public class MvtRepository {


	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyDbTemplate;
	
	public Page<?> findAllMvtUsers(String startDt, String endDt, Pageable page,String id,String status) {
		
		String extraParam="";
		
		if(id!="") {
			extraParam=" APP_CREATE_ID='"+id+"' AND ";
		}
	
		if(!status.equals("3")) {
			extraParam+=" STATUS="+status +" AND ";
		}
		
		String sql="SELECT ID,PATIENT_NAME,DATE_FORMAT(CREATE_DT,'%d %b %Y %h:%i %p') AS CREATE_DT,STATUS,APP_CREATE_ID CREATE_BY FROM SCC_JIVA_JIVAGRAM_MVT_T A "
				+ " WHERE "+extraParam+" CREATE_DT > '" + startDt + "' and CREATE_DT < '" + endDt  + "' LIMIT " + page.getPageSize()
				+ " OFFSET " + page.getOffset();
		
		List<MvtResponseDto> responseDto = synchronyDbTemplate.query(sql, new Object[] {},
				new BeanPropertyRowMapper(MvtResponseDto.class));
        
		return new PageImpl<>(responseDto, page,getMvtUserCount(startDt,endDt,id,status) );
	}
	
	public void saveAgent(MvtDto mvt) {
		String sql="INSERT INTO SCC_JIVA_JIVAGRAM_MVT_T (PATIENT_NAME,AGE,PHONE_NO,EMAIL,COUNTRY,STATE,REMARKS,LOOKING_FOR,PATIENT_TYPE,GENDER,APP_CREATE_ID,CREATE_DT,CASE_ID)"
				+ " VALUES( '"+mvt.getPatientName()+"',"+mvt.getAge()+",'"+mvt.getPhoneNo()+"','"+mvt.getEmail()+"','"+mvt.getCountry()+"','"+mvt.getState()+"', '"
						+ mvt.getRemarks()+"','"+mvt.getLookingFor()+"','"+mvt.getPatientType()+"','"+mvt.getGender()+"','"+mvt.getAppCreateId()+"',NOW(),"+mvt.getCaseId()+" ) ";
		
		synchronyDbTemplate.update(sql);
	}
	
	public int getMvtUserCount(String startDt, String endDt,String id,String status) {
		String extraParam="";
		
		if(id!="") {
			extraParam=" AND APP_CREATE_ID='"+id+"'";
		}
	
		if(!status.equals("3")) {
			extraParam+=" AND STATUS="+status;
		}
		
		String sql="SELECT COUNT(ID) FROM SCC_JIVA_JIVAGRAM_MVT_T A WHERE CREATE_DT >'" + startDt +"' AND CREATE_DT <'" + endDt + "'"+extraParam;
		
		return synchronyDbTemplate.queryForObject(sql, new Object[] {}, Integer.class);
	}
		
	public void saveMvtByDoctor(String id,String patientName,String age,String phoneNo,String email,String country,String state,String remarks,String lookingFor,String patientType,
		String gender,String recommedation,String docRemarks,String docCreateId) {
		
		String sql="UPDATE SCC_JIVA_JIVAGRAM_MVT_T SET PATIENT_NAME='"+patientName+"', AGE="+age+", PHONE_NO="+phoneNo+", EMAIL='"+email+"', COUNTRY='"+country+"', STATE='"+state
				+"', REMARKS='"+remarks+"', LOOKING_FOR='"+lookingFor+"', PATIENT_TYPE='"+patientType+"', GENDER='"+gender+"', RECOMMENDATION='"+recommedation+"', DOC_REMARKS='"+docRemarks+"',"
				+ " APP_DOC_CREATE_ID='"+docCreateId+"',STATUS=1 WHERE ID="+id;
		
		synchronyDbTemplate.update(sql);
	}

	public List<MvtDto> findById(Long id) {
		// TODO Auto-generated method stub

		String sql="SELECT * FROM SCC_JIVA_JIVAGRAM_MVT_T WHERE ID="+id;
		List<MvtDto> responseDto=synchronyDbTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper(MvtDto.class));		
		
		return responseDto;
	}

	public List<Map<String,Object>> getAllFiles(String caseId) {
		String sql="SELECT FILE_NAME_1, FILE_NAME_2, FILE_NAME_3, FILE_NAME_4, FILE_NAME_5, FILE_NAME_6, FILE_NAME_7, "
				+ " FILE_NAME_8, FILE_NAME_9, FILE_NAME_10 "
				+ "FROM SCC_JIVA_FILE_LOADER_T WHERE  PROGRAM_TYPE='mvt' AND CASE_ID="+caseId;
		List<Map<String,Object>> responseDto=synchronyDbTemplate.queryForList(sql);		
		
		return responseDto;
	}
	 
}
