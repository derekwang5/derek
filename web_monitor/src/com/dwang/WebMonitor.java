package com.dwang;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebMonitor {
	
	private final String USER_AGENT = "Chrome/37.0.2062.124";

	private List<String> cookies;
	private HttpsURLConnection conn;
	
	public static void main(String[] args) throws Exception {
		String url = "https://reserve.cdn-apple.com/HK/en_HK/reserve/iPhone/availability";
		
		WebMonitor webMonitor = new WebMonitor();
		
		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());
	 
		// 1. Send a "GET" request, so that you can extract the form's data.
		String page = webMonitor.GetPageContent(url);
		System.out.println(page);
		
//        webMonitor.doSubmit(url, data);
		
		String postParams = webMonitor.getFormParams(page, null, null);
		
		// 2. Construct above post's content and then send a POST request for
		// authentication
		webMonitor.sendPost(url, postParams);
	}
	
	private void sendPost(String url, String postParams) throws Exception {
		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();
	 
		// Acts like a browser
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("HOST", "accounts.google.com");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//		for (String cookie : this.cookies) {
//			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
//		}
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
	 
		conn.setDoOutput(true);
		conn.setDoInput(true);
	 
		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
	 
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + postParams);
		System.out.println("Response Code : " + responseCode);
	 
		BufferedReader in = 
	             new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		
	}

	public void doSubmit(String url, Map<String, String> data) throws Exception{
		URL siteUrl = new URL(url);
	    HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setDoOutput(true);
	    conn.setDoInput(true);

	    DataOutputStream out = new DataOutputStream(conn.getOutputStream());

	    Set keys = data.keySet();
	    Iterator keyIter = keys.iterator();
	    String content = "";
	    for(int i=0; keyIter.hasNext(); i++) {
	        Object key = keyIter.next();
	        if(i!=0) {
	            content += "&";
	        }
	        content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
	    }
	    System.out.println(content);
	    out.writeBytes(content);
	    out.flush();
	    out.close();
	    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line = "";
	    while((line=in.readLine())!=null) {
	        System.out.println(line);

	    }
	    in.close();
	}
	
	public String getFormParams(String html, String username, String password)
			throws UnsupportedEncodingException {
	 
		System.out.println("Extracting form's data...");
	 
		Document doc = Jsoup.parse(html);
	 
		// Google form id
		//Element loginform = doc.getElementById("gaia_loginform");
//		Elements e = doc.getElementsByAttributeValueContaining("class", "storeselect select-style products");
//		System.out.println(e.get(0));
		
		Map<String, String> data = new HashMap<String, String>();
        data.put("products", "1");
        data.put("stores", "R428");
        Set keys = data.keySet();
	    Iterator keyIter = keys.iterator();
	    String content = "";
	    for(int i=0; keyIter.hasNext(); i++) {
	        Object key = keyIter.next();
	        if(i!=0) {
	            content += "&";
	        }
	        content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
	    }
	    System.out.println(content);

		
		//Elements inputElements = loginform.getElementsByTag("input");
//		List<String> paramList = new ArrayList<String>();
//		for (Element inputElement : inputElements) {
//			String key = inputElement.attr("name");
//			String value = inputElement.attr("value");
//	 
//			if (key.equals("Email"))
//				value = username;
//			else if (key.equals("Passwd"))
//				value = password;
//			paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
//		}
//	 
//		// build parameters LIST
//		StringBuilder result = new StringBuilder();
//		for (String param : paramList) {
//			if (result.length() == 0) {
//				result.append(param);
//			} else {
//				result.append("&" + param);
//			}
//		}
//		return result.toString();
		return content;
	  }

	private String GetPageContent(String url) throws Exception {
		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();
	 
		// default is GET
		conn.setRequestMethod("GET");
	 
		conn.setUseCaches(false);
		
		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		
		Map<String, String> data = new HashMap<String, String>();
		
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
	 
		BufferedReader in = 
	            new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
	 
		// Get the response cookies
		setCookies(conn.getHeaderFields().get("Set-Cookie"));
	 
		return response.toString();
	}
	
	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	  }

}
