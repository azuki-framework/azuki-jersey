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

		Response res = get("/sample/echo", params);

		String json = res.readEntity(String.class);

		System.out.println("<<<" + json);
	}

	@Test
	public void testEchoPost() {

		Form form = new Form();
		form.param("msg", "メッセージ[POST]");

		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
		//Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_JSON_TYPE);

		Response res = post("/sample/echo", entity);

		String json = res.readEntity(String.class);

		System.out.println("<<<" + json);
	}

	@Test
	public void testAA() {
		SampleService.RequestDto dto = new SampleService.RequestDto();
		dto.setToken("tokennnn");

		Entity<SampleService.RequestDto> e = Entity.entity(dto, MediaType.APPLICATION_JSON_TYPE);

		Response res = post("/sample/location", e);

		String json = res.readEntity(String.class);

		System.out.println("<<<" + json);
	}
}
