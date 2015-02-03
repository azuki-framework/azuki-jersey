/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.ws.rs;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/02/02
 * @author Kawakicchi
 */
public final class Priorities {

	private Priorities() {
		// prevents construction
	}

	/**
	 * Security authentication filter/interceptor priority. 1000
	 */
	public static final int AUTHENTICATION = javax.ws.rs.Priorities.AUTHENTICATION;
	/**
	 * Security authorization filter/interceptor priority. 2000
	 */
	public static final int AUTHORIZATION = javax.ws.rs.Priorities.AUTHORIZATION;
	/**
	 * Header decorator filter/interceptor priority. 3000
	 */
	public static final int HEADER_DECORATOR = javax.ws.rs.Priorities.HEADER_DECORATOR;
	/**
	 * Message encoder or decoder filter/interceptor priority. 4000
	 */
	public static final int ENTITY_CODER = javax.ws.rs.Priorities.ENTITY_CODER;
	/**
	 * User-level filter/interceptor priority. 5000
	 */
	public static final int USER = javax.ws.rs.Priorities.USER;
}
