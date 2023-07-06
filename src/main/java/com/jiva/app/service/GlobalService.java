package com.jiva.app.service;

import java.util.List;
import java.util.Map;

public interface GlobalService {

	List<Map<String,Object>> userNameByChatname_service_get(Map<String,Object> reqParam);

	Map<String,Object> supportDetails_service_get(Map<String, Object> reqParam);

}
