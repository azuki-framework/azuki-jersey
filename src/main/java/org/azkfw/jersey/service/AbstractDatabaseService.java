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

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.azkfw.business.database.DatabaseConnection;
import org.azkfw.jersey.filter.BusinessSupportFilter;
import org.azkfw.jersey.filter.BusinessSupportFilter.BusinessContainer;

/**
 * このクラスは、データベース機能を実装したサービスクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/01/26
 * @author Kawakicchi
 */
public abstract class AbstractDatabaseService extends AbstractPersistenceService {

	@Context
	private HttpServletRequest request;

	/**
	 * コンストラクタ
	 */
	public AbstractDatabaseService() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name 名前
	 */
	public AbstractDatabaseService(final String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param clazz クラス
	 */
	public AbstractDatabaseService(final Class<?> clazz) {
		super(clazz);
	}

	/**
	 * コネクションを取得する。
	 * 
	 * @return コネクション
	 * @throws SQL実行時に問題が発生した場合
	 */
	protected final DatabaseConnection getConnection() throws SQLException {
		BusinessContainer container = BusinessSupportFilter.getContainer(request);
		return container.getConnection();
	}

	/**
	 * コミット処理を行う。
	 * 
	 * @throws SQLException SQL実行中に問題が発生した場合
	 */
	protected final void commit() throws SQLException {
		BusinessContainer container = BusinessSupportFilter.getContainer(request);
		container.commit();
	}

	/**
	 * ロールバック処理を行う。
	 * 
	 * @throws SQLException SQL実行中に問題が発生した場合
	 */
	protected final void rollback() throws SQLException {
		BusinessContainer container = BusinessSupportFilter.getContainer(request);
		container.rollback();
	}
}
