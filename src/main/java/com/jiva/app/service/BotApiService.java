package com.jiva.app.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import com.jiva.app.dtos.BotAvailableSlotDto;
import com.jiva.app.dtos.BotPaymentLinkGeneratorDto;
import com.jiva.app.dtos.ResponseMessage;
import com.jiva.app.dtos.WacDto;

public interface BotApiService {

	ResponseMessage checkIsRegister_service(@NotBlank @Max(10) String phoneno);

	ResponseMessage clinicbypincode_service(@NotBlank @Max(10) String pincode);

	ResponseMessage generatePaymentLink_service(BotPaymentLinkGeneratorDto paymentlinkDto);

	ResponseMessage getDiseaselist_service();

	ResponseMessage cancelAppointment_service(String agentId, String appointmentId);

	ResponseMessage availableSlot_service(String groupName, String slotDate) throws Exception;

	ResponseMessage getClinicState_service();

	ResponseMessage getClinicCity_service(String state);

	ResponseMessage getClinicByCityName_service(String city);

	ResponseMessage sendOtp_service(String phoneno);

	ResponseMessage verifyOtp_service(String phoneno, String otp);

	ResponseMessage getPatientlistByParam_service(String phoneno, String caseId);

	ResponseMessage getPatientAppointmentlist_service(String phoneno, String caseId);

	ResponseMessage mrcTCAppointment_service(String caseId);

	ResponseMessage bookedIMRCAppointment_service(String caseId) throws Exception;

}
