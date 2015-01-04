package org.azkfw.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
		return msg;
	}

	@POST
	@Path("echo")
	public String echoPost(final @FormParam("msg") String msg) {
		return msg;
	}

	public static class RequestDto {
		private String token;
		private Double latitude;
		private Double longitude;

		public void setToken(final String token) {
			this.token = token;
		}

		public String getToken() {
			return this.token;
		}

		public void setLocation(final double latitude, final double longitude) {
			this.latitude = Double.valueOf(latitude);
			this.longitude = Double.valueOf(longitude);
		}

		public Double getLatitude() {
			return this.latitude;
		}

		public Double getLongitude() {
			return this.longitude;
		}
	}

	public static class ResponseDto {

	}

	@POST
	@Path("location")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseDto test(final RequestDto request) {
		System.out.println(String.format("Token     : %s", request.getToken()));
		System.out.println(String.format("Latitude  : %s", (null != request.getLatitude()) ? request.getLatitude() : "null"));
		System.out.println(String.format("Longitude : %s", (null != request.getLongitude()) ? request.getLongitude() : "null"));

		ResponseDto response = new ResponseDto();
		return response;
	}
}
