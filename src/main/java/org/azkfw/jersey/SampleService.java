package org.azkfw.jersey;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("sample")
public class SampleService extends AbstractService {

	/**
	 * コンストラクタ
	 */
	public SampleService() {
		super(SampleService.class);
	}

	@GET
	@Path("echo")
	public String echoGet(final @QueryParam("msg") String msg) {
		System.out.println(">>>"+msg);
		return msg;
	}
	
	@POST
	@Path("echo")
	public String echoPost(final @FormParam("msg") String msg) {
		System.out.println(">>>"+msg);
		return msg;
	}

}
