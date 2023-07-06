package com.jiva.app.serviceImp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.jiva.app.dtos.WhatsappResponse;
import com.jiva.app.service.WhatsappService;

@Service
public class WhatsappServiceImp implements WhatsappService{

	@Override
	public WhatsappResponse sendWhatsappText(String phone, String mText) {
		WhatsappResponse resMsg =new  WhatsappResponse();
		URL providerURL  = null;
		HttpURLConnection urlConn = null;
		String output="";
		String result="";
		try {
			String  data = "userid=2000193973"; // your loginId
            data += "&password=" + URLEncoder.encode("ITuKLVvMV", "UTF-8"); // your password
            data += "&send_to=" + URLEncoder.encode(phone, "UTF-8"); // a valid 10 digit phone no.
            data += "&v=1.1";
            data += "&format=json";
            data += "&method=SENDMESSAGE";
            data += "&msg_type=TEXT";
            data +=	"&msg="+mText;
            String sData = "http://media.smsgupshup.com/GatewayAPI/rest?" + data;
            System.out.println(sData);
            providerURL = new URL(sData);
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
	        System.out.println(result);
	        JSONObject obj = new JSONObject(result);
            resMsg.setResponseStatus(obj.getJSONObject("response").getString("status"));
            if((obj.getJSONObject("response").getString("status").equals("success"))){
            	resMsg.setWhatsappId(obj.getJSONObject("response").getString("id"));
            }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return resMsg;
	}

	@Override
	public WhatsappResponse sendWhatsappImageText(String phone, String mText,String mediaUrl) {
		WhatsappResponse resMsg =new  WhatsappResponse();
		URL providerURL  = null;
		HttpURLConnection urlConn = null;
		String output="";
		String result="";
		try {
			String  data = "userid=2000193973"; // your loginId
            data += "&password=" + URLEncoder.encode("ITuKLVvMV", "UTF-8"); // your password
            data += "&send_to=" + URLEncoder.encode(phone, "UTF-8"); // a valid 10 digit phone no.
            data += "&v=1.1";
            data += "&format=json";
            data += "&method=SENDMEDIAMESSAGE";
            data += "&msg_type=IMAGE";
            data +=	"&caption="+mText;
            data +=	"&media_url="+URLEncoder.encode(mediaUrl, "UTF-8");
            String sData = "http://media.smsgupshup.com/GatewayAPI/rest?" + data;
            System.out.println(sData);
            providerURL = new URL(sData);
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
	        System.out.println(result);
	        JSONObject obj = new JSONObject(result);
            resMsg.setResponseStatus(obj.getJSONObject("response").getString("status"));
            if((obj.getJSONObject("response").getString("status").equals("success"))){
            	resMsg.setWhatsappId(obj.getJSONObject("response").getString("id"));
            }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return resMsg;
	}

}
