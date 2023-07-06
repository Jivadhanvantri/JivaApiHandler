package com.jiva.app.serviceImp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jiva.app.service.SMSService;

@Service
public class SMSServiceImp implements SMSService {
	
	private Logger logger=LoggerFactory.getLogger(SMSServiceImp.class);

	@Override
	public String sendSmsWithGupSup(String uri) {
		String result="";
		URL providerURL  = null;
		HttpURLConnection urlConn = null;
		String output="";
		Map<String,Object> response=new HashMap();
		
		try {
				RestTemplate restTemplate = new RestTemplate();
				result = restTemplate.getForObject(uri, String.class);
				logger.info(result);
			
	            providerURL = new URL(uri);
	            urlConn = (HttpURLConnection) providerURL.openConnection();
	            urlConn.setDoInput(true);
	            urlConn.setDoOutput(true);
	            urlConn.setUseCaches(false);
	            urlConn.connect();
	            if(urlConn.getResponseCode() == 200) {
	                BufferedReader br = new BufferedReader(new InputStreamReader((urlConn.getInputStream())));
	                while ((output = br.readLine()) != null) {
	                    result += output;
	                }
	            }
		        logger.info(result);
	
	            
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

}
