package org.azkfw.jersey;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.azkfw.jersey.test.AbstractJerseyTestCase;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Test;

public class SampleServiceTest extends AbstractJerseyTestCase {

	@Override
	protected Map<String, String> getInitParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("jersey.config.server.provider.packages", "org.azkfw.jersey;");
		params.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.media.multipart.MultiPartFeature");
		return params;
	}

	@Override
	protected List<Class<?>> getTestClasses() {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(SampleService.class);
		return classes;
	}

	@Test
	public void testEchoGet() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("msg", "メッセージ[GET]");

		Response res = getJson("/sample/echo", params);

		String json = res.readEntity(String.class);

		System.out.println("<<<" + json);
	}

	@Test
	public void testEchoPost() {
		
		Form form = new Form();
		form.param("msg","メッセージ[POST]");

		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
		//Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_JSON_TYPE);

		Response res = postJson("/sample/echo", entity);

		String json = res.readEntity(String.class);

		System.out.println("<<<" + json);
	}

	@Test
	public void testAA() {
		SampleService.RequestDto dto = new SampleService.RequestDto();
		dto.setToken("tokennnn");

		Entity<SampleService.RequestDto> e = Entity.entity(dto, MediaType.APPLICATION_JSON_TYPE);

		Response res = postJson("/sample/location", e);

		String json = res.readEntity(String.class);

		System.out.println("<<<" + json);

	}

	protected final Response getJson(final String aPath) {
		return getJson(aPath, null);
	}

	/**
	 * JSON形式でPOSTする。
	 * 
	 * @param aPath パス
	 * @param aJson パラメータ
	 * @return 結果
	 */
	protected final Response getJson(final String aPath, final Map<String, Object> aParams) {
		WebTarget target = target(aPath);
		if (null != aParams) {
			for (String key : aParams.keySet()) {
				Object value = aParams.get(key);
				target = target.queryParam(key, value);
			}
		}

		Response response = target.request().get();
		return response;
	}

	/**
	 * JSON形式でPOSTする。
	 * 
	 * @param aPath パス
	 * @param aJson パラメータ
	 * @return 結果
	 */
	protected final Response postJson(final String aPath, final Entity<?> aEntity) {
		Response response = target(aPath).request().post(aEntity);
		return response;
	}

	/**
	 * MultiPart形式でPOSTする。
	 * 
	 * @param aPath パス
	 * @param aParams パラメータ
	 * @return 結果
	 * @throws IOException
	 */
	protected final Response postMultiPart(final String aPath, final Map<String, Object> aParams) throws IOException {

		FormDataMultiPart multiPart = new FormDataMultiPart();
		if (null != aParams) {
			for (String key : aParams.keySet()) {
				Object obj = aParams.get(key);
				if (null == obj) {

				} else if (obj instanceof File) {
					File file = (File) obj;
					FileDataBodyPart fileDataBodyPart = new FileDataBodyPart(key, file);
					multiPart.bodyPart(fileDataBodyPart);
				} else {
					String string = obj.toString();
					FormDataBodyPart formDataBodyPart = new FormDataBodyPart(key, string);
					multiPart.bodyPart(formDataBodyPart);
				}
			}
		}
		Entity<FormDataMultiPart> entity = Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE);

		Response response = target(aPath).request().header("Content-type", "multipart/form-data").post(entity);

		return response;
	}
}
