package com.licensetokil.atypistcalendar.gcal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UtilitiesJUnitTest {

	@Test
	public void	test_json_get_noArguments_noAdditionalHeaders() {
		try {
			String reply = Utilities.sendJsonHttpsRequest(
					"https://httpbin.org/get",
					Utilities.REQUEST_METHOD_GET,
					Utilities.EMPTY_ADDITIONAL_HEADERS,
					Utilities.EMPTY_REQUEST_BODY);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/get", jsonReply.get("url").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_json_get_noArguments_withAdditionalHeaders() {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Custom-Header-One", "tree");
			headers.put("Custom-Header-Two", "rubber");

			String reply = Utilities.sendJsonHttpsRequest(
					"https://httpbin.org/get",
					Utilities.REQUEST_METHOD_GET,
					headers,
					Utilities.EMPTY_REQUEST_BODY);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/get", jsonReply.get("url").getAsString());
			assertEquals("tree", jsonReply.getAsJsonObject("headers").get("Custom-Header-One").getAsString());
			assertEquals("rubber", jsonReply.getAsJsonObject("headers").get("Custom-Header-Two").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_json_post_noArguments_noAdditionalHeaders() {
		try {
			String reply = Utilities.sendJsonHttpsRequest(
					"https://httpbin.org/post",
					Utilities.REQUEST_METHOD_POST,
					Utilities.EMPTY_ADDITIONAL_HEADERS,
					Utilities.EMPTY_REQUEST_BODY);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/post", jsonReply.get("url").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_json_post_noArguments_withAdditionalHeaders() {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Custom-Header-One", "tree");
			headers.put("Custom-Header-Two", "rubber");

			String reply = Utilities.sendJsonHttpsRequest(
					"https://httpbin.org/post",
					Utilities.REQUEST_METHOD_POST,
					headers,
					Utilities.EMPTY_REQUEST_BODY);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/post", jsonReply.get("url").getAsString());
			assertEquals("tree", jsonReply.getAsJsonObject("headers").get("Custom-Header-One").getAsString());
			assertEquals("rubber", jsonReply.getAsJsonObject("headers").get("Custom-Header-Two").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_json_delete_noArguments_noAdditionalHeaders() {
		try {
			String reply = Utilities.sendJsonHttpsRequest(
					"https://httpbin.org/delete",
					Utilities.REQUEST_METHOD_DELETE,
					Utilities.EMPTY_ADDITIONAL_HEADERS,
					Utilities.EMPTY_REQUEST_BODY);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/delete", jsonReply.get("url").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_json_delete_noArguments_withAdditionalHeaders() {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Custom-Header-One", "tree");
			headers.put("Custom-Header-Two", "rubber");
			String reply = Utilities.sendJsonHttpsRequest(
					"https://httpbin.org/delete",
					Utilities.REQUEST_METHOD_DELETE,
					headers,
					Utilities.EMPTY_REQUEST_BODY);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/delete", jsonReply.get("url").getAsString());
			assertEquals("tree", jsonReply.getAsJsonObject("headers").get("Custom-Header-One").getAsString());
			assertEquals("rubber", jsonReply.getAsJsonObject("headers").get("Custom-Header-Two").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_json_put_noArguments_noAdditionalHeaders() {
		try {
			String reply = Utilities.sendJsonHttpsRequest(
					"https://httpbin.org/put",
					Utilities.REQUEST_METHOD_PUT,
					Utilities.EMPTY_ADDITIONAL_HEADERS,
					Utilities.EMPTY_REQUEST_BODY);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/put", jsonReply.get("url").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_json_put_noArguments_withAdditionalHeaders() {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Custom-Header-One", "tree");
			headers.put("Custom-Header-Two", "rubber");

			String reply = Utilities.sendJsonHttpsRequest(
					"https://httpbin.org/put",
					Utilities.REQUEST_METHOD_PUT,
					headers,
					Utilities.EMPTY_REQUEST_BODY);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/put", jsonReply.get("url").getAsString());
			assertEquals("tree", jsonReply.getAsJsonObject("headers").get("Custom-Header-One").getAsString());
			assertEquals("rubber", jsonReply.getAsJsonObject("headers").get("Custom-Header-Two").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_formUrlencoded_post_noArguments_noAdditionalHeaders() {
		try {
			String reply = Utilities.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/post",
					Utilities.REQUEST_METHOD_POST,
					Utilities.EMPTY_ADDITIONAL_HEADERS,
					Utilities.EMPTY_FORM_PARAMETERS);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/post", jsonReply.get("url").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_formUrlencoded_post_noArguments_withAdditionalHeaders() {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Custom-Header-One", "tree");
			headers.put("Custom-Header-Two", "rubber");

			String reply = Utilities.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/post",
					Utilities.REQUEST_METHOD_POST,
					headers,
					Utilities.EMPTY_FORM_PARAMETERS);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/post", jsonReply.get("url").getAsString());
			assertEquals("tree", jsonReply.getAsJsonObject("headers").get("Custom-Header-One").getAsString());
			assertEquals("rubber", jsonReply.getAsJsonObject("headers").get("Custom-Header-Two").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_formUrlencoded_post_withArguments_noAdditionalHeaders() {
		try {
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("argone", "helloworld");
			parameters.put("argtwo", "cs2103t");

			String reply = Utilities.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/post",
					Utilities.REQUEST_METHOD_POST,
					Utilities.EMPTY_ADDITIONAL_HEADERS,
					parameters);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/post", jsonReply.get("url").getAsString());
			assertEquals("helloworld", jsonReply.get("form").getAsJsonObject().get("argone").getAsString());
			assertEquals("cs2103t", jsonReply.get("form").getAsJsonObject().get("argtwo").getAsString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_formUrlencoded_post_withArguments_withAdditionalHeaders() {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Custom-Header-One", "tree");
			headers.put("Custom-Header-Two", "rubber");

			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("argone", "helloworld");
			parameters.put("argtwo", "cs2103t");


			String reply = Utilities.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/post",
					Utilities.REQUEST_METHOD_POST,
					headers,
					parameters);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/post", jsonReply.get("url").getAsString());
			assertEquals("tree", jsonReply.getAsJsonObject("headers").get("Custom-Header-One").getAsString());
			assertEquals("rubber", jsonReply.getAsJsonObject("headers").get("Custom-Header-Two").getAsString());
			assertEquals("helloworld", jsonReply.get("form").getAsJsonObject().get("argone").getAsString());
			assertEquals("cs2103t", jsonReply.get("form").getAsJsonObject().get("argtwo").getAsString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_formUrlencoded_get_noArguments_noAdditionalHeaders() {
		try {
			String reply = Utilities.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/get",
					Utilities.REQUEST_METHOD_GET,
					Utilities.EMPTY_ADDITIONAL_HEADERS,
					Utilities.EMPTY_FORM_PARAMETERS);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/get", jsonReply.get("url").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_formUrlencoded_get_noArguments_withAdditionalHeaders() {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Custom-Header-One", "tree");
			headers.put("Custom-Header-Two", "rubber");

			String reply = Utilities.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/get",
					Utilities.REQUEST_METHOD_GET,
					headers,
					Utilities.EMPTY_FORM_PARAMETERS);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/get", jsonReply.get("url").getAsString());
			assertEquals("tree", jsonReply.getAsJsonObject("headers").get("Custom-Header-One").getAsString());
			assertEquals("rubber", jsonReply.getAsJsonObject("headers").get("Custom-Header-Two").getAsString());
			assertEquals("{}", jsonReply.get("args").toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_formUrlencoded_get_withArguments_noAdditionalHeaders() {
		try {
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("argone", "helloworld");
			parameters.put("argtwo", "cs2103t");

			String reply = Utilities.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/get",
					Utilities.REQUEST_METHOD_GET,
					Utilities.EMPTY_ADDITIONAL_HEADERS,
					parameters);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/get", jsonReply.get("url").getAsString());
			assertEquals("helloworld", jsonReply.get("form").getAsJsonObject().get("argone").getAsString());
			assertEquals("cs2103t", jsonReply.get("form").getAsJsonObject().get("argtwo").getAsString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_formUrlencoded_get_withArguments_withAdditionalHeaders() {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Custom-Header-One", "tree");
			headers.put("Custom-Header-Two", "rubber");

			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("argone", "helloworld");
			parameters.put("argtwo", "cs2103t");


			String reply = Utilities.sendUrlencodedFormHttpsRequest(
					"https://httpbin.org/get",
					Utilities.REQUEST_METHOD_GET,
					headers,
					parameters);

			JsonObject jsonReply = (JsonObject)new JsonParser().parse(reply);

			assertEquals("http://httpbin.org/get", jsonReply.get("url").getAsString());
			assertEquals("tree", jsonReply.getAsJsonObject("headers").get("Custom-Header-One").getAsString());
			assertEquals("rubber", jsonReply.getAsJsonObject("headers").get("Custom-Header-Two").getAsString());
			assertEquals("helloworld", jsonReply.get("form").getAsJsonObject().get("argone").getAsString());
			assertEquals("cs2103t", jsonReply.get("form").getAsJsonObject().get("argtwo").getAsString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
