package org.azkfw.jersey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.azkfw.jersey.test.AbstractJerseyTestCase;
import org.junit.Test;

public class SampleAPITest extends AbstractJerseyTestCase {

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
		classes.add(SampleAPI.class);
		return classes;
	}

	@Test
	public void test() {

		Response res = getJson("/sample/echo");

		String json = res.readEntity(String.class);

		System.out.println(json);
	}

	/**
	 * JSON形式でPOSTする。
	 * 
	 * @param aPath パス
	 * @param aJson パラメータ
	 * @return 結果
	 */
	private final Response getJson(final String aPath) {
		Response response = target(aPath).request().get();

		return response;
	}

	/**
	 * JSON形式でPOSTする。
	 * 
	 * @param aPath パス
	 * @param aJson パラメータ
	 * @return 結果
	 */
	private final Response postJson(final String aPath, final String aJson) {
		Entity<String> entity = Entity.json(aJson);
		Response response = target(aPath).request().post(entity);

		return response;
	}
}
