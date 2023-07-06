package com.jiva.app.serviceImp;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiva.app.dtos.AddressDetailsDto;
import com.jiva.app.dtos.AdroidFirstConnectDto;
import com.jiva.app.dtos.AdroidResponseDto;
import com.jiva.app.dtos.AppointmetStatusData;
import com.jiva.app.dtos.CaseNotesDto;
import com.jiva.app.dtos.ClinicAppointmentDto;
import com.jiva.app.dtos.ClinicApptRequestDto;
import com.jiva.app.dtos.ClinicFreshCallDto;
import com.jiva.app.dtos.CreateContactDto;
import com.jiva.app.dtos.DispositionResponseDto;
import com.jiva.app.dtos.ExtentionDetailsDto;
import com.jiva.app.dtos.InboundDetailsDto;
import com.jiva.app.dtos.IperformanceObDto;
import com.jiva.app.dtos.JivaBotPaymentDto;
import com.jiva.app.dtos.OutboundDetailsDto;
import com.jiva.app.dtos.OutcomeRequestDto;
import com.jiva.app.dtos.PatientDataRequest;
import com.jiva.app.dtos.PatientDataResponse;
import com.jiva.app.dtos.PatientDetailsDto;
import com.jiva.app.dtos.ResponseHandler;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.dtos.SMSDetails;
import com.jiva.app.dtos.ShareChatDto;
import com.jiva.app.dtos.ShareChatRequestDto;
import com.jiva.app.dtos.WacDocDto;
import com.jiva.app.dtos.WacDto;
import com.jiva.app.dtos.WacVCRequestDto;
import com.jiva.app.dtos.WacVCResponseDto;
import com.jiva.app.dtos.WhatsappRequest;
import com.jiva.app.dtos.WhatsappResponse;
import com.jiva.app.reposistory.JivaReposistory;
import com.jiva.app.reposistory.NiceReposistory;
import com.jiva.app.reposistory.SynchronyReposistory;
import com.jiva.app.service.SynchronyService;

import sync.pif.client.ActivityClient;
import sync.pif.client.ClientException;
import sync.pif.client.ContactClient;
import sync.pif.util.HandlerException;
import sync.pif.util.activity.Activity;
import sync.pif.util.contact.Address;
import sync.pif.util.contact.Contact;
import sync.pif.util.contact.EAddress;
import sync.pif.util.contact.Person;
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class SynchronyServiceImp implements SynchronyService{
	
	
	Logger logger = LoggerFactory.getLogger(SynchronyServiceImp.class);
	
	@Autowired
	private SynchronyReposistory syncRepo;

	@Autowired
	private JivaReposistory jivaRepo;
	
	
	@Autowired
	private WhatsappServiceImp whatsappService;
	
	private ContactClient oContactClient;
	private ActivityClient oActivityClient;
	
	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyTemplate;
	
	String strPattern = "^[0-9]{10}$";
	
	@Override
	public PatientDataResponse getPatientDataList(PatientDataRequest requestData) {
		PatientDataResponse response  = new PatientDataResponse();
		String orderId = "0";
		try {
			String name = syncRepo.getPatientName(requestData.getDhanid());
			List<PatientDetailsDto> details1 = syncRepo.getPatientDataDb(requestData.getDhanid());
			if(name!=null && !name.equals("")) {
					int newCode= 0;
					synchronized(this) 
			        { 
						newCode=jivaRepo.getCode();
						logger.info(newCode + "::newCode");
						orderId= requestData.getGroup() + newCode;
						logger.info(orderId + "::orderId");
						newCode++;
						jivaRepo.updateCode(newCode);
			        }
					response.setName(name);
					if(details1!=null && details1.size() >0) {
						PatientDetailsDto details = details1.get(0);
						response.setPlace(details.getPlace());
						response.setPincode(details.getPincode());
						if(details.getCityId()!=null && details.getCityId().length()  >0) {
							List<AddressDetailsDto> addrs1=jivaRepo.getAddressDetails(details.getCityId());
							if(addrs1!=null && addrs1.size() >0) {
								AddressDetailsDto addrs = addrs1.get(0);
								if(!addrs.getCityName().equals("") && addrs.getCityName().length() >0) {
									response.setCityName(addrs.getCityName());
								}
								if(!addrs.getStateName().equals("") && addrs.getStateName().length() >0) {
									response.setStateName(addrs.getStateName());
								}
							}
						}
						if(details.getCountryId()!=null && details.getCountryId().length() >0) {
							response.setCountryName(jivaRepo.getCountry(details.getCountryId()));
						}
					}
					response.setDhanId(requestData.getDhanid());
					
					response.setOrderId(orderId);
					jivaRepo.saveOnlinePayemnt(requestData.getDhanid(), requestData.getPhone(), requestData.getAmount(), orderId, requestData.getPmode(), requestData.getFcurrency(), requestData.getUserId(), requestData.getSno(),requestData.getFeedtype());
					if(response.getName().equals("0") || response.getName().equals("")){
						String patientName = syncRepo.getPatientName(requestData.getDhanid());
						if(patientName!=null && patientName.length() >0) {
							response.setName(patientName);
						}else {
							response.setName("");
						}
						
					}
					response.setMessage("Record found successfully");
				
			}else {
				response.setMessage("No Record Found");
			}
		}catch(Exception e) {
			logger.error("Error in getPatientDataList ->"+e.getMessage());
		}
		return response;
	}

	@Override
	public List<AppointmetStatusData> getAppointmentStatus(String dhanId) {
		List<AppointmetStatusData> responseList = new ArrayList<AppointmetStatusData>();
		try {
			List<AppointmetStatusData> statusList = syncRepo.getAppointmentStatusData(dhanId);
			if(null !=statusList && statusList.size() >0) {
				statusList.stream().forEach(data ->{
					String aptStatus = data.getAppointmentStatus();
					AppointmetStatusData listData = new AppointmetStatusData();
					listData.setDhanId(data.getDhanId());
					listData.setAppointmentDate(data.getAppointmentDate());
					listData.setPatientName(data.getPatientName());
					listData.setClinicName(data.getClinicName());
					 if(aptStatus.equals("0") || aptStatus.equals("1")){
		                  listData.setAppointmentStatus("Booked");
		              }else if(aptStatus.equals("2")){
		            	  listData.setAppointmentStatus("Converted");
		              }else if(aptStatus.equals("3")){
		            	  listData.setAppointmentStatus("Rejected");
		              }else if(aptStatus.equals("4")){
		            	  listData.setAppointmentStatus("Cancelled");
		              }
		              else if(aptStatus.equals("5")){
		            	  listData.setAppointmentStatus("Switched Off/Not Reachable/No Response/Call not received");
		              }else if(aptStatus.equals("6")){
		            	  listData.setAppointmentStatus("Already Consultation Done");
		              }else if(aptStatus.equals("7")){
		            	  listData.setAppointmentStatus("Rescheduled");
		              }
		              else if(aptStatus.equals("8")){  
		            	  listData.setAppointmentStatus("Cancelled");
		              }else if(aptStatus.equals("9")){ 
		            	  listData.setAppointmentStatus("Did not booked any appointment");
		              }else if(aptStatus.equals("10")){
		            	  listData.setAppointmentStatus("Will Arrive on Time");
		              }else if(aptStatus.equals("11")){	  
		            	  listData.setAppointmentStatus("General Enquiry");
		              }else if(aptStatus.equals("12")){ 
		            	  listData.setAppointmentStatus("Out of station");
		              }else if(aptStatus.equals("13")){ 
		            	  listData.setAppointmentStatus("Time not confirmed but will arrive");
		              }
					 listData.setPatientType("Fresh");
					 listData.setCreateDt(data.getCreateDt());
					 boolean result=jivaRepo.getPaitnetType(data.getDhanId(), data.getCreateDt());
					 if(result) {
						 listData.setPatientType("Repeat");
					 }
					 responseList.add(listData);
				});
			}
		}catch(Exception e) {
			logger.error("Error in getAppointmentStatus ->"+e.getMessage());
		}
		return responseList;
	}

	@Override
	public String createNewContact(CreateContactDto contactDto) {
		try {
			oContactClient = new ContactClient("nsood@cincom.com", "cincomsood1", "http://172.16.1.99:2138/rpcrouter");
			oActivityClient = new ActivityClient("nsood@cincom.com", "cincomsood1","http://172.16.1.99:2138/rpcrouter");
			if(contactDto.getMobile() !=null && contactDto.getName().length() >0  && contactDto.getMobile().length() > 0 && contactDto.getName() !=null) {
				String mobile= contactDto.getMobile();
				String name = contactDto.getName();
				name = name.replace("--"," ");
				boolean sFlag=false;
				String caseId="";
				if(mobile.length() == 12)
			    {
			      mobile = mobile.substring(2, mobile.length());
			    }
			    else if(mobile.length() == 11)
			    {
			      mobile = mobile.substring(1, mobile.length());
			    }
				if(syncRepo.isContactExist(mobile)) {
					caseId = syncRepo.getCaseId(syncRepo.getContactId(mobile));
					sFlag=false;
					if(caseId ==null || caseId.equals("")) {
						sFlag=true;
					}
				}else {
					sFlag=true;
				}
				if(sFlag) {
					Contact oContact = new Contact();
					Person oPerson = new Person();
					Activity oActivity = new Activity();
					oContact.setContactType("Customer");
				    oPerson.setFirstName(name);
					oPerson.setLastName(".");
					Vector<EAddress> vEAddress = new Vector<EAddress>();
					EAddress sphone = new EAddress();
					sphone.setAddressType("Telephone1");
					sphone.setAddressLocation("Home");
					sphone.setAddress(mobile);
					vEAddress.addElement(sphone);
					
					oContact.setEAddresses((Vector) vEAddress);
					oContact.setCampGroupID(1001L);
					oContact.setPerson(oPerson);
					long oContactId = 0l;
					try {
						oContactId = this.oContactClient.create(oContact);
					} catch (HandlerException | ClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					final Vector<Long> v = new Vector<Long>();
					v.add(new Long(oContactId));
					oActivity.setContactIDs((Vector) v);
					oActivity.setCampaignID(1001L);
					oActivity.setCategory("Ayurveda Call");
					oActivity.setSubCategory("Report Uploaded");
					long lActivityID = 0l;
					try {
						lActivityID = this.oActivityClient.create(oActivity);
						caseId = String.valueOf(lActivityID);
					} catch (HandlerException | ClientException e) {
						e.printStackTrace();
					}
				}
				return caseId;
			}
		}catch(Exception e) {
			logger.error("Error in createNewContact ->"+e.getMessage());
			return "";
		}
		return "";
	}
	
	public String saveDhanNotes(CaseNotesDto notesDto) {
		String result = "success";
		Connection conn=null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String hedNotes = "\'";
		hedNotes =hedNotes.concat("Note");
		hedNotes =hedNotes.concat("\'");
		String sUserId = "99";
		try {
			String mNotes = notesDto.getNotes();
			conn=synchronyTemplate.getDataSource().getConnection();
			String sql = "{ call SCC_SYSTEM_PKG_GET_ROW_ID_CASE_NOTE('SCC_CASE_NOTE',? )}";
		    cstmt = conn.prepareCall(sql);
		    cstmt.registerOutParameter(1, Types.NUMERIC, 0);
		    cstmt.execute();
		    java.math.BigDecimal seqBD = cstmt.getBigDecimal(1, 0);
		    long jobNo = seqBD.longValue();
		    cstmt.close();
		    mNotes = mNotes.replaceAll("%20", " ");
		    mNotes = mNotes.replaceAll(",", ".");
		    mNotes = mNotes.replaceAll("'", " ");
		    mNotes = mNotes.replaceAll("%", " ");
		    
		    sql = "INSERT INTO SCC_CASE_NOTE_T(CASE_NOTE_ID,CREATE_ID, CREATE_DT, HEADER, NOTE, CASE_ID, MODIFY_ID, MODIFY_DT) " +
		            "VALUES(" + jobNo + "," + sUserId + ",now()," + hedNotes + ",'" + mNotes + "'," + notesDto.getCaseId() + "," + sUserId + ", now())";
		    pstmt = conn.prepareStatement(sql);
		    pstmt.executeUpdate();
		    pstmt.close();
		    
		}catch(Exception e) {
			e.printStackTrace();
			result ="failure";
		}finally {
			try {
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e) {
				result ="failure";
				logger.error("Error in saveDhanNotes ->"+e.getMessage());
			}
		}
		return result;
	}
	
	@Override
	public String addCaseNotes(CaseNotesDto notesDto) {
		String result = "success";
		Connection conn=null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String hedNotes = "\'";
		hedNotes =hedNotes.concat("Note");
		hedNotes =hedNotes.concat("\'");
		String sUserId = "99";
		if(notesDto.getsUserId()!=null && notesDto.getsUserId().length() >0) {
			sUserId = notesDto.getsUserId();
		}
		try {
			String mNotes = notesDto.getNotes();
			conn=synchronyTemplate.getDataSource().getConnection();
			String sql = "{ call SCC_SYSTEM_PKG_GET_ROW_ID_CASE_NOTE('SCC_CASE_NOTE',? )}";
		    cstmt = conn.prepareCall(sql);
		    cstmt.registerOutParameter(1, Types.NUMERIC, 0);
		    cstmt.execute();
		    java.math.BigDecimal seqBD = cstmt.getBigDecimal(1, 0);
		    long jobNo = seqBD.longValue();
		    cstmt.close();
		    mNotes = mNotes.replaceAll("%20", " ");
		    mNotes = mNotes.replaceAll(",", ".");
		    mNotes = mNotes.replaceAll("'", " ");
		    mNotes = mNotes.replaceAll("%", " ");
		    
		    sql = "INSERT INTO SCC_CASE_NOTE_T(CASE_NOTE_ID,CREATE_ID, CREATE_DT, HEADER, NOTE, CASE_ID, MODIFY_ID, MODIFY_DT) " +
		            "VALUES(" + jobNo + "," + sUserId + ",now()," + hedNotes + ",'" + mNotes + "'," + notesDto.getCaseId() + "," + sUserId + ", now())";
		    pstmt = conn.prepareStatement(sql);
		    pstmt.executeUpdate();
		    pstmt.close();
		    
		}catch(Exception e) {
			logger.error("Error in addCaseNotes ->"+e.getMessage());
			result ="failure";
		}finally {
			try {
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e) {
				result ="failure";
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public ResponseHandler saveOutcome(OutcomeRequestDto outcomeReq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage getDispositionByUserId(String dhanId, String userId) {
		ResponseMessage resMes = new ResponseMessage();
		if(dhanId!=null && dhanId.length() >0 && userId!=null && userId.length() >0) {
			String contId = syncRepo.getContactByCase(dhanId);
			if(contId!=null && contId.length() >0) {
				List<DispositionResponseDto> resData = syncRepo.getDispositionData(contId, userId);
				resMes.setObj(resData);
				resMes.setMessage("Record found successfully");
				resMes.setStatusCode(200);
			}else {
				resMes.setMessage("Contact id does not exist.");
				resMes.setStatusCode(201);
			}
		}else {
			resMes.setMessage("Dhan Id or userId is required");
			resMes.setStatusCode(201);
		}
		return resMes;
	}

	@Override
	public ResponseMessage saveClinicCallToAdroid(ClinicFreshCallDto callDto) {
		ResponseMessage response  = new ResponseMessage();
		if(callDto.getCaseId().length() >0 && callDto.getPhone().length() >0 && callDto.getClinicName().length() >0) {
			String contactId=syncRepo.getContactByCase(callDto.getCaseId());
			if(contactId!=null && contactId.length() >0) {
				String email = syncRepo.getPhoneNoByType(contactId, "3");
				if(email==null || email.length() ==0) {
					email="";
				}
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String createdDt = LocalDateTime.now().format(formatter);
                
                String patientName = syncRepo.getPatientName(callDto.getCaseId());
                if(null == patientName)patientName=".";
                String url = "http://122.187.17.210//test.ajax?do=manualUpload&domainname=adro&campname=CLINIC_FRESH_CALL&phone1="+callDto.getPhone().trim()+"&skillname=CLINIC_FRESH_CALL&listname=CLINIC_FRESH_CALL&mobile=&caseid="+callDto.getCaseId().trim()+"&leadid=TEST1&leadtype=FCA&prefix=S3&name="+patientName+"&email="+email+"&leaddate="+createdDt+"&category=ADROIT&area="+callDto.getClinicName()+"&status=NEW&qname=adro";
                logger.info(url);
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(url, String.class);
                logger.info(result);
                
                AdroidResponseDto resDto = new AdroidResponseDto();
                resDto.setAdroidResponse(result);
                resDto.setCaseId(callDto.getCaseId());
                resDto.setContactId(contactId);
                resDto.setPhone(callDto.getPhone());
                syncRepo.insertAdroidResponse(resDto);
                
                AdroidFirstConnectDto connectDto = new AdroidFirstConnectDto();
                connectDto.setCaseId(callDto.getCaseId());
                connectDto.setContactId(contactId);
                connectDto.setAssignedUserId("0");
                connectDto.setPhone1(callDto.getPhone());
                connectDto.setPhone2("");
                connectDto.setCreatId("10001");
                syncRepo.insertAdroidFirstConnectData(connectDto);
                
                response.setMessage(result);
				response.setStatusCode(200);
                
			}else {
				response.setMessage("contact not found");
				response.setStatusCode(400);
			}
		}else {
			response.setMessage("Fileds are mandatory");
			response.setStatusCode(400);
		}
		return response;
	}

	@Override
	public ResponseMessage getDispositionBySessionId(String sessionId, String userId) {
		ResponseMessage resMes = new ResponseMessage();
		if(sessionId!=null && sessionId.length() >0 && userId!=null && userId.length() >0) {
				List<DispositionResponseDto> resData = syncRepo.getDispositionBySession(sessionId, userId);
				if(resData!=null && resData.size() >0) {
					resMes.setObj(resData);
					resMes.setMessage("Record found successfully");
					resMes.setStatusCode(200);
				}else {
					resMes.setMessage("No Record Found");
					resMes.setStatusCode(201);
				}
		}else {
			resMes.setMessage("Dhan Id or userId is required");
			resMes.setStatusCode(201);
		}
		return resMes;
	}

	@Override
	public ResponseMessage getOutboundDetails(String fromDate, String toDate) {
		ResponseMessage resMessage = new  ResponseMessage();
		if(fromDate==null || fromDate.length() ==0 || toDate==null || toDate.length() == 0) {
			resMessage.setMessage("Date is mandatory");
			resMessage.setStatusCode(500);
		}else {
			List<OutboundDetailsDto> oDetails = syncRepo.getDigitalDashboardDetails(fromDate,toDate);
			if(oDetails!=null && oDetails.size() >0) {
				resMessage.setObj(oDetails);
				resMessage.setStatusCode(200);
				resMessage.setMessage("Record found successfully");
			}else {
				resMessage.setObj(oDetails);
				resMessage.setStatusCode(400);
				resMessage.setMessage("No Record found!");
			}
			
		}
		return resMessage;
	}

	@Override
	public ResponseMessage getInboundDetails(String fromDate, String toDate) {
		ResponseMessage resMessage = new  ResponseMessage();
		if(fromDate==null || fromDate.length() ==0 || toDate==null || toDate.length() == 0) {
			resMessage.setMessage("Date is mandatory");
			resMessage.setStatusCode(500);
		}else {
			List<InboundDetailsDto> iDetails = syncRepo.getInboundDashboard(fromDate,toDate);
			if(iDetails!=null && iDetails.size() >0) {
				resMessage.setObj(iDetails);
				resMessage.setStatusCode(200);
				resMessage.setMessage("Record found successfully");
			}else {
				resMessage.setObj(iDetails);
				resMessage.setStatusCode(400);
				resMessage.setMessage("No Record found!");
			}
			
		}
		return resMessage;
	}
	

	@Override
	public ResponseMessage createCustomDhanvantariid(WacDto wacDto) {
		ResponseMessage resMsg = new ResponseMessage();
		try {
			oContactClient = new ContactClient("nsood@cincom.com", "cincomsood1", "http://172.16.1.99:2138/rpcrouter");
			oActivityClient = new ActivityClient("nsood@cincom.com", "cincomsood1","http://172.16.1.99:2138/rpcrouter");
			String strPattern = "^[0-9]{10}$";
			String phone = wacDto.getPhone();
			String name = wacDto.getPatientName();
			String age=wacDto.getAge();
			String gender=wacDto.getGender();
			String disease=wacDto.getDisease();
			
			boolean validateMobile = phone.matches(strPattern);
			boolean sFlag=false;
			String dhanId="";
			WacDto responseWac = new WacDto();
			if(phone ==null || phone.length()==0) {
				resMsg.setMessage("Not a valid number!");
				resMsg.setStatusCode(403);
			}else if(name.length() > 25) {
				resMsg.setMessage("Length of name should be less than 25 char!");
				resMsg.setStatusCode(403);
			}
			else {
				if(syncRepo.isContactExist(phone)) {
					dhanId = syncRepo.getCaseId(syncRepo.getContactId(phone));
					sFlag=false;
					if(dhanId ==null || dhanId.equals("")) {
						sFlag=true;
					}
				}else {
					sFlag=true;
				}
				if(sFlag) {
					Contact oContact = new Contact();
					Person oPerson = new Person();
					Activity oActivity = new Activity();
					oContact.setContactType("Customer");
				    oPerson.setFirstName(name);
					oPerson.setLastName(".");
					oPerson.setGender(gender);
					oPerson.setMiddleName(age);				
					Vector<EAddress> vEAddress = new Vector<EAddress>();
					EAddress sphone = new EAddress();
					sphone.setAddressType("Telephone1");
					sphone.setAddressLocation("Home");
					sphone.setAddress(phone);
					vEAddress.addElement(sphone);
				
					oContact.setEAddresses((Vector) vEAddress);
					oContact.setCampGroupID(1001L);
					oContact.setPerson(oPerson);
					long oContactId = 0l;
					try {
						oContactId = this.oContactClient.create(oContact);
					} catch (HandlerException | ClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					final Vector<Long> v = new Vector<Long>();
					v.add(new Long(oContactId));
					oActivity.setContactIDs((Vector) v);
					oActivity.setCampaignID(1001L);
					oActivity.setCategory("Ayurveda Call");
					oActivity.setSubCategory("Report Uploaded");
					long lActivityID = 0l;
					try {
						lActivityID = this.oActivityClient.create(oActivity);
						dhanId = String.valueOf(lActivityID);
					} catch (HandlerException | ClientException e) {
						e.printStackTrace();
					}
					
					syncRepo.updateDisease(disease,oContactId);
					
				}
				
				
				responseWac.setDhanId(dhanId);
				responseWac.setPatientName(name);
				responseWac.setPhone(phone);
				responseWac.setAge(age);
				responseWac.setGender(gender);
				responseWac.setDisease(disease);
				
            	//sending response back
				resMsg.setMessage("Record created successfully");
				resMsg.setStatusCode(200);
				resMsg.setObj(responseWac);
			}
		}catch(Exception e) {
			e.printStackTrace();
			resMsg.setMessage("Error!Please contact to administrator");
			resMsg.setStatusCode(500);
			logger.error("Error in createDhanvantariid ->"+e.getMessage());
		}
		return resMsg;
	}


	@Override
	public ResponseMessage createDhanvantariid(WacDto wacDto) {
		ResponseMessage resMsg = new ResponseMessage();
		try {
			oContactClient = new ContactClient("nsood@cincom.com", "cincomsood1", "http://172.16.1.99:2138/rpcrouter");
			oActivityClient = new ActivityClient("nsood@cincom.com", "cincomsood1","http://172.16.1.99:2138/rpcrouter");
			String strPattern = "^[0-9]{10}$";
			String phone = wacDto.getPhone();
			String name = wacDto.getPatientName();
			String age=wacDto.getAge();
			String gender=wacDto.getGender();
			String disease=wacDto.getDisease();
			
			boolean validateMobile = phone.matches(strPattern);
			boolean sFlag=false;
			String dhanId="";
			WacDto responseWac = new WacDto();
			if(phone ==null || phone.length()==0 || !validateMobile) {
				resMsg.setMessage("Not a valid number!");
				resMsg.setStatusCode(403);
			}else if(name.length() > 25) {
				resMsg.setMessage("Length of name should be less than 25 char!");
				resMsg.setStatusCode(403);
			}
			else {
				if(syncRepo.isContactExist(phone)) {
					dhanId = syncRepo.getCaseId(syncRepo.getContactId(phone));
					sFlag=false;
					if(dhanId ==null || dhanId.equals("")) {
						sFlag=true;
					}
				}else {
					sFlag=true;
				}
				if(sFlag) {
					Contact oContact = new Contact();
					Person oPerson = new Person();
					Activity oActivity = new Activity();
					oContact.setContactType("Customer");
				    oPerson.setFirstName(name);
					oPerson.setLastName(".");
					oPerson.setGender(gender);
					oPerson.setMiddleName(age);				
					Vector<EAddress> vEAddress = new Vector<EAddress>();
					EAddress sphone = new EAddress();
					sphone.setAddressType("Telephone1");
					sphone.setAddressLocation("Home");
					sphone.setAddress(phone);
					vEAddress.addElement(sphone);
				
					oContact.setEAddresses((Vector) vEAddress);
					oContact.setCampGroupID(1001L);
					oContact.setPerson(oPerson);
					long oContactId = 0l;
					try {
						oContactId = this.oContactClient.create(oContact);
					} catch (HandlerException | ClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					final Vector<Long> v = new Vector<Long>();
					v.add(new Long(oContactId));
					oActivity.setContactIDs((Vector) v);
					oActivity.setCampaignID(1001L);
					oActivity.setCategory("Ayurveda Call");
					oActivity.setSubCategory("Report Uploaded");
					long lActivityID = 0l;
					try {
						lActivityID = this.oActivityClient.create(oActivity);
						dhanId = String.valueOf(lActivityID);
					} catch (HandlerException | ClientException e) {
						e.printStackTrace();
					}
					
					syncRepo.updateDisease(disease,oContactId);
					
				}
				
				
				responseWac.setDhanId(dhanId);
				responseWac.setPatientName(name);
				responseWac.setPhone(phone);
				responseWac.setAge(age);
				responseWac.setGender(gender);
				responseWac.setDisease(disease);
				
            	//sending response back
				resMsg.setMessage("Record created successfully");
				resMsg.setStatusCode(200);
				resMsg.setObj(responseWac);
			}
		}catch(Exception e) {
			e.printStackTrace();
			resMsg.setMessage("Error!Please contact to administrator");
			resMsg.setStatusCode(500);
			logger.error("Error in createDhanvantariid ->"+e.getMessage());
		}
		return resMsg;
	}

	@Transactional
	@Override
	public ResponseMessage createVideoLink(WacVCRequestDto requestDto) {
		ResponseMessage resMsg = new ResponseMessage();
		String dhanId = requestDto.getDhanId();
		String botId = requestDto.getBotId();
		String roomId ="";
		WacVCResponseDto responseDto = new WacVCResponseDto();
		try {
			if(dhanId==null || dhanId.length() == 0) {
				resMsg.setMessage("DhanvantariId is required");
				resMsg.setStatusCode(403);
			}else if(botId == null || botId.length() == 0) {
				resMsg.setMessage("Bot Id is required!");
				resMsg.setStatusCode(403);
			}else {
				String nt= "Consult Area :"+requestDto.getConsult_area() +" \n"+
							"Specialty :"+requestDto.getSpecialty() + " \n" +
							"Patient age :"+requestDto.getPatient_age() + " \n"+
							"Gender :"+requestDto.getGender() + " \n" +
							"Language :" + requestDto.getLanguage();
				CaseNotesDto notes = new CaseNotesDto();
				notes.setCaseId(dhanId);
				notes.setNotes(nt);
				String notesResponse =saveDhanNotes(notes);
				
				String sData = "http://ayurveda.jiva.com/path-to-sample-app/api/create-room/";
		        RestTemplate restTemplate = new RestTemplate();
		        String apiresult = restTemplate.getForObject(sData, String.class);
		        JSONObject json = new JSONObject(apiresult);
		        roomId = json.getJSONObject("room").getString("room_id");
		        
		        String patientUrl="https://ayurveda.jiva.com/path-to-sample-app/client/welcome.php?caseId="+dhanId+"&roomId="+roomId;
		        responseDto.setConsultation_link(patientUrl);
		        responseDto.setDhanId(dhanId);
		        resMsg.setMessage("Consultation booked successfully");
				resMsg.setStatusCode(200);
				resMsg.setObj(responseDto);
				
				
				//String sentFor=syncRepo.wacSentFor();
				//if(sentFor == null || sentFor.length() == 0) {
					//sentFor="Sujeet";
				//}
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
				
				java.util.Date date1 = new java.util.Date();
			    String cDate = formatter1.format(date1);
			   
			    java.util.Date date2 = new java.util.Date();
			    String cTime = formatter2.format(date2);
			   
			    
			    WhatsappResponse whatsResponse = null;
				String doctorUrl = "https://ayurveda.jiva.com/path-to-sample-app/client/doctor-welcome.php?caseId="+dhanId+"&roomId="+roomId+"&docId=15841";
				doctorUrl =URLEncoder.encode(doctorUrl, "UTF-8");
				
				String mText= "Dear+%2ADoctor%2A%2C+Your+%2AVideo+consultation%2A+"+doctorUrl+"+has+been+booked+for+"+cDate+"+"+cTime+".+For+more+details%2C+kindly+visit+the+DID%3A+"+dhanId+"%2C+WhatsApp+group+or+speak+to+your+%2Areporting+manager+at+Jiva.%2A";
				whatsResponse =  whatsappService.sendWhatsappText("8920600820", mText);
				logger.info(mText);
				if (whatsResponse != null && whatsResponse.getResponseStatus().equalsIgnoreCase("success")) {
					mText = mText.replace("'", "");
					WhatsappRequest request = new WhatsappRequest();
					request.setOrderCode("0");
					request.setDhanId(dhanId);
					request.setWhatsappId(whatsResponse.getWhatsappId());
					request.setMobileNo("8920600820");
					request.setStatus(whatsResponse.getResponseStatus());
					request.setSentFor("WAC_MSG");
					request.setGrpId("0");
					request.setSmsText(mText);
					syncRepo.saveWhatsappResponse(request);
				} 
				WacDocDto docDto = new WacDocDto();
				docDto.setDhanId(dhanId);
				docDto.setDocId("15841");
				docDto.setVcLink(doctorUrl);
				docDto.setSendFor("Sujeet");
				docDto.setVcStatus("1");
				syncRepo.saveWacDetails(docDto);
			}
		}catch(Exception e) {
			resMsg.setMessage("Error!Please contact to administrator");
			resMsg.setStatusCode(500);
			logger.error("Error in createVideoLink ->"+e.getMessage());
		}
		return resMsg;
	}
	public String getTinyUrl(String url){
		String retval="";
		String output="";
		URL providerURL  = null;
		HttpURLConnection urlConn = null;
		try{
			String sData = "http://tinyurl.com/api-create.php?url=" + url;
			logger.info(sData);
			providerURL = new URL(sData);
			urlConn = (HttpURLConnection)providerURL.openConnection();
			urlConn.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader((urlConn.getInputStream())));
			while ((output = br.readLine()) != null)
			{
				retval+=output;
			}
		 }catch(Exception e){
			 logger.error("Error in getTinyUrl ->"+e.getMessage());
			retval="failure";
		}
		return retval;
	}

	@Override
	public ResponseMessage getBotPaymentStatus(String phone,String botId) {
		ResponseMessage response = new ResponseMessage();
		try {
			if(null ==phone || phone.length() ==0 || botId ==null || botId.length() == 0) {
				response.setMessage("Fields are mandatory");
				response.setObj(null);
				response.setStatusCode(400);
			}else {
			  String status	= jivaRepo.getBotPaymentStatus(phone, botId);
			  if(status!=null) {
				  response.setMessage("Record found successfully");
				  response.setObj(status);
				  response.setStatusCode(200); 
			  }else {
				  response.setMessage("Failure");
				  response.setObj(status);
				  response.setStatusCode(400); 
			  }
			}
		}catch(Exception e) {
			response.setMessage("Failure!");
			response.setObj(null);
			response.setStatusCode(400);
			logger.error("Error in getBotPaymentStatus ->"+e.getMessage());
		}
		return response;
	}

	@Override
	public ResponseMessage updateBotPayment(String botid,String amount,String status) {
		ResponseMessage response = new ResponseMessage();
		try {
			if(null ==status || status.length() ==0 || botid ==null || botid.length() == 0 || amount==null || amount.length() ==0) {
				response.setMessage("Fields are mandatory");
				response.setObj(null);
				response.setStatusCode(400);
			}else {
				int r =jivaRepo.updateBotPayment(botid, amount, status);
				if(r>0) {
					response.setMessage("Payment updated successfully");
					response.setObj(null);
					response.setStatusCode(200);
				}else {
					response.setMessage("Failure!");
					response.setObj(null);
					response.setStatusCode(400);
				}
			}
		}catch(Exception e) {
			response.setMessage("Failure!");
			response.setObj(null);
			response.setStatusCode(400);
			logger.error("Error in updateBotPayment ->"+e.getMessage());
		}
		return response;
	}

	@Override
	public String getDhanPaymentUser(String dhanId, String userId, String type) {
		String response="";
		try {
		 if(dhanId == null || dhanId.length() ==0 || userId==null || userId.length() ==0 || type ==null || type.length() ==0) {
			 response = "Fields are mandatory";
		 }else {
			response = syncRepo.getPaymentUsername(dhanId, type, userId);
		 }
		}catch(Exception e) {
			logger.error("Error in getDhanPaymentUser ->"+e.getMessage());
			response="Failure";
		}
		return response;
	}
	
	@Override
	public String saveDhanSms(SMSDetails details) {
		String response="Failure";
		try {
			if(details.getDhanId() != null ) {
				syncRepo.saveSmsDetails(details);
				response="Record saved successfully";
			}
		}catch(Exception e) {
			logger.error("Error in saveDhanSms ->"+e.getMessage());
			response="Failure";
		}
		return response;
	}

	@Override
	public String addGenericInternalList(String caseId, String listName) {
		String response="Failure";
		try {
			if(caseId!=null && caseId.length() >0 && listName!=null && listName.length() >0) {
				if(syncRepo.createInternalList(caseId, listName) > 0) {
					response="Record saved successflly";
				}else {
					response="Record already exist";
				}
			}
		}catch(Exception e) {
			logger.error("Error in addGenericInternalList ->"+e.getMessage());
			response="Failure";
		}
		return response;
	}

	@Override
	public String getClinicUserNameByCode(String groupName) {
		return syncRepo.getClinicUserName(groupName);
	}

	@Override
	public Integer getClinicApptByGroupName(ClinicAppointmentDto apptDto) {
		return syncRepo.getClinicAppointmentCountByGroupName(apptDto);
	}

	@Override
	public String createClinicAppt(ClinicApptRequestDto request) {
		return syncRepo.createClinicAppointment(request);
	}

	@Override
	public ResponseMessage getSahreChatData(ShareChatRequestDto chatDto) {
		logger.info("ShareChat started---->" +chatDto);
		ResponseMessage response = new ResponseMessage();
		String phone = chatDto.getPhone_no();
		try {
		 if(phone==null || phone.length() == 0 || !phone.matches(strPattern)) {
			 response.setMessage("Mobile number is mandatory and should be 10 digit");
			 response.setStatusCode(400);
		 }else {
				 // insert sharechat details
				 ShareChatDto shareDetails = new ShareChatDto();
				 shareDetails.setSUBMIT_DT(chatDto.getDate());
				 shareDetails.setWEB_CARD(chatDto.getWebcard());
				 shareDetails.setNAME(chatDto.getName());
				 shareDetails.setMOBILE(phone);
				 shareDetails.setCITY(chatDto.getCity());
				 shareDetails.setSRC(chatDto.getSrc());
				 logger.info("ShareChat details----->"+shareDetails);
				 int result = syncRepo.saveShareChatDetails(shareDetails);
				 if(result >0) {
					 response.setMessage("Record has been saved successfully");
					 response.setStatusCode(201);
					 logger.info("sharechat lead inserted successfully "+ phone);
				 }else {
					 response.setMessage("Failure!.Record not inserted!");
					 response.setStatusCode(500);
					 logger.error("sharechat lead failure "+ phone);
				 }
		 }
		}catch(Exception e) {
			logger.error("Error in getSahreChatData ->"+e.getMessage());
			response.setMessage("Failure! Record not saved");
			response.setStatusCode(500);
		}
		return response;
	}

	@Override
	public List<Map<String, Object>> getRddCityData(String dhanId) {
		return syncRepo.getRddCityRepo(dhanId);
	}

	@Override
	public String getConsultationFormStatus(String dhanId) {
		return syncRepo.getConsultationStatus(dhanId);
	}

	@Override
	public ResponseMessage getIPerformanceOutbound(IperformanceObDto ipoutbound) {
		ResponseMessage response = new ResponseMessage();
		try {
			int result = syncRepo.saveIpOutboundDetails(ipoutbound);
			 if(result >0) {
				 response.setMessage("Record has been saved successfully");
				 response.setStatusCode(201);
				 logger.info("Iperformance Outbound lead inserted successfully "+ ipoutbound.getpPhone());
			 }else {
				 response.setMessage("Failure!.Record not inserted!");
				 response.setStatusCode(500);
				 logger.error("Iperformance Outbound lead failure "+ ipoutbound.getpPhone());
			 }
		}catch(Exception e) {
			logger.error("Error in getIPerformanceOutbound ->"+e.getMessage());
			response.setMessage("Failure! Record not saved");
			response.setStatusCode(500);
		}
		return response;
	}

	
}
