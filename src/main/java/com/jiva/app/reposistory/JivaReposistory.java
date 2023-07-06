package com.jiva.app.reposistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jiva.app.dtos.AddressDetailsDto;
import com.jiva.app.dtos.JivaBotPaymentDto;

import java.util.List;

@Repository
public class JivaReposistory {

	
	Logger logger = LoggerFactory.getLogger(JivaReposistory.class);
	
	@Autowired
	@Qualifier("jivaTemplate")
	private JdbcTemplate jivaTemplate;
	
	
	public int getCode() {
		String sql="SELECT code from tmc_payment where id='1'";
		return jivaTemplate.queryForObject(sql,new Object[] {},Integer.class);
	}
	
	
	
	public double getTotalCharge(String caseId) {
		String sql="select IFNULL(sum(total_charge),'0.00') from jiva.order O,customer C where O.customer_id=C.id and dhanvantariid='"+caseId+"' order by O.id desc limit 1";
		return jivaTemplate.queryForObject(sql,new Object[] {},Double.class);
	}
	
	public String getCountry(String countryId) {
		try {
			String sql="select name from country where id=?";
			return jivaTemplate.queryForObject(sql,new Object[] {countryId},String.class);
		}catch(Exception e) {
			logger.error("Error in getCountry ->"+e.getMessage());
			return "";
		}
	}
	
	public void updateCode(int code) {
		String sql= "UPDATE tmc_payment SET code=? where id=1";
		jivaTemplate.update(sql,new Object[] {code});
	}
	
	public String getBotPaymentStatus(String phone,String botId) {
		try {
			String sql="SELECT status FROM jiva_bot_payment where phone='"+phone+"' and paycode='"+botId+"'";
			return jivaTemplate.queryForObject(sql,new Object[] {},String.class);
		}catch(Exception e) {
			logger.error("Error in getBotPaymentStatus ->"+e.getMessage());
			return null;
		}
	}
	
	public int updateBotPayment(String botid,String amount,String status) {
		String sql="update jiva_bot_payment set status=?,amount=?,paymentdt=now() where paycode=?";
		return jivaTemplate.update(sql,new Object[] {status,amount,botid});
	}
	
	public void saveBotPayment(JivaBotPaymentDto botDto) {
		String sql="insert into jiva_bot_payment(caseId,phone,status,paycode,amount,createdt) "+
				   "values(?,?,?,?,?,now())";
		jivaTemplate.update(sql,new Object[] {botDto.getCaseId(),botDto.getPhone(),botDto.getStatuss(),botDto.getPaycode(),botDto.getAmount()});
	}
	
	public void saveOnlinePayemnt(String dhanId,String phone,String amount,String payCode,String payMode,String curr,String userId,String smsId,String feedbackType) {
		String paymentDate = "0000-00-00 00:00:00";
		String sql="INSERT INTO online_payment (dhid, phone, amount,pay_code,payment_date,payment_mode,currency,userId,smsid,feedback_type) "+
		"VALUES (?,?,?,?,?,?,?,?,?,?)";
		jivaTemplate.update(sql,new Object[] {dhanId,phone,amount,payCode,paymentDate,payMode,curr,userId,smsId,feedbackType});
		
	}
	
	public boolean getPaitnetType(String dhanId,String createDt) {
		try {
			String sql="select count(customerid) from customer where dhanvantariid =? and created < ?";
			return jivaTemplate.queryForObject(sql,new Object[] {dhanId,createDt},Integer.class)>0?true:false;
		}catch(Exception e) {
			logger.error("Error in getPaitnetType ->"+e.getMessage());
			return false;
		}
	}
	
	public List<AddressDetailsDto> getAddressDetails(String cityId) {
		try {
			String sql = "SELECT c.name cityName,s.name stateName from city c, state s where c.state_id=s.id and c.id=?";
			return jivaTemplate.query(sql,new Object[] {cityId},new BeanPropertyRowMapper(AddressDetailsDto.class));
		}catch(Exception e) {
			logger.error("Error in getAddressDetails ->"+e.getMessage());
			return null;
		}
	}
	
	
}
