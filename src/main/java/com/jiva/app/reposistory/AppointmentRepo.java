package com.jiva.app.reposistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.jiva.app.dtos.CaseNotesDto;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.mail.domain.Mail;
import com.jiva.app.service.MailService;
import com.jiva.app.service.SynchronyService;
import com.jiva.app.utils.MailUtils;
import com.jiva.app.utils.helper;

@Repository
public class AppointmentRepo {

	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyDbTemplate;

	@Autowired
	@Qualifier("jivaTemplate")
	private JdbcTemplate jivaTemplate;

	@Autowired
	private GlobalRepo globalRepo;
	
	@Autowired
	private SynchronyService service;


	@Autowired
	private MailService mailService;

	public Page<?> getNutritionistAppointment(Map<String, String> reqParam) throws Exception {
		String extraParam = "", extraCountParam = "";
		Pageable pageable = PageRequest.of(Integer.parseInt(reqParam.get("pageIndex")),
				Integer.parseInt(reqParam.get("pageSize")));

		if (reqParam.get("mode").equalsIgnoreCase("today")) {
			// Appointment for today.
			extraParam += " AND DATE(APPOINTMENT_DT) = DATE(NOW()) ORDER BY APPOINTMENT_TIME ASC";
			extraCountParam += " AND DATE(APPOINTMENT_DT) = DATE(NOW())";
		} else if (reqParam.get("mode").equalsIgnoreCase("tomorrow")) {
			// Appointment for tomorrow.
			extraParam += " AND DATE(APPOINTMENT_DT) = DATE_ADD(CURDATE(),INTERVAL 1 DAY)  ORDER BY APPOINTMENT_TIME ASC";
			extraCountParam += " AND DATE(APPOINTMENT_DT) = DATE_ADD(CURDATE(),INTERVAL 1 DAY) ";
		} else if (reqParam.get("mode").equalsIgnoreCase("custom")) {
			// Appointment for custom search.
			String sDt = helper.setDateFormat(reqParam.get("startDt").toString()) + " 00:00:00";
			String eDt = helper.setDateFormat(reqParam.get("endDt").toString()) + " 23:59:59";
			extraParam += " AND D.APPOINTMENT_DT >= '" + sDt + "' and D.APPOINTMENT_DT<='" + eDt
					+ "' ORDER BY APPOINTMENT_DT,APPOINTMENT_TIME ASC LIMIT " + pageable.getPageSize() + " OFFSET "
					+ pageable.getOffset();
			extraCountParam += " AND APPOINTMENT_DT >= '" + sDt + "' and APPOINTMENT_DT<='" + eDt + "'";
		}

		String sql = "SELECT D.ID,D.CASE_ID,D.CONSULT_TYPE,C.GENDER,FIRST_NAME_DISPLAY as PATIENT_NAME,GETUSERNAME(E.NOTIFY_USER_ID) AS CONSULTING_DOC,DATE_FORMAT(D.APPOINTMENT_DT,'%d %b %Y') AS APPOINTMENT_DT, "
				+ "DATE_FORMAT(D.APPOINTMENT_DT,'%h:%i %p')  AS APPOINTMENT_TIME, (CASE WHEN CONSULT_MODE=1 THEN 'VC' WHEN CONSULT_MODE=2 THEN 'TC' ELSE '' END) AS CONSULT_MODE,(SELECT ROOM_ID FROM SCC_JIVA_VC_MEETING_DETAILS_T F WHERE F.CASE_ID=D.CASE_ID AND F.APPOINTMENT_DT=D.APPOINTMENT_DT ORDER BY ID DESC LIMIT 1) AS ROOM_ID, "
				+ "  (SELECT ACTUAL_NO FROM SCC_JIVA_WHATSAPP_CONSENT_T K WHERE K.CASE_ID=D.CASE_ID ORDER BY K.ID DESC LIMIT 1) as whatsappNo,GETUSERNAME(D.CREATE_ID) as CreatedBY,D.APPOINTMENT_DT AS APPT_DT, PROGRAM_ENR "
				+ "FROM SCC_JIVA_IMRC_APPOINTMENT_T D,SCC_CASE_T E,SCC_CASE_CONTACT_T A,SCC_CONTACT_T B ,SCC_PERSON_T C "
				+ " WHERE D.CASE_ID=E.CASE_ID AND E.CASE_ID=A.CASE_ID AND A.CONTACT_ID=B.CONTACT_ID AND B.PERSON_ID=C.PERSON_ID AND A.CASE_ID=D.CASE_ID AND IS_CANCEL=0 AND DOC_ID='"
				+ reqParam.get("docId") + "' " + extraParam + "";
		
		
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> responseDto = synchronyDbTemplate.queryForList(sql);
		if (responseDto.size() > 0) {
			responseDto.forEach(eObj -> {
				Map<String, Object> map = new HashMap<String, Object>();

				String sql1 = "SELECT SYS_SESS_DESC FROM SCC_SM_SYS_SESS_STATISTIC_T A,SCC_CASE_CONTACT_T B  WHERE A.CONTACT_ID=B.CONTACT_ID AND B.CASE_ID= "
						+ eObj.get("CASE_ID")
						+ "	 AND A.CREATE_ID="
						+ reqParam.get("docId") + " AND (CASE WHEN SESSION_END_DT IS NULL THEN SESSION_ROUTE_DT ELSE SESSION_END_DT END) >='" + eObj.get("APPT_DT")
						+ "' ORDER BY SYS_SESS_ID DESC LIMIT 1";
				
				List<Map<String, Object>> obj2 = synchronyDbTemplate.queryForList(sql1);
				
				sql1="SELECT DURATION,CATEGORY FROM SCC_JIVA_PROGRAM_DURATION_MASTER_T WHERE CATEG_CODE='"+eObj.get("CONSULT_TYPE")+"'";
				List<Map<String,Object>> masterObj=synchronyDbTemplate.queryForList(sql1);
				map=helper.mapListToObject(map, masterObj);

				map.putAll(eObj);
				if (obj2.size() > 0) {
					map.putAll(obj2.get(0));
				}
				
				
				String actualNo=(String) map.get("whatsappNo");
			 
				if(!actualNo.equals("") && actualNo.length()==10) {
					map.put("whatsappNo", "91"+actualNo);
				}
				
				response.add(map);

			});

		}
		return new PageImpl<>(response, pageable, getNutritionistAppointmentCount(reqParam, extraCountParam));
	}

	private int getNutritionistAppointmentCount(Map<String, String> reqParam, String extraParam) {

		String sql = "SELECT COUNT(ID) FROM SCC_JIVA_IMRC_APPOINTMENT_T D " + " WHERE IS_CANCEL=0 AND DOC_ID='"
				+ reqParam.get("docId") + "' " + extraParam;

		return synchronyDbTemplate.queryForObject(sql, new Object[] {}, Integer.class);
	}

	public Map<String, Object> getAppointmentInitDetails_repo(Map<String, String> reqParam) {
		Map<String, Object> response = new HashMap<String, Object>();

		String sql = "SELECT D.ID,D.CASE_ID,C.GENDER,FIRST_NAME_DISPLAY as PATIENT_NAME,GETUSERNAME(D.DOC_ID) AS DOCTOR_NAME,DATE_FORMAT(D.APPOINTMENT_DT,'%d %b %Y') AS APPOINTMENT_DT, "
				+ "DATE_FORMAT(D.APPOINTMENT_DT,'%h:%i %p')  AS APPOINTMENT_TIME,D.DOC_ID as DOC_ID , B.CONTACT_ID FROM SCC_JIVA_IMRC_APPOINTMENT_T D,SCC_CASE_CONTACT_T A,SCC_CONTACT_T B ,SCC_PERSON_T C "
				+ " WHERE A.CONTACT_ID=B.CONTACT_ID AND B.PERSON_ID=C.PERSON_ID AND A.CASE_ID=D.CASE_ID AND IS_CANCEL=0 AND D.ID="
				+ reqParam.get("appointmentId");
		List<Map<String, Object>> obj1 = synchronyDbTemplate.queryForList(sql);
		response = helper.mapListToObject(response, obj1);
        
		String pCaseDesc = "1,0,0";
		String pPhone = "";
		String pPhone1 = "";
		String pPhone2 = "";

		sql = "SELECT CASE_DESC FROM SCC_CASE_T WHERE CASE_ID=" + response.get("CASE_ID");
		pCaseDesc = synchronyDbTemplate.queryForObject(sql, new Object[] {}, String.class);
		
		sql = "SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T A,SCC_CASE_CONTACT_T B WHERE A.CONTACT_ID=B.CONTACT_ID AND  B.CASE_ID = "
				+ response.get("CASE_ID") + " AND ADDR_TYPE_ID IN (1001, 1021, 1041) ORDER BY ADDR_TYPE_ID";
		List<Map<String, Object>> list = synchronyDbTemplate.queryForList(sql);
		sql="SELECT (CASE WHEN (CASE_DESC IS NULL OR CASE_DESC='') THEN '1,0,0' ELSE CASE_DESC END) as CASE_DESC,(CASE "
				+ "WHEN CASE_DESC='1,0,0' THEN (SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T C WHERE C.CONTACT_ID=B.CONTACT_ID AND ADDR_TYPE_ID=1001 ORDER BY EADDR_ID DESC LIMIT 1) "
				+ "WHEN CASE_DESC='0,1,0' THEN (SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T C WHERE C.CONTACT_ID=B.CONTACT_ID AND ADDR_TYPE_ID=1021 ORDER BY EADDR_ID DESC LIMIT 1) "
				+ "WHEN CASE_DESC='0,0,1' THEN (SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T C WHERE C.CONTACT_ID=B.CONTACT_ID AND ADDR_TYPE_ID=1041 ORDER BY EADDR_ID DESC LIMIT 1) "
				+ "WHEN CASE_DESC='' OR CASE_DESC IS NULL THEN (SELECT EADDRESS FROM SCC_CONTACT_EADDRESS_T C WHERE C.CONTACT_ID=B.CONTACT_ID AND ADDR_TYPE_ID=1001 ORDER BY EADDR_ID DESC LIMIT 1) "
				+ "ELSE '' END) as defaultPhone FROM SCC_CASE_T A,SCC_CASE_CONTACT_T B  WHERE A.CASE_ID=B.CASE_ID AND A.CASE_ID="+ response.get("CASE_ID");
		List<Map<String, Object>> obj4 = synchronyDbTemplate.queryForList(sql);
		response = helper.mapListToObject(response, obj4);

		for (Map<String, Object> el : list) {
			if (pPhone.equals("")) {
				pPhone = String.valueOf(el.get("EADDRESS"));
			} else if (pPhone1.equals("")) {
				pPhone1 = String.valueOf(el.get("EADDRESS"));
			} else {
				pPhone2 = String.valueOf(el.get("EADDRESS"));
			}
		}
		
		response.put("ptnPhone1", pPhone);
		response.put("ptnPhone2", pPhone1);
		response.put("ptnPhone3", pPhone2);
		String tableName="";
	
	   if(reqParam.get("role")!=null) {
			if(reqParam.get("role").equals("MINDWELLBEINGCOACH")) {
				tableName="SCC_JIVA_MWB_FORM_T";
			}else if(reqParam.get("role").equals("NUTRITIONIST") || reqParam.get("role").equals("MRCDOCTOR")) {
				tableName="SCC_JIVA_NUTRITION_T";
			}else if(reqParam.get("role").equals("YOGATHERAPIST")) {
				tableName="SCC_JIVA_YOGA_FORM_T";
			}
	
		    if(!tableName.equals("")) {
				sql="SELECT COUNT(ID) as allowDisposition FROM "+tableName+" WHERE CASE_ID="+response.get("CASE_ID");
				
					List<Map<String, Object>> obj5 = synchronyDbTemplate.queryForList(sql);
					response = helper.mapListToObject(response, obj5);
		    }
	   }
	   
	   if(reqParam.get("role").equals("MRCDOCTOR")) {
		   sql = "SELECT EXTENTION,G_MEET_LINK FROM SCC_JIVA_IMRC_DOCTOR_LIST_T WHERE DOC_ID=" + response.get("DOC_ID");
	   }else {
		   sql = "SELECT EXTENTION,G_MEET_LINK FROM SCC_JIVA_PROGRAM_DOCTOR_LIST_T WHERE DOC_ID=" + response.get("DOC_ID");
	   }
	
		List<Map<String, Object>> obj3 = synchronyDbTemplate.queryForList(sql);
		response = helper.mapListToObject(response, obj3);
		return response;
	}

	public Map<String, Object> nutritionConsultationForm_edit(Map<String, String> reqParam) {

		Map<String, Object> response = new HashMap<String, Object>();

		String sql = "SELECT CASE_ID, HEIGHT, WEIGHT, GENDER, BMI, BMR, NUTRI_PLAN, LEAST_WGT, MAX_WGT, PREV_WGT, WGT_LOSS, FML_HIST, MENST_HIST, MEDICAL_ISSUE, OIL_USE, FOOD_TAKE, "
				+ "WH_OR_RICE, SUG_INTAKE, MILK_INTAKE, TEA_INTAKE, FRUIT_INTAKE, WATER_INTAKE, ALLERGY, PEAK_HUNGER, EXERCISE_PATT, SEDENTARY_LIFESTYLE, SLEEP_PATT, STRESS_LVL, "
				+ "GUY_HLTH, ENERGY_LVL, CHEST, ABDOMEN, HIPS, ARMS, THIGHS, VEG, NON_VEG, ALCOHOL, TOBACCO, CHOCOLATE, GETUSERNAME(CREATE_ID) AS CREATE_BY, NAME, AGE, WGT_LOSS_REASON, WAKEUP_TIME, SLEEP_TIME, "
				+ "EARLY_MORNING, B_F, MID_MORNING, LUNCH, EVENING, DINNER, EGGET, SWEETS,DATE_FORMAT(CREATE_DT,'%d %b %Y') AS CREATE_DT,TIME(CREATE_DT) AS CREATE_TIME FROM SCC_JIVA_NUTRITION_T WHERE CASE_ID='"
				+ reqParam.get("caseId") + "'";

		List<Map<String, Object>> objlist1 = synchronyDbTemplate.queryForList(sql);

		if (!(objlist1.size() > 0)) {
			sql = "SELECT CASE_ID ,GETPATIENTNAME(A.CONTACT_ID) as NAME,MIDDLE_NAME as 'AGE',SSN as 'WEIGHT',MOM_MAIDEN_NAME as 'HEIGHT',GENDER as 'GENDER' "
					+ "FROM SCC_CASE_CONTACT_T A,SCC_CONTACT_T B ,SCC_PERSON_T C WHERE A.CONTACT_ID=B.CONTACT_ID AND B.PERSON_ID=C.PERSON_ID AND CASE_ID = '"
					+ reqParam.get("caseId") + "'";

			List<Map<String, Object>> objlist2 = synchronyDbTemplate.queryForList(sql);
			response.putAll(objlist2.get(0));

			Long bmi=helper.calculateBMI(String.valueOf(response.get("HEIGHT")), String.valueOf(response.get("WEIGHT")));
			response.put("BMI", bmi.toString());
			
			Long bmr=helper.calculateBMR(String.valueOf(response.get("HEIGHT")), String.valueOf(response.get("WEIGHT")), String.valueOf(response.get("AGE")), String.valueOf(response.get("GENDER")));
			response.put("BMR", bmr.toString());
			
			response.putAll(objlist2.get(0));
		} else {
			response.putAll(objlist1.get(0));
		}

		return response;
	}

	public List<Map<String, Object>> nutritionConsultationPlanHistory(Map<String, String> reqParam) {

		String sql = "SELECT NUTRI_PLAN,DATE_FORMAT(CREATE_DT,'%d %b %Y') AS CREATE_DT FROM SCC_JIVA_NUTRITION_PLAN_HIST_T WHERE CASE_ID='"
				+ reqParam.get("caseId") + "'";

		List<Map<String, Object>> objlist1 = synchronyDbTemplate.queryForList(sql);

		return objlist1;
	}

	public List<Map<String, Object>> nutritionConsultationWeightHistory(Map<String, String> reqParam) {

		String sql = "SELECT WEIGHT,DATE_FORMAT(CREATE_DT,'%d %b %Y') AS CREATE_DT FROM SCC_JIVA_NUTRITION_WGT_T WHERE CASE_ID="
				+ reqParam.get("caseId");

		List<Map<String, Object>> objlist1 = synchronyDbTemplate.queryForList(sql);

		return objlist1;
	}

	public String nutritionConsultation_save(Map<String, String> reqParam) {
		String caseId = "", sql = "", insertNames = "", insertValues = "", updateParams = "", comma = "";

		for (String key : reqParam.keySet()) {
			insertNames += comma + key;
			insertValues += comma + "'" + reqParam.get(key) + "'";
			updateParams += comma + key + " = '" + reqParam.get(key) + "'";
			comma = ",";
		}
		
		insertNames+=",CREATE_DT";
		insertValues+=",NOW()";


		sql = "SELECT COUNT(*) FROM SCC_JIVA_NUTRITION_T WHERE CASE_ID = '" + reqParam.get("CASE_ID") + "'";
		int count = synchronyDbTemplate.queryForObject(sql, new Object[] {}, Integer.class);

		if (count > 0) {
			sql = "UPDATE SCC_JIVA_NUTRITION_T SET " + updateParams + " WHERE CASE_ID=" + reqParam.get("CASE_ID");
		} else {
			sql = "INSERT INTO SCC_JIVA_NUTRITION_T (" + insertNames + ") VALUES(" + insertValues + ")";
		}
      
		synchronyDbTemplate.update(sql, new Object[] {});

		// Update Weight History
		if (!reqParam.get("WEIGHT").trim().equals("")) {
			sql = "INSERT INTO SCC_JIVA_NUTRITION_WGT_T (CASE_ID,WEIGHT,CREATE_ID,CREATE_DT) VALUES('"
					+ reqParam.get("CASE_ID") + "','" + reqParam.get("WEIGHT") + "','" + reqParam.get("CREATE_ID")
					+ "',NOW())";
			synchronyDbTemplate.update(sql, new Object[] {});
		}

		// Update Weight History
		if (!reqParam.get("NUTRI_PLAN").trim().equals("")) {
			sql = "INSERT INTO SCC_JIVA_NUTRITION_PLAN_HIST_T (CASE_ID,NUTRI_PLAN,CREATE_ID,CREATE_DT) VALUES('"
					+ reqParam.get("CASE_ID") + "','" + reqParam.get("NUTRI_PLAN") + "','" + reqParam.get("CREATE_ID")
					+ "',NOW())";
			synchronyDbTemplate.update(sql, new Object[] {});
		}

		return "Sucessfully Updated";

	}

	public Map<String, Object> entrollment_details(Map<String, String> reqParam) {
		// TODO Auto-generated method stub
		Map<String, Object> responseDto = new HashMap<String, Object>();
		String sql = "";

		sql = "SELECT GETUSERNAME(" + reqParam.get("userId")
				+ ") as DOC_NAME,GETPATIENTNAME(CONTACT_ID) AS PATIENT_NAME FROM SCC_CASE_CONTACT_T A,SCC_CASE_T B WHERE A.CASE_ID=B.CASE_ID AND B.CASE_ID="
				+ reqParam.get("caseId");
		List<Map<String, Object>> objlist3 = synchronyDbTemplate.queryForList(sql);
		responseDto = helper.mapListToObject(responseDto, objlist3);

		sql = "SELECT CONTACT_EXT_KEY FROM SCC_USER_T A, SCC_CONTACT_T B, SCC_PERSON_T C WHERE A.USER_ID="
				+ reqParam.get("userId") + " AND "
				+ "A.CONTACT_ID = B.CONTACT_ID AND B.PERSON_ID = C.PERSON_ID AND CONTACT_EXT_KEY IS NOT NULL AND CONTACT_EXT_KEY != ''";

		List<Map<String, Object>> objlist4 = synchronyDbTemplate.queryForList(sql);
		responseDto = helper.mapListToObject(responseDto, objlist4);

		if (responseDto.get("CONTACT_EXT_KEY") != null) {
			sql = "select B.desc from web_user A,user_group B where A.groupid=B.id and A.id="
					+ responseDto.get("CONTACT_EXT_KEY");
			String clinicName = jivaTemplate.queryForObject(sql, new Object[] {}, String.class);
			responseDto.put("CLINIC_NAME", clinicName);
		}

		sql = "SELECT CASE_ID, DIAGNOSIS, VIKRITI, COMORBIDITY, SPECIFIC_CMNT, SPECIFIC_ADVICE, GETUSERNAME(CREATE_ID) AS CREATE_ID, CREATE_DT FROM SCC_JIVA_PROGRAM_MGNT_POST_ENROLLMENT_T WHERE CASE_ID="
				+ reqParam.get("caseId") + " AND MODULE_TYPE='" + reqParam.get("MODULE_TYPE") + "'";
		List<Map<String, Object>> objlist1 = synchronyDbTemplate.queryForList(sql);

		if (objlist1.size() > 0) {
			responseDto.putAll(objlist1.get(0));
		} else {

			sql = "SELECT DIAGNOSIS,COMORBIDITY,VIKRITI,SPECIFIC_CMNT,SPECIFIC_ADVICE FROM SCC_JIVA_PROGRAM_MGNT_POST_ENROLLMENT_T WHERE CASE_ID="
					+ reqParam.get("caseId") + " AND MODULE_TYPE='" + reqParam.get("MODULE_TYPE")
					+ "' ORDER BY ID DESC LIMIT 1";
			List<Map<String, Object>> objlist2 = synchronyDbTemplate.queryForList(sql);
			if (objlist2.size() > 0) {
				responseDto.putAll(objlist2.get(0));
			}

		}
		return responseDto;
	}

	public List<Map<String, Object>> notes_view(Map<String, String> reqParam) {

		String extraParam = "";

		if (!reqParam.get("chatName").equalsIgnoreCase("all")) {
			if (reqParam.get("chatName").equalsIgnoreCase("doc")) {
				extraParam += " AND B.CHAT_NAME LIKE '%doc%'";
			} else {
				extraParam += " AND B.CHAT_NAME IN ('" + reqParam.get("chatName") + "')";
			}
		}

		String sql = "SELECT FIRST_NAME, LAST_NAME, DATE_FORMAT(A.CREATE_DT, '%d-%b-%y') as CREATE_DT, NOTE FROM SCC_CASE_NOTE_T A, "
				+ "SCC_USER_T B, SCC_CONTACT_T C, SCC_PERSON_T D WHERE A.CREATE_ID = B.USER_ID "
				+ "AND B.CONTACT_ID = C.CONTACT_ID AND C.PERSON_ID = D.PERSON_ID AND CASE_ID =" + reqParam.get("caseId")
				+ " " + extraParam + " ORDER BY A.CREATE_DT DESC";
		List<Map<String, Object>> objlist1 = synchronyDbTemplate.queryForList(sql);

		return objlist1;
	}

	public Map<String, List<Map<String, Object>>> consultationform_view(Map<String, String> reqParam) {
		Map<String, List<Map<String, Object>>> responseDto = new HashMap<String, List<Map<String, Object>>>();

		String sql = "SELECT B.CC_WHAT_NAME as System,C.CC_WHERE_NAME AS Disease,A.CC_HOW_LONG_NUM as dur1,D.INTERVAL_NAME AS dur2,E.CC_TYPE_NAME as Status "
				+ "FROM SCC_JIVA_CONSULT_FORM_CC_T A,SCC_JIVA_CC_WHAT_T B, SCC_JIVA_CC_WHERE_T C, SCC_JIVA_TIME_INTERVAL_T D, SCC_JIVA_CC_TYPE_T E,SCC_JIVA_CONSULT_FORM_T F "
				+ " WHERE D.INTERVAL_ID=A.CC_HOW_LONG_INTERVAL AND A.CC_WHAT_ID = B.CC_WHAT_ID AND F.FORM_ID=A.FORM_ID AND C.CC_WHERE_ID=A.CC_WHERE_ID "
				+ " AND E.CC_TYPE_ID=A.CC_TYPE AND CASE_ID=" + reqParam.get("caseId");
		List<Map<String, Object>> objlist1 = synchronyDbTemplate.queryForList(sql);
		responseDto.put("chiefComplaint", objlist1);

		sql = "SELECT B.PH_WHAT_NAME AS System,C.PH_WHERE_NAME as Disease,A.PH_HOW_LONG_NUM as dur1,D.INTERVAL_NAME AS dur2 "
				+ "FROM SCC_JIVA_CONSULT_FORM_PH_T A, SCC_JIVA_PH_WHAT_T B, SCC_JIVA_PH_WHERE_T C,SCC_JIVA_TIME_INTERVAL_T D,SCC_JIVA_CONSULT_FORM_T F "
				+ "WHERE A.PH_WHAT_ID = B.PH_WHAT_ID AND A.PH_WHERE_ID = C.PH_WHERE_ID AND A.PH_HOW_LONG_INTERVAL = D.INTERVAL_ID AND F.FORM_ID=A.FORM_ID AND CASE_ID="
				+ reqParam.get("caseId");

		List<Map<String, Object>> objlist2 = synchronyDbTemplate.queryForList(sql);
		responseDto.put("pastHistory", objlist2);
		sql = "SELECT B.DISEASE_WHAT_NAME as  System, C.DISEASE_WHERE_NAME as Disease,A.DIS_HOW_LONG_NUM as dur1,D.INTERVAL_NAME AS dur2, "
				+ "(CASE WHEN DIS_IMPROVEMENT_1=1 THEN '---' WHEN DIS_IMPROVEMENT_1=2 THEN 'Condition Aggravated' "
				+ "WHEN DIS_IMPROVEMENT_1=3 THEN 'Condidition Relieved' WHEN DIS_IMPROVEMENT_1=4 THEN 'Litte Improvement' WHEN "
				+ "DIS_IMPROVEMENT_1=5 THEN 'No Relief' WHEN DIS_IMPROVEMENT_1=6 THEN 'Significant Improvement' WHEN DIS_IMPROVEMENT_1=7 THEN 'Moderate Relief' ELSE '' END) as Status "
				+ "FROM SCC_JIVA_CONSULT_FORM_T M,SCC_JIVA_CONSULT_FOLLOWUP_T F,SCC_JIVA_CONSULT_FORM_DIS_T A, SCC_JIVA_DISEASE_WHAT_T B, SCC_JIVA_DISEASE_WHERE_T C, "
				+ "SCC_JIVA_TIME_INTERVAL_T D "
				+ "WHERE M.FORM_ID=F.FORM_ID AND M.FORM_ID=A.FORM_ID AND  A.DIS_WHAT_ID = B.DISEASE_WHAT_ID AND A.DIS_WHERE_ID = C.DISEASE_WHERE_ID "
				+ "AND A.DIS_HOW_LONG_INTERVAL = D.INTERVAL_ID AND M.CASE_ID = " + reqParam.get("caseId")
				+ " AND B.DISEASE_WHAT_NAME!='Product Order' GROUP BY M.FORM_ID ORDER BY FORM_DIS_ID ";
		List<Map<String, Object>> objlist3 = synchronyDbTemplate.queryForList(sql);
		responseDto.put("diagnosis", objlist3);

		sql = "SELECT LT_DATA FROM SCC_JIVA_CONSULT_FORM_T WHERE CASE_ID=" + reqParam.get("caseId")
				+ " ORDER BY FORM_ID DESC LIMIT 1";
		List<Map<String, Object>> objlist4 = synchronyDbTemplate.queryForList(sql);
		responseDto.put("lineOfTreatment", objlist4);
		return responseDto;
	}

	public Map<String, Object> blockAppointmentInitDetails(Map<String, String> reqParam) {

		Map<String, Object> responseDto = new HashMap<String, Object>();

		String sql = "SELECT SHIFT_TIMING,WEEK_OFF,SHIFT_MONTH as DATE FROM SCC_JIVA_PROGRAM_DOCTOR_SHIFT_LIST_T WHERE "
				+ " DOC_ID=" + reqParam.get("userId") +" AND SHIFT_MONTH='"+reqParam.get("month")+"' ORDER BY CREATE_DT DESC LIMIT 1";
		List<Map<String, Object>> objlist2 = synchronyDbTemplate.queryForList(sql);
		responseDto = helper.mapListToObject(responseDto, objlist2);

		return responseDto;
	}

	public Page<?> blockAppointmentgetAll(Map<String, String> reqParam) throws Exception {
		String extraParam = "";
		Pageable pageable = PageRequest.of(Integer.parseInt(reqParam.get("pageIndex")),
				Integer.parseInt(reqParam.get("pageSize")));

		String sDt = helper.setDateFormat(reqParam.get("startDt").toString());
		String eDt = helper.setDateFormat(reqParam.get("endDt").toString());

		extraParam += " DOC_ID=" + reqParam.get("docId") + " AND SHIFT_MONTH='"+reqParam.get("month")+"' ";

//		String sql = "SELECT B.ID,B.DOC_ID, MEETING_START_TIME, MEETING_START_TIME_AM_PM, MEETING_END_TIME, MEETING_END_TIME_AM_PM,DATE_FORMAT( MEETING_DT_FROM,'%d-%b-%Y') as MEETING_DT_FROM,DATE_FORMAT( MEETING_DT_TO,'%d-%b-%Y') as MEETING_DT_TO FROM SCC_JIVA_PROGRAM_DOCTOR_LIST_T A,SCC_JIVA_PROGRAM_DOCTOR_MEETING_LIST_T B WHERE A.DOC_ID=B.DOC_ID AND ISACTIVE = 1"
//				+ " AND MEETING_DT_FROM >= '" + sDt + "' " + extraParam + "ORDER BY MEETING_DT_FROM ASC LIMIT "
//				+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
//		
      
		String sql="SELECT ID,DOC_ID, MEETING_START_TIME, MEETING_START_TIME_AM_PM, MEETING_END_TIME, MEETING_END_TIME_AM_PM,DATE_FORMAT( MEETING_DT_FROM,'%d-%b-%Y') as MEETING_DT_FROM,DATE_FORMAT( MEETING_DT_TO,'%d-%b-%Y') as MEETING_DT_TO FROM SCC_JIVA_PROGRAM_DOCTOR_MEETING_LIST_T "
				+ " WHERE (MEETING_DT_FROM >= '" + sDt + "' OR MEETING_DT_TO >= '" + sDt + "' ) AND " + extraParam + "ORDER BY MEETING_DT_FROM ASC LIMIT "
                + pageable.getPageSize() + " OFFSET " + pageable.getOffset();
		
		List<Map<String, Object>> responseDto = synchronyDbTemplate.queryForList(sql);

		return new PageImpl<>(responseDto, pageable, getBlockAppointmentgetAllCount(reqParam, extraParam));
	}

	private int getBlockAppointmentgetAllCount(Map<String, String> reqParam, String extraParam) {

		String sql = "SELECT COUNT(ID) FROM SCC_JIVA_PROGRAM_DOCTOR_MEETING_LIST_T WHERE "
				+ extraParam;

		return synchronyDbTemplate.queryForObject(sql, new Object[] {}, Integer.class);
	}

	/*
	 * Repo to delete Meeting Timing.
	 */
	public String blockAppointmentDelete_repo(Map<String, String> reqParam) {
		String sql = "DELETE FROM SCC_JIVA_PROGRAM_DOCTOR_MEETING_LIST_T WHERE ID=" + reqParam.get("ID");

		synchronyDbTemplate.update(sql, new Object[] {});
		return "Sucessfully Delete";
	}

	public ResponseMessage blockAppointmentAll_save(Map<String, String> reqParam) throws Exception {

		Map<String, Object> responseDto = new HashMap<String, Object>();
		Map<String,Object> mailObj=new HashMap();
		Map<String,Object> shiftMapObj=new HashMap();
		ResponseMessage responseHandler = new ResponseMessage();
		
		String sql = "",currentMeetingTime="";
		String inSlot="0";
       
		String sDt = helper.setDateFormat(reqParam.get("startDt").toString());
		String eDt = helper.setDateFormat(reqParam.get("endDt").toString());
		String startTime = reqParam.get("startHr") + ":" + reqParam.get("startMin");
		String endTime = reqParam.get("endHr") + ":" + reqParam.get("endMin");
		
		mailObj.put("meetingTime", sDt+" to "+eDt+" / "+startTime+" to " +endTime);
		
		sql="SELECT STR_TO_DATE(SUBSTRING_INDEX(SHIFT_TIMING,' ',2),'%l:%i %p') AS shift_st,STR_TO_DATE(SUBSTRING_INDEX(SHIFT_TIMING,' ',-2),'%l:%i %p') AS shift_end FROM "
				+ " SCC_JIVA_PROGRAM_DOCTOR_SHIFT_LIST_T WHERE DOC_ID="+reqParam.get("DOC_ID")+" AND SHIFT_MONTH=DATE_FORMAT('"+sDt+"','%b-%Y') ORDER BY CREATE_DT DESC LIMIT 1";
		List<Map<String, Object>> shiftObj = synchronyDbTemplate.queryForList(sql);
		shiftMapObj=helper.mapListToObject(shiftMapObj, shiftObj);
		
		
		// Check Meeting within ShiftTiming.
		sql="SELECT count(ID) FROM SCC_JIVA_PROGRAM_DOCTOR_SHIFT_LIST_T WHERE DOC_ID="+reqParam.get("DOC_ID")		
				+ " AND '"+shiftMapObj.get("shift_st")+"' < SUBSTRING_INDEX(CONCAT('1990-01-01 ',STR_TO_DATE('"+startTime+" "+reqParam.get("startMode")+"','%l:%i %p')) + INTERVAL 1 MINUTE,' ',-1) AND "
				+ " '"+shiftMapObj.get("shift_end")+"' > SUBSTRING_INDEX(CONCAT('1990-01-01 ',STR_TO_DATE('"+endTime+" "+reqParam.get("endMode")+"','%l:%i %p')) - INTERVAL 1 MINUTE,' ',-1) AND SHIFT_MONTH=DATE_FORMAT('"+sDt+"','%b-%Y') ORDER BY DOC_ID,CREATE_DT DESC LIMIT 1 ";
		int inShift=synchronyDbTemplate.queryForObject(sql, new Object[] {}, Integer.class);
		
	    if(inShift==0) {
	    	responseHandler.setStatusCode(403);
	    	responseHandler.setMessage("Meeting should be in Shift Timing.");    	
	    	return responseHandler;
	    }

		// Fetch All Category with timeslot
	    sql="SELECT APPOINTMENT_DT,CONCAT(A.DOC_ID,' ') as docId,GETUSERNAME(DOC_ID) AS DOC_NAME,B.DURATION,B.CATEGORY FROM SCC_JIVA_IMRC_APPOINTMENT_T A, "
	    		+ "SCC_JIVA_PROGRAM_DURATION_MASTER_T B WHERE A.CONSULT_TYPE=B.CATEG_CODE AND "
	    		+ "DOC_ID='"+reqParam.get("DOC_ID")+"' AND DATE(APPOINTMENT_DT) >= '"+sDt+"' AND DATE(APPOINTMENT_DT) <= '"+eDt+"' "
	    		+ " AND TIME(APPOINTMENT_DT) BETWEEN TIME(CONCAT('"+sDt+" ',STR_TO_DATE('"+startTime+" "+reqParam.get("startMode")+"','%l:%i %p')) - INTERVAL 1 HOUR) AND "
	    		+ " TIME(CONCAT('"+eDt+" ',STR_TO_DATE('"+endTime+" "+reqParam.get("endMode")+"','%l:%i %p')) + INTERVAL 1 HOUR ) AND A.IS_CANCEL=0";
		List<Map<String, Object>> obj1 = synchronyDbTemplate.queryForList(sql);
		
		
		sql = "SELECT * FROM SCC_JIVA_PROGRAM_DOCTOR_LIST_T WHERE DOC_ID=" + reqParam.get("DOC_ID");
		List<Map<String, Object>> obj2 = synchronyDbTemplate.queryForList(sql);
		responseDto = helper.mapListToObject(responseDto, obj2);
		
		
		if(obj1.size()>0) {
			
			mailObj.put("appointments",obj1);
			mailObj.put("DOC_ID", reqParam.get("DOC_ID"));
			mailObj.put("DOC_NAME", responseDto.get("DOC_NAME"));
			// Send Mail if meeting within appointment time.
			Mail mail = MailUtils.alertMail(mailObj);
			String result = mailService.sendEmail(mail);
		}
	
		
		if (responseDto.get("ID") != null) {
            
			sql = "INSERT INTO SCC_JIVA_PROGRAM_DOCTOR_MEETING_LIST_T (DOC_ID,DOC_NAME,MEETING_DT_FROM,MEETING_START_TIME,MEETING_START_TIME_AM_PM,MEETING_DT_TO,MEETING_END_TIME,MEETING_END_TIME_AM_PM,CREATE_DT,SHIFT_MONTH) "
					+ "VALUES(" + responseDto.get("DOC_ID") + ",'" + responseDto.get("DOC_NAME") + "','" + sDt + "','"
					+ startTime + "','" + reqParam.get("startMode") + "','" + eDt + "','" + endTime + "','"
					+ reqParam.get("endMode") + "',NOW(),DATE_FORMAT('"+sDt+"','%b-%Y'))";
			 synchronyDbTemplate.update(sql, new Object[] {});
			
			 String meetingTimestamp=sDt+" - "+eDt+" | "+startTime+" "+reqParam.get("startMode")+" to "+endTime+" "+reqParam.get("endMode");
			 
			 sql = "INSERT INTO SCC_JIVA_PROGRAM_DOCTOR_LOG_T (DOC_ID,MEETING_TIMESTAMP, STATUS, CREATE_BY, CREATE_DT)"
				      +" VALUES ("+responseDto.get("DOC_ID")+",'"+meetingTimestamp+"','INSERT',"+responseDto.get("DOC_ID")+",NOW())";
			 synchronyDbTemplate.update(sql, new Object[] {});	    
			 
			 responseHandler.setStatusCode(200);
		     responseHandler.setMessage("Blocked Successfully.");   
		    	
			 return responseHandler;
		} else {
			
			responseHandler.setStatusCode(404);
	    	responseHandler.setMessage("No Data Found");   			
			return responseHandler;
		}

	}

	public String updateShift(Map<String, String> reqParam) throws Exception {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String shiftTiming = reqParam.get("startHr") + ":" + reqParam.get("startMin") + " " + reqParam.get("startMode")
				+ " to " + reqParam.get("endHr") + ":" + reqParam.get("endMin") + " " + reqParam.get("endMode");
		
		String shiftMonth=reqParam.get("month") + "-" + year;
	
		String sql = "INSERT INTO SCC_JIVA_PROGRAM_DOCTOR_SHIFT_LIST_T ( DOC_ID, SHIFT_TIMING, SHIFT_MONTH, WEEK_OFF, CREATE_BY,CREATE_DT) "
				+ "VALUES('"+reqParam.get("docId")+"','"+shiftTiming+"','"+shiftMonth+"','"+reqParam.get("weekOff")+"','"+reqParam.get("docId")+"',NOW())";
		synchronyDbTemplate.update(sql, new Object[] {});
		
		return "Sucessfully Updated";
	}

	public String saveForm_yoga_repo(Map<String, String> reqParam) throws Exception {

		String sql = "", insertNames = "", insertValues = "", updateParams = "", comma = "", value = "";

		for (String key : reqParam.keySet()) {
			value = reqParam.get(key);

			if (key.equals("DATE")) {
				value = helper.setDateFormat(value);
			}

			insertNames += comma + key;
			insertValues += comma + "'" + value + "'";
			updateParams += comma + key + " = '" + value + "'";
			comma = ",";
		}
		// Insert Default Data.
		insertNames += ", CREATE_DT";
		insertValues += ",NOW()";

		sql = "SELECT COUNT(*) FROM SCC_JIVA_YOGA_FORM_T WHERE CASE_ID = '" + reqParam.get("CASE_ID") + "'";
		int count = synchronyDbTemplate.queryForObject(sql, new Object[] {}, Integer.class);

		if (count > 0) {
			sql = "UPDATE SCC_JIVA_YOGA_FORM_T SET " + updateParams + " WHERE CASE_ID=" + reqParam.get("CASE_ID");
		} else {
			sql = "INSERT INTO SCC_JIVA_YOGA_FORM_T (" + insertNames + ") VALUES(" + insertValues + ")";
		}

		synchronyDbTemplate.update(sql, new Object[] {});

	    sql="INSERT INTO SCC_JIVA_YOGA_STRESS_LVL_HISTORY_T ( CASE_ID, STRESS_LVL, CREATE_ID, CREATE_DT )"
	    	+ " VALUES("+reqParam.get("CASE_ID")+", "+reqParam.get("STRESS_LVL")+","+reqParam.get("CREATE_ID")+",NOW() ) ";
	    
	    synchronyDbTemplate.update(sql, new Object[] {});
	    
		return "Successfully Update";

	}
    
	public Map<String, Object> editForm_yoga_repo(Map<String, String> reqParam) {
		// TODO Auto-generated method stub

		Map<String, Object> responseDto = new HashMap<String, Object>();

		String sql = "SELECT PATIENT_NAME, AGE, WEIGHT, CHIEF_COMPLAINT, STRESS_LVL, YOGA_GOAL, CHAKRA_CHART, DIAGNOSIS, CREATE_ID, CREATE_DT, CASE_ID, DATE_FORMAT(DATE,'%d %b %Y') AS DATE FROM SCC_JIVA_YOGA_FORM_T WHERE CASE_ID="
				+ reqParam.get("CASE_ID");
		List<Map<String, Object>> obj1 = synchronyDbTemplate.queryForList(sql);
		responseDto = helper.mapListToObject(responseDto, obj1);

		if (obj1.size() == 0) {
			Map<String, Object> obj2 = globalRepo.getPatientInfo(reqParam.get("CASE_ID"));
			responseDto.putAll(obj2);
		}

		sql = "SELECT ACTUAL_NO FROM SCC_JIVA_WHATSAPP_CONSENT_T WHERE CASE_ID=" + responseDto.get("CASE_ID")
				+ " ORDER BY ID DESC LIMIT 1";
		List<Map<String, Object>> obj3 = synchronyDbTemplate.queryForList(sql);
		responseDto = helper.mapListToObject(responseDto, obj3);
	
		return responseDto;

	}
	
	public List<Map<String, Object>> stressLvlHist_yoga_repo(Map<String, String> reqParam) {
		// TODO Auto-generated method stub

		Map<String, Object> responseDto = new HashMap<String, Object>();

		String sql = "SELECT CASE_ID, STRESS_LVL, GETUSERNAME(CREATE_ID) AS CREATE_ID, DATE_FORMAT(CREATE_DT,'%d %b %Y') AS CREATE_DT FROM  "
				+ " SCC_JIVA_YOGA_STRESS_LVL_HISTORY_T WHERE CASE_ID="+ reqParam.get("caseId");
		
		List<Map<String, Object>> obj1 = synchronyDbTemplate.queryForList(sql);

		return obj1;

	}
    
	public Map<String, Object> getProgramOrderDetails_repo(Map<String, String> reqParam) {
        
		Map<String, Object> responseDto = new HashMap<String, Object>();
		String productIds="";
		
		System.out.println(reqParam.get("mode"));
		if(reqParam.get("mode").equalsIgnoreCase("yogatherapist")) {
			productIds="1509,1510,1511";
		}else if(reqParam.get("mode").equalsIgnoreCase("nutritionist") || reqParam.get("mode").equalsIgnoreCase("mrcdoctor")) {
			productIds="1306,1307,1308";
		}
	  
	  if(!productIds.equals("")) {
		String sql = "select product_id from jiva.order O,customer C,order_product P where O.customer_id=C.id and O.id=P.order_id and product_id in ("+productIds+") and dhanvantariid='"+reqParam.get("caseId")+"' order by O.id desc limit 1";

		List<Map<String, Object>> obj1= jivaTemplate.queryForList(sql);
		
		obj1.forEach(el->{
			
			if(String.valueOf(el.get("product_id")).equals("1509") || String.valueOf(el.get("product_id")).equals("1306")) {
				responseDto.put("code", 1);
			}else if(String.valueOf(el.get("product_id")).equals("1510") || String.valueOf(el.get("product_id")).equals("1307")) {
				responseDto.put("code", 2);
			}else if(String.valueOf(el.get("product_id")).equals("1511") || String.valueOf(el.get("product_id")).equals("1308")) {
				responseDto.put("code", 3);
			}
			
		});
	  }

		return responseDto;
	}
	
	public String updateDisposition(Map<String, String> reqParam) {
		
		String sql="";
		
		if(!reqParam.get("disposition").equals("") && !reqParam.get("subdisposition").equals("")){
			sql="SELECT DISPOSTION,SUB_DISPOSITION FROM SCC_JIVA_ADD_FORM_T A,SCC_JIVA_ADD_FORM_DISPOSTION_T B, SCC_JIVA_ADD_FORM_SUB_DISPOSTION_T C WHERE A.FORM_ID=B.FORM_ID "+
				"AND B.DIS_ID=C.DIS_ID AND B.DIS_ID="+reqParam.get("disposition")+" AND SUB_DIS_ID="+reqParam.get("subdisposition")+" AND UCASE(FORM_NAME)=UCASE('"+reqParam.get("formName")+"')";
		}else{
			sql="SELECT DISPOSTION,'(select one)' FROM SCC_JIVA_ADD_FORM_T A,SCC_JIVA_ADD_FORM_DISPOSTION_T B WHERE "+
				"A.FORM_ID=B.FORM_ID AND B.DIS_ID="+reqParam.get("disposition")+" AND UCASE(FORM_NAME)=UCASE('"+reqParam.get("formName")+"')";
		}
		
		List<Map<String, Object>> obj1 = synchronyDbTemplate.queryForList(sql);
		Map<String,Object> mapParam=helper.mapListToObject(new HashMap<String,Object>(), obj1);
		
		if(!mapParam.get("DISPOSTION").equals("") ) {
			 sql = "UPDATE SCC_JIVA_IMRC_APPOINTMENT_T SET DISPOSITION=?, DISPOSITION_TIME=NOW() WHERE ID=" + reqParam.get("appointmentId");
			 synchronyDbTemplate.update(sql, new Object[] {mapParam.get("DISPOSTION")});
			 
			 String notes=""+mapParam.get("DISPOSTION")+" Disposition added for appointmentId : "+reqParam.get("appointmentId")+" ( "+reqParam.get("formName")+" ) ";
		     CaseNotesDto notesDto=new CaseNotesDto(reqParam.get("caseId"),notes,reqParam.get("userId"));
			 service.addCaseNotes(notesDto);		
		}
		 
		return "Successfully Update";
	}
	

	public String updateStartSession(Map<String, String> reqParam) {
		 String sql="";
		
		 sql = "UPDATE SCC_JIVA_IMRC_APPOINTMENT_T SET START_TIME=NOW() WHERE ID=" + reqParam.get("appointmentId");
		 synchronyDbTemplate.update(sql, new Object[] {});
		 
		 String notes="Appointment session start for appointmentId : "+reqParam.get("appointmentId");
	     CaseNotesDto notesDto=new CaseNotesDto(reqParam.get("caseId"),notes,reqParam.get("userId"));
		 service.addCaseNotes(notesDto);	
		 
     	 return "Successfully Update";
	}
		

	public String saveForm_MWB_repo(Map<String, String> reqParam) throws Exception {

		String sql = "", insertNames = "", insertValues = "", updateParams = "", comma = "", value = "";

		for (String key : reqParam.keySet()) {
			value = reqParam.get(key);

			if (key.equals("DATE")) {
				value = helper.setDateFormat(value);
			}

			insertNames += comma + key;
			insertValues += comma + "'" + value + "'";
			updateParams += comma + key + " = '" + value + "'";
			comma = ",";
		}
		// Insert Default Data.
		insertNames += ", CREATE_DT";
		insertValues += ",NOW()";

		sql = "SELECT COUNT(*) FROM SCC_JIVA_MWB_FORM_T WHERE CASE_ID = '" + reqParam.get("CASE_ID") + "'";
		int count = synchronyDbTemplate.queryForObject(sql, new Object[] {}, Integer.class);

		if (count > 0) {
			sql = "UPDATE SCC_JIVA_MWB_FORM_T SET " + updateParams + " WHERE CASE_ID=" + reqParam.get("CASE_ID");
		} else {
			sql = "INSERT INTO SCC_JIVA_MWB_FORM_T (" + insertNames + ") VALUES(" + insertValues + ")";
		}

		synchronyDbTemplate.update(sql, new Object[] {});

	    sql="INSERT INTO SCC_JIVA_MWB_STRESS_LVL_HISTORY_T ( CASE_ID, STRESS_LVL, CREATE_ID, CREATE_DT )"
	    	+ " VALUES("+reqParam.get("CASE_ID")+", "+reqParam.get("STRESS_LVL")+","+reqParam.get("CREATE_ID")+",NOW() ) ";
	    
	    synchronyDbTemplate.update(sql, new Object[] {});
	    
		return "Successfully Update";

	}
    
	public Map<String, Object> editForm_MWB_repo(Map<String, String> reqParam) {
		// TODO Auto-generated method stub

		Map<String, Object> responseDto = new HashMap<String, Object>();

		String sql = "SELECT PATIENT_NAME, CASE_ID, CHIEF_COMPLAINT, MARRIED_STATUS, STRESS_LVL, MENTAL_STATUS, ROOT_CAUSE, AGE, NOTES, DATE_FORMAT(DATE,'%d %b %Y') AS DATE FROM SCC_JIVA_MWB_FORM_T WHERE CASE_ID="
				+ reqParam.get("CASE_ID");
		List<Map<String, Object>> obj1 = synchronyDbTemplate.queryForList(sql);
		responseDto = helper.mapListToObject(responseDto, obj1);
		
		if (obj1.size() == 0) {
			Map<String, Object> obj2 = globalRepo.getPatientInfo(reqParam.get("CASE_ID"));
			responseDto.putAll(obj2);
		}
	
		return responseDto;

	}
	
	public List<Map<String, Object>> stressLvlHist_MWB_repo(Map<String, String> reqParam) {
		// TODO Auto-generated method stub

		Map<String, Object> responseDto = new HashMap<String, Object>();

		String sql = "SELECT CASE_ID, STRESS_LVL, GETUSERNAME(CREATE_ID) AS CREATE_ID, DATE_FORMAT(CREATE_DT,'%d %b %Y') AS CREATE_DT FROM  "
				+ " SCC_JIVA_MWB_STRESS_LVL_HISTORY_T WHERE CASE_ID="+ reqParam.get("caseId");
		
		List<Map<String, Object>> obj1 = synchronyDbTemplate.queryForList(sql);

		return obj1;

	}


         
}
