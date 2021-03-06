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
package org.azkfw.jersey.filter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.azkfw.business.BusinessServiceException;
import org.azkfw.business.logic.Logic;
import org.azkfw.jersey.filter.BusinessSupportFilter.BusinessContainer;
import org.azkfw.jersey.service.AbstractDatabaseService;
import org.azkfw.util.StringUtility;

/**
 * このクラスは、ビジネス機能を実装したフィルタクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/02/02
 * @author Kawakicchi
 */
public abstract class AbstractBusinessFilter extends AbstractDatabaseService {

	@Context
	private HttpServletRequest request;

	/**
	 * コンストラクタ
	 */
	public AbstractBusinessFilter() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name Name
	 */
	public AbstractBusinessFilter(final String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param clazz Class
	 */
	public AbstractBusinessFilter(final Class<?> clazz) {
		super(clazz);
	}

	/**
	 * ロジックを取得する。
	 * 
	 * @param name ロジック名
	 * @return ロジック
	 * @throws BusinessServiceException ビジネスサービス層に起因する問題が発生した場合
	 */
	protected final Logic getLogic(final String name) throws BusinessServiceException {
		return getLogic(StringUtility.EMPTY, name);
	}

	/**
	 * ロジックを取得する。
	 * 
	 * @param ns 名前空間
	 * @param name ロジック名
	 * @return ロジック
	 * @throws BusinessServiceException ビジネスサービス層に起因する問題が発生した場合
	 */
	protected final Logic getLogic(final String ns, final String name) throws BusinessServiceException {
		Logic logic = null;

		BusinessContainer container = BusinessSupportFilter.getContainer(request);
		logic = container.getLogic(ns, name);

		return logic;
	}

}