package com.jiva.app.reposistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jiva.app.dtos.DispositionResponseDto;
import com.jiva.app.dtos.NiceDetailsDto;

import java.util.List;

@Repository
public class NiceReposistory {
	
	@Autowired
	@Qualifier("niceTemplate")
	private JdbcTemplate niceTemplate;
	
	
	/*public List<NiceDetailsDto> getNiceDetails(String sessionId){
		String sql="SELECT sessionid, caseid, b.duration as diff,c.calldate as dialDate, case when (connecttime='0000-00-00 00:00:00') then NULL else connecttime end as answerDate,"+
	              "case when (hanguptime='0000-00-00 00:00:00') then NULL else hanguptime end as disconnectDate,'' as incomingDid FROM nice.crmtodialer a "+
	              "JOIN obcalldetails b ON a.uniqueid=b.uniqueid "+
	              "JOIN cdr c ON a.uniqueid=c.uniqueid "+
	              "WHERE sessionid='"+sessionId+"'"+
	              " union all "+
	              "SELECT srcsessionid AS sessionid, b.caseid AS caseid,case when DATA2='' then substring_index(SUBSTRING(data, INSTR(data, '|')+1), '|', 1)  as diff"+
	              "ELSE DATA2 end as talktime,b.calltime as dialDate, calltime AS answerDate, TIME AS disconnectDate, tollfreenum as incomingDid FROM vw_queuelog a "+
	              "JOIN tollfree_track c ON a.callid=c.uniqueid JOIN ibobtransfer b ON a.callid=b.callid WHERE  EVENT IN ('COMPLETECALLER','COMPLETEAGENT') and srcsessionid ='"+sessionId+"'";
		System.out.println(sql);
		return niceTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper(NiceDetailsDto.class));
	}*/
}
