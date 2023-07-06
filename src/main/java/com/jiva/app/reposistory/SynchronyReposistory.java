package com.jiva.app.reposistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import com.jiva.app.dtos.AdroidFirstConnectDto;
import com.jiva.app.dtos.AdroidResponseDto;
import com.jiva.app.dtos.AppointmetStatusData;
import com.jiva.app.dtos.ClinicAppointmentDto;
import com.jiva.app.dtos.ClinicApptRequestDto;
import com.jiva.app.dtos.ContactExtensionDto;
import com.jiva.app.dtos.DispositionResponseDto;
import com.jiva.app.dtos.ExtentionDetailsDto;
import com.jiva.app.dtos.FinalOutcomeDto;
import com.jiva.app.dtos.InboundDetailsDto;
import com.jiva.app.dtos.IperformanceObDto;
import com.jiva.app.dtos.NiceDetailsDto;
import com.jiva.app.dtos.OutboundDetailsDto;
import com.jiva.app.dtos.PatientDetailsDto;
import com.jiva.app.dtos.SMSDetails;
import com.jiva.app.dtos.ShareChatDto;
import com.jiva.app.dtos.WacDocDto;
import com.jiva.app.dtos.WhatsappRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SynchronyReposistory {

	Logger logger = LoggerFactory.getLogger(SynchronyReposistory.class);
	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyTemplate;
	
	@Autowired
	private JivaReposistory jivaRepo;
	
	@Autowired
	private NiceReposistory niceRepo;
	
	public List<PatientDetailsDto> getPatientDataDb(String dhanId){
		try {
			String sql="SELECT CONCAT(P.FIRST_NAME_DISPLAY,' ',P.LAST_NAME_DISPLAY) name, SHIPPING_ADDRESS_1 place, SHIPPING_CITY_ID cityId,"+
					 "SHIPPING_STATE_ID  stateId,SHIPPING_PINCODE pincode, SHIPPING_DISTRICT, SHIPPING_COUNTRY_ID countryId FROM "+
					 "SCC_JIVA_ORDER_FORM_T A "+
					 "join SCC_CONTACT_T C on A.CONTACT_ID = C.CONTACT_ID "+
					 "join SCC_PERSON_T P on C.PERSON_ID = P.PERSON_ID "+
					 "left outer join  SCC_JIVA_ORDER_FORM_ADDR_T B on A.ORDER_ID = B.ORDER_ID  WHERE A.CASE_ID = ? ORDER BY A.CREATE_DT DESC limit 1";
			return synchronyTemplate.query(sql,new Object[]{dhanId},new BeanPropertyRowMapper(PatientDetailsDto.class));
		}catch(Exception e) {
			logger.error("Error in getPatientDataDb ->"+e.getMessage());
			return null;
		}
	}
	
	public String createClinicAppointment(ClinicApptRequestDto request) {
		try {
			String sql="INSERT INTO SCC_JIVA_APPOINTMENT_T (APPOINTMENT_DT, CASE_ID, FIRST_NAME, EADDRESS, CLINIC_GRP,CREATE_ID,STATE,CREATE_DT) "+
					   "VALUES (?,?,?,?,?,?,0,NOW())";
			int r=synchronyTemplate.update(sql,new Object[] {request.getAptdate(),request.getDhandid(),request.getPtname(),request.getPhone(),request.getGroupname(),request.getCreateId()});
			if(r > 0)
				return "Record saved successfully";
			return "Failure!Record not saved";
		}catch(Exception e) {
			logger.error("Error in createClinicAppointment ->" + e.getMessage());
			return "Failure!Record not saved";
		}
	}
	
	public List<Map<String, Object>> getRddCityRepo(String dhanId){
		String sql="SELECT UNIX_TIMESTAMP(LEAD_CREATE_DT) as leadCreateDate,(CASE WHEN DIS_IMPROVEMENT_1=1 THEN '---' WHEN DIS_IMPROVEMENT_1=2 THEN 'Condition Aggravated' "+
				   "WHEN DIS_IMPROVEMENT_1=3 THEN 'Condidition Relieved' WHEN DIS_IMPROVEMENT_1=4 THEN 'Litte Improvement' WHEN "+
				    "DIS_IMPROVEMENT_1=5 THEN 'No Relief' WHEN DIS_IMPROVEMENT_1=6 THEN 'Significant Improvement' WHEN DIS_IMPROVEMENT_1=7 THEN 'Moderate Relief' ELSE '' END) as relief_status, "+
				   "(SELECT UNIX_TIMESTAMP(CREATE_DT) FROM SCC_CASE_T M WHERE M.CASE_ID=A.CASE_ID) AS caseCreateDt "+
				    "FROM SCC_JIVA_CONSULT_FORM_T A,SCC_JIVA_CONSULT_FOLLOWUP_T B,SCC_CONTACT_EXTENTION_T C "+
				    "WHERE A.FORM_ID=B.FORM_ID AND A.CONTACT_ID=C.CONTACT_ID AND  A.CASE_ID="+dhanId+" ORDER BY FOLLOWUP_ID DESC limit 1";
		return synchronyTemplate.queryForList(sql);
	}
	
	public String getConsultationStatus(String dhanId) {
		try {
			String sql="SELECT (CASE WHEN A.STATUS=1 THEN 'Open' ELSE 'Closed' end) FROM SCC_JIVA_CONSULT_FORM_T A, "+
					"SCC_JIVA_CONSULT_FOLLOWUP_T B WHERE A.CASE_ID = "+dhanId+" AND A.FORM_ID = B.FORM_ID GROUP BY A.FORM_ID, A.CREATE_DT, A.CREATE_NAME, A.STATUS ORDER BY A.FORM_ID DESC";
			return synchronyTemplate.queryForObject(sql,new Object[] {},String.class);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int saveIpOutboundDetails(IperformanceObDto ipOutbound) {
		try {
			String sql="INSERT INTO SCC_JIVA_IP_OUTBOUND_T(NAME,PHONE,CATEGOTY,CREATE_DT) "+
					   "VALUES(?,?,?,NOW())";
			return synchronyTemplate.update(sql,new Object[] {ipOutbound.getpName(),ipOutbound.getpPhone(),ipOutbound.getpCategory()});
		}catch(Exception e) {
			logger.error("Errror in saveIpOutboundDetails ->"+e.getMessage());
			return 0;
		}
	}
	
	public int saveShareChatDetails(ShareChatDto chatDetails) {
		try {
			String sql="INSERT INTO SCC_JIVA_SHARECHAT_DETAILS_T(SUBMIT_DT,WEB_CARD,NAME,MOBILE,CITY,CASE_ID,CONTACT_ID,SRC,CREATE_DT) "+
					"VALUES(?,?,?,?,?,?,?,?,NOW())";
			return synchronyTemplate.update(sql,new Object[] {chatDetails.getSUBMIT_DT(),chatDetails.getWEB_CARD(),chatDetails.getNAME(),chatDetails.getMOBILE(),chatDetails.getCITY(),chatDetails.getCASE_ID(),chatDetails.getCONTACT_ID(),chatDetails.getSRC()});
		}catch(Exception e) {
			logger.error("Errror in saveShareChatDetails ->"+e.getMessage());
			return 0;
		}
	}
	
	public void saveSmsDetails(SMSDetails smsDetails) {
		System.out.println(smsDetails);
		String sql= "INSERT INTO SCC_JIVA_SMS_T (PHONE, SMS_TEXT, CREATE_ID, CASE_ID, SYS_SESS_ID,SEND_STATUS, TRANSACTION_ID) " +
                "VALUES(?,?,?,?,?,?,?)";
		synchronyTemplate.update(sql,new Object[] {smsDetails.getPhoneNo(),smsDetails.getMsgText(),smsDetails.getCreateId(),smsDetails.getDhanId(),smsDetails.getSessionId(),smsDetails.getSendStatus(),smsDetails.getTransactionId()});
	}
	public void saveWacDetails(WacDocDto docDto) {
		String sql="INSERT INTO SCC_JIVA_WAC_DETAILS_T(CASE_ID,DOC_ID,VC_LINK,SEND_FOR,VC_STATUS,CREATE_DT) "+
					"VALUES(?,?,?,?,?,NOW())";
		synchronyTemplate.update(sql,new Object[] {docDto.getDhanId(),docDto.getDocId(),docDto.getVcLink(),docDto.getSendFor(),docDto.getVcStatus()});
	}
	public void saveWhatsappResponse(WhatsappRequest request) {
		 String sql = "INSERT INTO SCC_JIVA_WHATSAPP_INFO_T(ORDER_CODE,CASE_ID,WHATSAPP_ID,MOBILE_NO,STATUS,SENT_FOR,GROUP_ID,SMS_TEXT,CREATE_DT) "+
                "VALUES(?,?,?,?,?,?,?,?,now())";
		 synchronyTemplate.update(sql,new Object[] {request.getOrderCode(),request.getDhanId(),request.getWhatsappId(),request.getMobileNo(),request.getStatus(),request.getSentFor(),request.getGrpId(),request.getSmsText()});
	}
	public boolean isContactExist(String mobile) {
		String sql="SELECT COUNT(EADDRESS) FROM SCC_CONTACT_EADDRESS_T WHERE EADDRESS=?";
		return synchronyTemplate.queryForObject(sql,new Object[] {mobile},Integer.class)>0?true:false;
	}
	public int createInternalList(String caseId,String listName) {
		int insertCount = 0;
		String sql="SELECT CONTACT_ID FROM SCC_CASE_CONTACT_T WHERE CASE_ID="+caseId;
		String cntId= synchronyTemplate.queryForObject(sql,new Object[] {},String.class);
		if(cntId !=null && !cntId.equals("")) {
			sql="SELECT COUNT(LIST_ID) FROM SCC_JIVA_LIST_STAGING_T WHERE LIST_SRV_NAME='"+listName+"' AND CONTACT_ID='"+cntId+"' AND MOVED_YN='N'";
			System.out.println(sql);
			int oCount=synchronyTemplate.queryForObject(sql,new Object[] {},Integer.class);
			if(oCount ==0) {
				sql="INSERT INTO SCC_JIVA_LIST_STAGING_T(LIST_SRV_NAME, USER_ID, CAMP_ID, CONTACT_ID, CREATE_DT, MOVED_YN, CALLING_DT,SORT_KEY) VALUES ('"+listName+"',10001, 1001, '"+cntId+"', NOW(), 'N', NOW(),10)";
				System.out.println(sql);
				insertCount = synchronyTemplate.update(sql,new Object[] {});
			}
		}
		return insertCount;
	}
	public String getClinicUserName(String groupName) {
		try {
			String sql="select USER_NAME from SCC_JIVA_USER_GROUP_T inner join SCC_USER_T on SCC_USER_T.USER_ID=SCC_JIVA_USER_GROUP_T.USER_GRP_ID WHERE USER_GRP_NAME='"+groupName+"'";
			return synchronyTemplate.queryForObject(sql,new Object[] {},String.class);
		}catch(Exception e) {
			logger.error("Errror in getClinicUserName ->"+e.getMessage());
			return null;
		}
	}
	
	public Integer getClinicAppointmentCountByGroupName(ClinicAppointmentDto appt) {
		String sql="SELECT  COUNT(APPOINT_ID) as total FROM SCC_JIVA_APPOINTMENT_T WHERE CLINIC_GRP = ? and APPOINTMENT_DT=? AND STATE NOT IN (3,4)";
		return synchronyTemplate.queryForObject(sql,new Object[] {appt.getClinicGroup(),appt.getAppointmentDt()},Integer.class);
	}
	
	public String getPaymentUsername(String dhanId,String type,String userId) {
		String sql="";
		try {
			 if(type.equalsIgnoreCase("imrc")) {
				 sql="select GETUSERNAME(CREATE_ID) as username from SCC_JIVA_ADD_SUBDISPOSITION_T where DISPOSITION IN ('OC Payment Awaited') AND CASE_ID='"+dhanId+"' ORDER BY SDIS_ID DESC LIMIT 1";
			 }else if(type.equalsIgnoreCase("tmc")) {
				 sql="SELECT GETUSERNAME(CREATE_ID) as username  FROM SCC_JIVA_ADD_SUBDISPOSITION_T WHERE CASE_ID='"+dhanId+"' ORDER BY SDIS_ID DESC LIMIT 1";
			 }else if(type.equalsIgnoreCase("clinic")) {
				 sql="SELECT GETUSERNAME('"+userId+"') as username";
			 }
			return synchronyTemplate.queryForObject(sql,new Object[] {},String.class);
		}catch(Exception e) {
			logger.error("Error in getPaymentUsername ->"+e.getMessage());
			return null;
		}
	}
	
	
	public String wacSentFor() {
		try {
			String sql="SELECT IFNULL(SEND_FOR,'Sujeet') as sendFor FROM SCC_JIVA_WAC_DETAILS_T WHERE VC_STATUS=0 ORDER BY ID DESC LIMIT 1";
			return synchronyTemplate.queryForObject(sql,new Object[] {},String.class);
		}catch(Exception e) {
			logger.error("Error in wacSentFor ->"+e.getMessage());
			return null;
		}
	}
	
	public String getContactId(String mobile) {
		String sql="SELECT CONTACT_ID FROM SCC_CONTACT_EADDRESS_T WHERE EADDRESS=? ORDER BY EADDR_ID DESC LIMIT 1";
		return synchronyTemplate.queryForObject(sql,new Object[] {mobile},String.class);
	}
	public List<DispositionResponseDto> getDispositionData(String cntId,String userId){
		String sql="SELECT CASE_ID as dhanId,USER_ID as userId,A.CONTACT_ID as contactId,SYS_SESS_DESC as disposition,SESSION_ROUTE_DT as sessionRouteDt,SYS_SESS_ID as sessionId FROM SCC_SM_SYS_SESS_STATISTIC_T A,SCC_CASE_CONTACT_T B WHERE "
					+"A.CONTACT_ID=B.CONTACT_ID AND A.CONTACT_ID='"+cntId+"' AND A.USER_ID='"+userId+"' ORDER BY SYS_SESS_ID DESC LIMIT 1 ";
		return synchronyTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper(DispositionResponseDto.class));
	}
	
	public List<DispositionResponseDto> getDispositionBySession(String sessionId,String userId){
		String sql="SELECT CASE_ID as dhanId,USER_ID as userId,A.CONTACT_ID as contactId,SYS_SESS_DESC as disposition,SESSION_ROUTE_DT as sessionRouteDt,SYS_SESS_ID as sessionId FROM SCC_SM_SYS_SESS_STATISTIC_T A,SCC_CASE_CONTACT_T B WHERE "+
				"A.CONTACT_ID=B.CONTACT_ID AND SYS_SESS_ID='"+sessionId+"' AND A.USER_ID='"+userId+"'";
		return synchronyTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper(DispositionResponseDto.class));
	}
	
	
	
	
	public List<AppointmetStatusData> getAppointmentStatusData(String dhanId){
		try {
			String sql="SELECT CASE_ID dhanId, CONCAT(FIRST_NAME,' ', LAST_NAME) patientName, STATE appointmentStatus,USER_GRP_DESC clinicName,DATE_FORMAT(APPOINTMENT_DT,'%d %b %Y %h:%i %p') appointmentDate,A.CREATE_DT createDt FROM SCC_JIVA_APPOINTMENT_T A,"+
					"SCC_JIVA_USER_GROUP_T B WHERE A.CLINIC_GRP=B.USER_GRP_NAME AND CASE_ID=? AND FR_FLAG=1 AND USER_GRP_ID NOT IN ('0','1','11961') ORDER BY APPOINT_ID DESC LIMIT 1";
			return synchronyTemplate.query(sql,new Object[]{dhanId},new BeanPropertyRowMapper(AppointmetStatusData.class));
		}catch(Exception e) {
			logger.error("Error in getAppointmentStatusData ->"+e.getMessage());
			return null;
		}
	}
	
	public String getPhoneNoByType(String cntId,String addrTypeId) {
		try {
			String sql ="SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T WHERE CONTACT_ID=? AND ADDR_TYPE_ID=?";
			return synchronyTemplate.queryForObject(sql,new Object[] {cntId,addrTypeId},String.class);
		}catch(Exception e) {
			logger.error("Error in getPhoneNoByType ->"+e.getMessage());
			return null;
		}
	}
	
	
	
	public String getContactByCase(String caseId) {
		try {
			String  sql="SELECT CONTACT_ID FROM SCC_CASE_CONTACT_T WHERE CASE_ID=?";
			return synchronyTemplate.queryForObject(sql,new Object[] {caseId},String.class);
		}catch(Exception e) {
			logger.error("Error in getContactByCase ->"+e.getMessage());
			return null;
		}
	}
	
	public String getCaseId(String contactId) {
		try {
			String  sql="SELECT CASE_ID FROM SCC_CASE_CONTACT_T WHERE CONTACT_ID=?";
			return synchronyTemplate.queryForObject(sql,new Object[] {contactId},String.class);
		}catch(Exception e) {
			logger.error("Error in getCaseId ->"+e.getMessage());
			return null;
		}
	}
	public String getPatientName(String dhanId) {
		try {
			String sql="SELECT GETPATIENTNAME(CONTACT_ID) FROM SCC_CASE_CONTACT_T WHERE CASE_ID=?";
			return synchronyTemplate.queryForObject(sql,new Object[] {dhanId},String.class);
		}catch(Exception e) {
			logger.error("Error in getPatientName ->"+e.getMessage());
			return null;
		}
		
	}
	public void insertAdroidFirstConnectData(AdroidFirstConnectDto connectDto) {
		String sql="INSERT INTO SCC_JIVA_ADROID_FIRST_CONNECT_T (CASE_ID,CONTACT_ID,ASSIGNED_USER_ID,PHONE1,PHONE2,CREATE_ID,CREATE_DT) "+
				  "VALUES(?,?,?,?,?,?,now())";
		synchronyTemplate.update(sql,new Object[] {connectDto.getCaseId(),connectDto.getContactId(),connectDto.getAssignedUserId(),connectDto.getPhone1(),connectDto.getPhone2(),connectDto.getCreatId()});
	}
	
	public void insertShareChatExtentionDetails(ExtentionDetailsDto details) {
		String sql= "INSERT INTO SCC_CONTACT_EXTENTION_T(CONTACT_ID,FB_CITY_NAME,COMPAIGN_NAME,UTM_CAMPAIGNID,LEAD_CREATE_DT) "+
					"VALUES(?,?,?,?,NOW())";
		synchronyTemplate.update(sql,new Object[] {details.getCONTACT_ID(),details.getFB_CITY_NAME(),details.getCOMPAIGN_NAME(),details.getUTM_CAMPAIGNID()});
	}
	
	public void updateShareChatExtentionDetails(ExtentionDetailsDto details) {
		String sql="UPDATE SCC_CONTACT_EXTENTION_T SET FB_CITY_NAME=?,COMPAIGN_NAME=?,UTM_CAMPAIGNID=?,LEAD_CREATE_DT=NOW() WHERE CONTACT_ID=?";
		synchronyTemplate.update(sql,new Object[] {details.getFB_CITY_NAME(),details.getCOMPAIGN_NAME(),details.getUTM_CAMPAIGNID(),details.getCONTACT_ID()});	
	}
	
	public void insertAdroidResponse(AdroidResponseDto resDto) {
		String sql="INSERT INTO SCC_JIVA_ADROID_RESPONSE_T(RESPONSE,CASE_ID,CONTACT_ID,MOBILE_NO,CREATE_DT) "+
         	   "VALUES(?,?,?,?,NOW())";
		synchronyTemplate.update(sql,new Object[] {resDto.getAdroidResponse(),resDto.getCaseId(),resDto.getContactId(),resDto.getPhone()});
	}
	public List<ContactExtensionDto> getDiseaseDetails(String contactId){
		try {
			String sql="SELECT IFNULL(REPLACE(COMPAIGN_NAME,',',''),'') as compaignName,IFNULL(LEAD_CREATE_DT,'') as leadCreateDate,GENERAL_TECH as disease,OTHER_DISEASE as otherDisease,PINCODE as pincode,IFNULL(DOB,'') as dob FROM "
					  +"SCC_CONTACT_EXTENTION_T A,SCC_JIVA_AGENT_DISEASE_T B WHERE A.DISEASE=B.ID AND  CONTACT_ID='"+contactId+"'";
			return synchronyTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper(ContactExtensionDto.class));
		}catch(Exception e) {
			logger.error("Error in getDiseaseDetails ->"+e.getMessage());
			return null;
		}
	}
	
	public String getCaseCreateDt(String caseId) {
		try {
			String sql="SELECT CREATE_DT FROM SCC_CASE_T WHERE CASE_ID='"+caseId+"'";
			return synchronyTemplate.queryForObject(sql,new Object[] {},String.class);
		}catch(Exception e) {
			logger.error("Error in getCaseCreateDt ->"+e.getMessage());
			return null;
		}
	}
	
	public int isContacExtentiontExist(Long contactId) {
		String sql="SELECT COUNT(CONTACT_ID) FROM SCC_CONTACT_EXTENTION_T WHERE CONTACT_ID="+contactId;
		return synchronyTemplate.queryForObject(sql,new Object[] {},Integer.class);
	}
	
	public List<InboundDetailsDto> getInboundDashboard(String startDt,String endDt){
		String sql= "SELECT SESSION_ID,AGENT_NAME,WORK_TYPE,CREATE_DT,AGENT_ID,CONTACT_ID,OUTCOME,DIAL_DATE,ANS_DATE,DISCONNECT_DATE, "
					+"DIFF,CASE_ID,SUB_DIS,INCOMING_DID,SOURCE,JTC_ID,CASE_CREATE_DT,PATIENT_NAME,CAMPAIGN_NAME,LEAD_CREATE_DT,"
					+"DISEASE,PINCODE,DOB,TODAY_CREATE_DT FROM SCC_JIVA_INBOUND_DETAILS_T WHERE TODAY_CREATE_DT >='"+startDt+"' AND TODAY_CREATE_DT <='2022-10-01 23:59:59'";
		return synchronyTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper(InboundDetailsDto.class));
	}
	
	/*public List<InboundDetailsDto> getAgentInteractionNiceDetails(String fromDt,String toDt){
		String sql="SELECT S.SYS_SESS_ID as sessionId, S.USER_NAME as agentName, (CASE WHEN S.SESS_TYPE_ID=13 THEN 'Internal List' WHEN S.SESS_TYPE_ID=17 THEN 'Out Wor' WHEN S.SESS_TYPE_ID=15 THEN 'Callback' WHEN S.SESS_TYPE_ID=4 THEN 'Work' WHEN S.SESS_TYPE_ID=9 THEN 'Self Service' ELSE 'Telephony' END) as workType, S.SESSION_ROUTE_DT as createDt, S.USER_ID as agentId, S.CONTACT_ID as contactId, "
				+"S.SYS_SESS_DESC as outcome,D.SUBDISPOSITION as subDispostion,GETSOURCE(D.CASE_ID) as source,D.CASE_ID as dhanId,GETJTCID(S.CONTACT_ID) as jtcId,GETPATIENTNAME(S.CONTACT_ID) as patientName  FROM SCC_SM_SYS_SESS_STATISTIC_T S, "
				+"SCC_JIVA_ADD_SUBDISPOSITION_T D,SCC_USER_T U WHERE S.SYS_SESS_ID=D.SYS_SESS_ID AND S.USER_ID=U.USER_ID AND CHAT_NAME IN ('agent','campagent','education') AND ENABLED_YN=1 AND S.SESSION_ROUTE_DT > '"+fromDt+" 00:00:00' AND "
				+"S.SESSION_ROUTE_DT < '"+toDt+" 23:59:59' AND S.SYS_SESS_DESC IN ('Book Appointment','Call Transfer To Doctor','Clinic Consultation','FC List','IMRC Appointment booked','IMRC Patient','Live List','Live VC Infertility','Live VC List')";
		System.out.println(sql);
		List<InboundDetailsDto> iDetails =  synchronyTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper(InboundDetailsDto.class));
		List<InboundDetailsDto> rDetails = new ArrayList<InboundDetailsDto>();
		if(iDetails!=null && iDetails.size() >0) {
			iDetails.stream().forEach(i ->{
				List<NiceDetailsDto> nDetails = niceRepo.getNiceDetails(i.getSessionId());
				if(nDetails!=null && nDetails.size() >0) {
					nDetails.stream().forEach(n ->{
						InboundDetailsDto inDetails  = new InboundDetailsDto();
						inDetails.setDialDate(n.getDialDate());
						inDetails.setAnswerDate(n.getAnswerDate());
						inDetails.setDisconnectDate(n.getDisconnectDate());
						inDetails.setDiff(n.getDiff());
						inDetails.setIncomingDid(n.getIncomingDid());
						
						List<ContactExtensionDto> cDetails1=  getDiseaseDetails(i.getContactId());
						if(cDetails1!=null && cDetails1.size() >0) {
							ContactExtensionDto cDetails = cDetails1.get(0);
							inDetails.setDisease(cDetails.getDisease());
							inDetails.setOtherDisease(cDetails.getOtherDisease());
							inDetails.setCampaignName(cDetails.getCompaignName());
							inDetails.setLeadCreateDt(cDetails.getLeadCreateDate());
							inDetails.setPincode(cDetails.getPincode());
							inDetails.setDob(cDetails.getDob());
						}
						String caseCreateDt = getCaseCreateDt(i.getDhanId());
						if(caseCreateDt!=null && caseCreateDt.length() >0) {
							inDetails.setCaseCreateDt(caseCreateDt);
						}
						inDetails.setSessionId(i.getSessionId());
						inDetails.setDhanId(i.getDhanId());
						inDetails.setAgentName(i.getAgentName());
						inDetails.setWorkType(i.getWorkType());
						inDetails.setCreateDt(i.getCreateDt());
						inDetails.setAgentId(i.getAgentId());
						inDetails.setContactId(i.getContactId());
						inDetails.setOutcome(i.getOutcome());
						inDetails.setSubDispostion(i.getSubDispostion());
						inDetails.setSource(i.getSource());
						inDetails.setJtcId(i.getJtcId());
						inDetails.setPatientName(i.getPatientName());
						rDetails.add(inDetails);
					});
				}
			});
		}
		return rDetails;
	}*/
	
	public List<FinalOutcomeDto> getFinalOutcomeStatus(String contactId,String createDt,String outcome){
		String sql="select DATE_FORMAT(SESSION_ROUTE_DT,'%d %b %Y %h:%i %p') as finalOutcomeDt,SYS_SESS_DESC as finalOutcome,GETUSERNAME(USER_ID) as user,SUBDISPOSITION as finalSubOutcome from SCC_SM_SYS_SESS_STATISTIC_T S "+
	            "LEFT OUTER JOIN SCC_JIVA_ADD_SUBDISPOSITION_T SS  ON S.SYS_SESS_ID=SS.SYS_SESS_ID "+
	            "WHERE CONTACT_ID="+contactId + " AND S.CREATE_DT>'"+createDt+"' AND SYS_SESS_DESC IN ("+outcome+") ORDER BY S.SYS_SESS_ID DESC LIMIT 1";
		return synchronyTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper(FinalOutcomeDto.class));
	}
	
	public int getAttempt(String contactId) {
		String sql="SELECT COUNT(SYS_SESS_ID) FROM SCC_SM_SYS_SESS_STATISTIC_T S, SCC_USER_T U WHERE S.USER_ID=U.USER_ID AND S.CONTACT_ID ='"+contactId+"' AND CHAT_NAME IN ('Default Agent','digitalagent') ";
		return synchronyTemplate.queryForObject(sql,new Object[] {},Integer.class);
	}
	
	public List<OutboundDetailsDto> getDigitalDashboardDetails(String fromDt,String toDt){
		String sql="SELECT A.CASE_ID as dhanId,LIST_NAME as listName, SOURCE as source,LIST_CREATE_DT as listCreateDt,(CASE WHEN FIRST_TIME_CALL='0000-00-00 00:00:00' THEN '' ELSE FIRST_TIME_CALL END) as firstTimeCallDt,LEAD_STATUS as leadStatus,IFNULL(CONVERSION,''),CONSULTATION_TYPE as consultationType,CLINIC_GRP as clinicName, "
				+"IFNULL((CASE WHEN PREVIOUS_ORDER_DT='0000:00:00 00:00:00' THEN PREVIOUS_ORDER_DT=NULL ELSE PREVIOUS_ORDER_DT END),'') as previousOrderDt,trim(replace(IFNULL(FB_CITY_NAME,''),'\"','')) as fbCity,IFNULL(COMPAIGN_NAME,'') as campaignName, "
				+"IFNULL(CONSULT_URL,'') as url,IFNULL(REFERENCE_CODE,'') as referenceCode,A.CONTACT_ID as contactId,LEAD_CREATE_DT as leadCreateDt,IFNULL(UTM_SOURCE,'') as utmSource,IFNULL(UTM_MEDIUM,'') as utmMedium,IFNULL(UTM_CAMPAIGN,'') as utmCampaign,IFNULL(PAGE_TYPE,'') as pageType,IFNULL(SOURCE_2,'') as source2,IFNULL(SOURCE_CAT,'') as sourceCat,"
				+"FIRST_ORDER_CODE as order1MedicineId,SECOND_ORDER_CODE as order2MedicineId,SECOND_CONSULT_TYPE as order2ConsultType,GETUSERNAME(A.CREATE_ID) as createdBy,UTM_CAMPAIGNID as campaignId,IFNULL(CONVERSION,'') as conversionType FROM "
				+"SCC_JIVA_DAILY_LEAD_TRACKING_T A,SCC_CONTACT_EXTENTION_T B WHERE  A.CONTACT_ID=B.CONTACT_ID AND A.FIRST_TIME_CALL >='"+fromDt+" 00:00:00' AND A.FIRST_TIME_CALL <='"+toDt+" 23:59:59'"; 
		List<OutboundDetailsDto> oDetails= synchronyTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper(OutboundDetailsDto.class));
		if(oDetails!=null && oDetails.size() >0) {
			oDetails.stream().forEach(eDetails->{
				List<FinalOutcomeDto> fDetails= getFinalOutcomeStatus(eDetails.getContactId(),eDetails.getListCreateDt(),"'Book Appointment','Digital List','Urgent Followup','Query Resolved','CNI - Not Interested','CNR - Not Reachable Discard','Awaiting Response','Escalation Added','Teleconsultation','Pre-payment Appointment','Videoconsultation','CCB - Callback','CNR - Not Reachable Callback','MRC Patient','Payment Follow Up','Post Covid'");
				if(fDetails!=null && fDetails.size()>0) {
					FinalOutcomeDto  fDetails1 = fDetails.get(0);
					eDetails.setFinalOutcomeDt(fDetails1.getFinalOutcomeDt());
					eDetails.setFinalOutcome(fDetails1.getFinalOutcome());
					eDetails.setUser(fDetails1.getUser());
					eDetails.setFinalSubOutcome(fDetails1.getFinalSubOutcome());
				}
				eDetails.setAttempt(getAttempt(eDetails.getContactId())+"");
				double totalCharge = jivaRepo.getTotalCharge(eDetails.getDhanId());
				eDetails.setRevenue(String.valueOf(totalCharge));
				eDetails.setConversion("0");
				if(eDetails.getConversionType()!=null && eDetails.getConversionType().length() >0) {
					eDetails.setConversion("1");
				}
				List<FinalOutcomeDto> pDetails= getFinalOutcomeStatus(eDetails.getContactId(),eDetails.getListCreateDt(),"'Book Appointment','Call Transfer To Doctor','Clinic Consultation','FC List','IMRC Appointment booked','IMRC Patient','Live List','Live VC Infertility','Live VC List'");
				if(pDetails!=null && pDetails.size() >0) {
					FinalOutcomeDto pDetails1 = pDetails.get(0);
					eDetails.setPoName(pDetails1.getFinalOutcome());
					eDetails.setPo("1");
				}
				String caseCreateDt = getCaseCreateDt(eDetails.getDhanId());
				if(caseCreateDt!=null && !caseCreateDt.equals("")) {
					eDetails.setCaseCreateDt(caseCreateDt);
				}
			});
			return oDetails;
		}else {
			return null;
		}
	}

	public void updateDisease(String disease, long oContactId) {
		// TODO Auto-generated method stub
		String sql="SELECT COUNT(CONTACT_ID) FROM SCC_CONTACT_EXTENTION_T WHERE CONTACT_ID="+oContactId;
		int isExist=synchronyTemplate.queryForObject(sql, new Object[] {},Integer.class);
		
		if(isExist>0) {
			sql="UPDATE SCC_CONTACT_EXTENTION_T SET SYSTEM='"+disease+"' WHERE CONTACT_ID="+oContactId;
			synchronyTemplate.update(sql);
		}else {
			sql="INSERT INTO SCC_CONTACT_EXTENTION_T (CONTACT_ID,SYSTEM) VALUES("+oContactId+",'"+disease+"')";
			synchronyTemplate.update(sql);
		}

	}

	
}
