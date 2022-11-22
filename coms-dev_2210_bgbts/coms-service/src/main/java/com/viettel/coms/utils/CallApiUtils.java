package com.viettel.coms.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.viettel.ktts2.common.BusinessException;

public class CallApiUtils {
	static HttpHeaders HEADERS;
	
	private static Boolean startCallUrl(String linkCall, RestTemplate restTemplate, HttpEntity<Object> request) throws Exception{
		ResponseEntity<String> personResultAsJsonStr = restTemplate.postForEntity(linkCall , request, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(personResultAsJsonStr.getBody());
		root = objectMapper.convertValue(personResultAsJsonStr.getBody(), JsonNode.class);
        JsonNode jsonRoot = objectMapper.readTree(root.asText());
        String result = jsonRoot.toString();

        JSONObject jsonObj = new JSONObject(result);
        @SuppressWarnings("unchecked")
        Map<String, String> map = new Gson().fromJson(jsonObj.toString(),Map.class);
        System.out.println(map);

        String value = String.valueOf(map.get("status"));

        if(StringUtils.isNotBlank(value) && value.equals("200")) {
        	return true;
        }
        
		return false;
	}
	
	public static String callApiResponseTicketString(String linkCall, JSONObject personJsonObject){
		String result = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

//			HttpEntity<Object> request = new HttpEntity<Object>(personJsonObject.toString(), headers);
			HttpEntity<Object> request = new HttpEntity<Object>(personJsonObject.toString(), getDefaultHeader());
			
			return startCallTicketCRM(linkCall, restTemplate, request);
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}
	
	private static String startCallTicketCRM(String linkCall, RestTemplate restTemplate, HttpEntity<Object> request) throws Exception{
		ResponseEntity<String> personResultAsJsonStr = restTemplate.postForEntity(linkCall , request, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(personResultAsJsonStr.getBody());
		root = objectMapper.convertValue(personResultAsJsonStr.getBody(), JsonNode.class);
        JsonNode jsonRoot = objectMapper.readTree(root.asText());
        String result = jsonRoot.toString();

        
		return result;
	}

	public static Boolean callApiResponseString(String linkCall, JSONObject personJsonObject){
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

//			HttpEntity<Object> request = new HttpEntity<Object>(personJsonObject.toString(), headers);
			HttpEntity<Object> request = new HttpEntity<Object>(personJsonObject.toString(), getDefaultHeader());
			
			return startCallUrl(linkCall, restTemplate, request);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String callApiCrm(String linkCall, JSONObject personJsonObject) {
		Boolean checkCallApiCrm = callApiResponseString(linkCall, personJsonObject);
		String response = null;
		if(!checkCallApiCrm) {
			response = "Call sang CRM trả về trạng thái lỗi: " + personJsonObject.toString();
		} else {
			response = "Call sang CRM thành công: " + personJsonObject.toString();
		}
		
		return response;
	}
	
	public static HttpHeaders getDefaultHeader() {
        if (HEADERS == null) {
            HEADERS = new HttpHeaders();
            HEADERS.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HEADERS.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        }
        return HEADERS;
    }

	public static Boolean connectionCallbot(String url, JSONObject personJsonObject){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println("Func - Start time: " + dtf.format(now));
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
			ResponseEntity<String> personResultAsJsonStr;
			personResultAsJsonStr = restTemplate.postForEntity(url, request, String.class);
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(personResultAsJsonStr.getBody());
			root = objectMapper.convertValue(personResultAsJsonStr.getBody(), JsonNode.class);
            JsonNode jsonRoot = objectMapper.readTree(root.asText());
            String result = jsonRoot.toString();

            JSONObject jsonObj = new JSONObject(result);
            @SuppressWarnings("unchecked")
            Map<String, String> map = new Gson().fromJson(jsonObj.toString(),Map.class);
            System.out.println(map);
 
            String value = map.get("msg");
            
            LocalDateTime endNow = LocalDateTime.now();
            System.out.println("Func - End time: " + dtf.format(endNow));
            
            if(StringUtils.isNotBlank(value) && value.equalsIgnoreCase("Success")) {
            	return true;
            }
            
			return false;
		} catch (Exception e) {
			LocalDateTime endNow = LocalDateTime.now();
            System.out.println("Exception - End time: " + dtf.format(endNow));
			e.printStackTrace();
			throw new BusinessException("Có lỗi xảy ra khi connect erp");
		}
	}

}
