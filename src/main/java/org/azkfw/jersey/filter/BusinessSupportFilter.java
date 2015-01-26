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
import org.azkfw.business.logic.Logic;
import org.azkfw.business.logic.LogicManager;
import org.azkfw.business.property.Property;
import org.azkfw.business.property.PropertyManager;
import org.azkfw.business.property.PropertySupport;
import org.azkfw.context.ContextSupport;
import org.azkfw.persistence.database.DatabaseConnection;
import org.azkfw.persistence.database.DatabaseConnectionManager;
import org.azkfw.persistence.database.DatabaseConnectionSupport;
import org.azkfw.persistence.database.DatabaseSource;
import org.azkfw.plugin.PluginManager;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/01/26
 * @author Kawakicchi
 */
@Priority(0)
public class BusinessSupportFilter implements ContainerRequestFilter, ContainerResponseFilter {

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

	public static class BusinessContainer {

		/**
		 * データベースソース
		 */
		private DatabaseSource source;

		/**
		 * コネクション
		 */
		private DatabaseConnection myConnection;

		/**
		 * Logics
		 */
		private Map<String, Map<String, Logic>> logics;

		private BusinessContainer() {
			logics = new HashMap<String, Map<String, Logic>>();

		}

		private void initialize() {

		}

		private void release() {
			for (String namespace : logics.keySet()) {
				Map<String, Logic> ls = logics.get(namespace);
				for (String name : ls.keySet()) {
					ls.get(name).destroy();
				}
			}
			logics.clear();

			try {
				if (null != myConnection) {
					myConnection.getConnection().commit();
				}
			} catch (SQLException ex) {
				// fatal(ex);
			}
			if (null != source && null != myConnection) {
				try {
					source.returnConnection(myConnection);
				} catch (SQLException ex) {
					// warn(ex);
				}
				myConnection = null;
				source = null;
			}
		}

		public Logic getLogic(final String aNamespace, final String aName) throws BusinessServiceException {
			Logic logic = null;

			try {
				Map<String, Logic> ls = null;
				if (logics.containsKey(aNamespace)) {
					ls = logics.get(aNamespace);
				} else {
					ls = new HashMap<String, Logic>();
					logics.put(aNamespace, ls);
				}

				if (ls.containsKey(aName)) {
					logic = ls.get(aName);
				} else {
					logic = LogicManager.create(aNamespace, aName);
					if (null != logic) {
						doLogicSupport(logic);
						logic.initialize();
					}
					ls.put(aName, logic);
				}
			} catch (SQLException ex) {
				throw new BusinessServiceException(ex);
			}
			return logic;
		}

		/**
		 * コネクションを取得する。
		 * 
		 * @return コネクション
		 * @throws SQL実行時に問題が発生した場合
		 */
		public final DatabaseConnection getConnection() throws SQLException {
			if (null == myConnection) {
				source = DatabaseConnectionManager.getSource();
				myConnection = source.getConnection();
				if (null != myConnection) {
					myConnection.getConnection().setAutoCommit(false);
				}
			}
			return myConnection;
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

		/**
		 * ロジックにサポートを行う。
		 * <p>
		 * ロジックに新機能を追加したい場合、このメソッドをオーバーライドしスーパークラスの同メソッドを呼び出した後に追加機能をサポートすること。
		 * </p>
		 * 
		 * @param aLogic ロジック
		 * @throws SQLException SQL操作時に問題が発生した場合
		 */
		private void doLogicSupport(final Logic aLogic) throws SQLException {
			if (aLogic instanceof ContextSupport) {
				((ContextSupport) aLogic).setContext(getContext());
			}
			if (aLogic instanceof PropertySupport) {
				Property property = PropertyManager.get(aLogic.getClass());
				if (null == property) {
					property = PropertyManager.load(aLogic.getClass(), getContext());
				}
				((PropertySupport) aLogic).setProperty(property);
			}
			if (aLogic instanceof DatabaseConnectionSupport) {
				((DatabaseConnectionSupport) aLogic).setConnection(getConnection());
			}
		}

		/**
		 * コンテキストを取得する。
		 * 
		 * @return コンテキスト
		 */
		private final org.azkfw.context.Context getContext() {
			// TODO: コンテキストの引渡し方法を改善
			return PluginManager.getContext();
		}
	}
}
