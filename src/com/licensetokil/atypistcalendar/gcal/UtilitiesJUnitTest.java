package com.licensetokil.atypistcalendar.gcal;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UtilitiesJUnitTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetNoArgument() {
		try {
			String reply = Util.sendJsonHttpsRequest(
					"https://httpbin.org/get",
					Util.REQUEST_METHOD_GET,
					null,
					null);
			//System.out.println(reply);
			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);
			
			assertEquals("\"http://httpbin.org/get\"", jsonReply.get("url").toString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testPostNoArgument() {
		try {
			String reply = Util.sendJsonHttpsRequest(
					"https://httpbin.org/post",
					Util.REQUEST_METHOD_POST,
					null,
					null);
			//System.out.println(reply);
			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);
			
			assertEquals("\"http://httpbin.org/post\"", jsonReply.get("url").toString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteNoArgument() {
		try {
			String reply = Util.sendJsonHttpsRequest(
					"https://httpbin.org/delete",
					Util.REQUEST_METHOD_DELETE,
					null,
					null);
			//System.out.println(reply);
			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);
			
			assertEquals("\"http://httpbin.org/delete\"", jsonReply.get("url").toString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testPutNoArgument() {
		try {
			String reply = Util.sendJsonHttpsRequest(
					"https://httpbin.org/put",
					Util.REQUEST_METHOD_PUT,
					null,
					null);
			//System.out.println(reply);
			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);
			
			assertEquals("\"http://httpbin.org/put\"", jsonReply.get("url").toString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testPostFormUrlencoded() {
		try {
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("argone", "helloworld");
			
			String reply = Util.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/post",
					Util.REQUEST_METHOD_POST,
					null,
					parameters);
			System.out.println(reply);
			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);
			
			assertEquals("\"http://httpbin.org/post\"", jsonReply.get("url").toString());
			assertEquals("\"helloworld\"", jsonReply.get("form").getAsJsonObject().get("argone").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
