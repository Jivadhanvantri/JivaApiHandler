package com.jiva.app.service;

import com.jiva.app.dtos.AppointmetStatusData;
import com.jiva.app.dtos.CaseNotesDto;
import com.jiva.app.dtos.ClinicAppointmentDto;
import com.jiva.app.dtos.ClinicApptRequestDto;
import com.jiva.app.dtos.ClinicFreshCallDto;
import com.jiva.app.dtos.CreateContactDto;
import com.jiva.app.dtos.DispositionResponseDto;
import com.jiva.app.dtos.IperformanceObDto;
import com.jiva.app.dtos.OutboundDetailsDto;
import com.jiva.app.dtos.OutcomeRequestDto;
import com.jiva.app.dtos.PatientDataRequest;
import com.jiva.app.dtos.PatientDataResponse;
import com.jiva.app.dtos.ResponseHandler;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.dtos.SMSDetails;
import com.jiva.app.dtos.ShareChatRequestDto;
import com.jiva.app.dtos.WacDto;
import com.jiva.app.dtos.WacVCRequestDto;

import java.util.List;
import java.util.Map;

public interface SynchronyService {

	PatientDataResponse getPatientDataList(PatientDataRequest requestData);
	List<AppointmetStatusData> getAppointmentStatus(String dhanId);
	String createNewContact(CreateContactDto contactDto);
	String addCaseNotes(CaseNotesDto notesDto);
	ResponseHandler saveOutcome(OutcomeRequestDto outcomeReq);
	ResponseMessage getDispositionByUserId(String dhanId,String userId);
	ResponseMessage getDispositionBySessionId(String sessionId,String userId);
	ResponseMessage saveClinicCallToAdroid(ClinicFreshCallDto callDto);
	ResponseMessage getOutboundDetails(String fromDate,String toDate);
	ResponseMessage getInboundDetails(String fromDate,String toDate);
	ResponseMessage createDhanvantariid(WacDto wacDto);
	ResponseMessage createVideoLink(WacVCRequestDto requestDto);
	ResponseMessage getBotPaymentStatus(String phone,String botId);
	ResponseMessage updateBotPayment(String botId,String amount,String status);
	String getDhanPaymentUser(String dhanId,String userId,String type);
	String saveDhanSms(SMSDetails details);
	String addGenericInternalList(String caseId,String listName);
	String getClinicUserNameByCode(String groupName);
	Integer getClinicApptByGroupName(ClinicAppointmentDto apptDto);
	String createClinicAppt(ClinicApptRequestDto request);
	String getConsultationFormStatus(String dhanId);
	ResponseMessage getSahreChatData(ShareChatRequestDto chatDto);
	List<Map<String, Object>> getRddCityData(String dhanId);
	ResponseMessage getIPerformanceOutbound(IperformanceObDto ipoutbound);
	ResponseMessage createCustomDhanvantariid(WacDto wacDto);
	
	
}
