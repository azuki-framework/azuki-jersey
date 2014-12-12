package org.azkfw.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("sample")
public class SampleAPI {

	@GET
	@Path("echo")
	public String echo() {
		return "test";
	}
}
