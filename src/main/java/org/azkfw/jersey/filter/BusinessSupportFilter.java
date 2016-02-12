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

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;

import org.azkfw.business.BusinessServiceException;
import org.azkfw.business.database.DatabaseConnection;
import org.azkfw.business.database.DatabaseConnectionSupport;
import org.azkfw.business.logic.Logic;
import org.azkfw.business.logic.LogicManager;
import org.azkfw.database.DatabaseManager;
import org.azkfw.log.LoggingObject;
import org.azkfw.plugin.PluginManager;
import org.azkfw.plugin.PluginServiceException;
import org.azkfw.util.StringUtility;
import org.azkfw.ws.rs.Priorities;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/01/26
 * @author Kawakicchi
 */
@Priority(Priorities.AUTHENTICATION - 100)
public class BusinessSupportFilter extends LoggingObject implements ContainerRequestFilter, ContainerResponseFilter {

	private static final String ATTRIBUTE_NAME = "__BUSINESS_SUPPORT_FILTER_CONTAINER_ATTRIBUTE";

	public static BusinessContainer getContainer(final HttpServletRequest request) {
		BusinessContainer container = (BusinessContainer) request.getAttribute(ATTRIBUTE_NAME);
		return container;
	}

	@Context
	private HttpServletRequest request;

	@Override
	public void filter(final ContainerRequestContext req) throws IOException {
		BusinessContainer container = new BusinessContainer();
		container.initialize();
		request.setAttribute(ATTRIBUTE_NAME, container);
	}

	@Override
	public void filter(final ContainerRequestContext req, final ContainerResponseContext res) throws IOException {
		BusinessContainer container = getContainer(request);
		container.release();
	}

	public static class BusinessContainer extends LoggingObject{

		/** Database */
		private DatabaseConnection connection;

		/** Logics */
		private Map<String, Map<String, Logic>> logics;

		private BusinessContainer() {
			logics = new HashMap<String, Map<String, Logic>>();
		}

		/**
		 * ロジックを取得する。
		 * 
		 * @param name ロジック名
		 * @return ロジック
		 * @throws BusinessServiceException ビジネスサービス層に起因する問題が発生した場合
		 */
		public final Logic getLogic(final String name) throws BusinessServiceException {
			return getLogic(StringUtility.EMPTY, name);
		}
		
		/**
		 * ロジックを取得する。
		 * 
		 * @param namespace 名前空間
		 * @param name ロジック名
		 * @return ロジック
		 * @throws BusinessServiceException ビジネスサービス層に起因する問題が発生した場合
		 */
		public final Logic getLogic(final String namespace, final String name) throws BusinessServiceException {
			Logic logic = null;
			try {
				Map<String, Logic> logics = null;
				if (this.logics.containsKey(namespace)) {
					logics = this.logics.get(namespace);
				} else {
					logics = new HashMap<String, Logic>();
					this.logics.put(namespace, logics);
				}
				
				if (logics.containsKey(name)) {
					logic = logics.get(name);
				} else {
					logic = LogicManager.create(name);
					if (null != logic) {
						if (logic instanceof DatabaseConnectionSupport) {
							DatabaseConnectionSupport support = (DatabaseConnectionSupport) logic;
							support.setConnection(getConnection());
						}
						PluginManager.getInstance().support(logic);
						logic.initialize();
					}
					logics.put(name, logic);
				}
			} catch (SQLException ex) {
				throw new BusinessServiceException(ex);
			} catch (PluginServiceException ex) {
				throw new BusinessServiceException(ex);
			}
			return logic;
		}
		
		private void initialize() {

			logics.clear();
		}
		
		private void release() {
			for (Map<String, Logic> logics : this.logics.values()) {
				for (Logic logic : logics.values()) {
					logic.destroy();
				}
			}
			logics.clear();

			if (null != connection) {
				try {
					connection.getConnection().commit();
				} catch (SQLException ex) {
					fatal(ex);
				}
				try {
					connection.getConnection().close();
				} catch (SQLException ex) {
					fatal(ex);
				}
				connection = null;
			}
		}

		/**
		 * コネクションを取得する。
		 * 
		 * @return コネクション
		 * @throws SQLException SQL操作に起因する問題が発生した場合
		 */
		public final DatabaseConnection getConnection() throws SQLException {
			try {
				if (null == connection) {
					connection = new DatabaseConnection(DatabaseManager.getInstance().getConnection());
					connection.getConnection().setAutoCommit(false);
				}
			} catch (SQLException ex) {
				throw ex;
			}
			return connection;
		}

		/**
		 * コミット処理を行う。
		 * 
		 * @throws SQLException SQL実行中に問題が発生した場合
		 */
		public final void commit() throws SQLException {
			DatabaseConnection connection = getConnection();
			if (null != connection) {
				connection.getConnection().commit();
			}
		}

		/**
		 * ロールバック処理を行う。
		 * 
		 * @throws SQLException SQL実行中に問題が発生した場合
		 */
		public final void rollback() throws SQLException {
			DatabaseConnection connection = getConnection();
			if (null != connection) {
				connection.getConnection().rollback();
			}
		}
	}
}
