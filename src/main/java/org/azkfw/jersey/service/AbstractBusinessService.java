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
package org.azkfw.jersey.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.azkfw.business.BusinessServiceException;
import org.azkfw.business.logic.Logic;
import org.azkfw.jersey.filter.BusinessSupportFilter;
import org.azkfw.jersey.filter.BusinessSupportFilter.BusinessContainer;
import org.azkfw.util.StringUtility;

/**
 * このクラスは、ビジネス機能を実装したｻｰﾋﾞｽクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/01/23
 * @author kawakicchi
 */
public abstract class AbstractBusinessService extends AbstractDatabaseService {

	@Context
	private HttpServletRequest request;

	/**
	 * コンストラクタ
	 */
	public AbstractBusinessService() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aName Name
	 */
	public AbstractBusinessService(final String aName) {
		super(aName);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aClass Class
	 */
	public AbstractBusinessService(final Class<?> aClass) {
		super(aClass);
	}

	/**
	 * ロジックを取得する。
	 * 
	 * @param aName ロジック名
	 * @return ロジック
	 * @throws BusinessServiceException ビジネスサービス層に起因する問題が発生した場合
	 */
	protected final Logic getLogic(final String aName) throws BusinessServiceException {
		return getLogic(StringUtility.EMPTY, aName);
	}

	/**
	 * ロジックを取得する。
	 * 
	 * @param aNamespace 名前空間
	 * @param aName ロジック名
	 * @return ロジック
	 * @throws BusinessServiceException ビジネスサービス層に起因する問題が発生した場合
	 */
	protected final Logic getLogic(final String aNamespace, final String aName) throws BusinessServiceException {
		Logic logic = null;

		BusinessContainer container = BusinessSupportFilter.getContainer(request);
		logic = container.getLogic(aNamespace, aName);

		return logic;
	}

}
