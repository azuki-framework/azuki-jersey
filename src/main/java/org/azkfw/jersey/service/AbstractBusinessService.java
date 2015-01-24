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

/**
 * このクラスは、ビジネス機能を実装したｻｰﾋﾞｽクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/01/23
 * @author kawakicchi
 */
public abstract class AbstractBusinessService extends AbstractService {

	/**
	 * コンストラクタ
	 * 
	 * @param name 名前
	 */
	public AbstractBusinessService(final String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param clazz クラス
	 */
	public AbstractBusinessService(final Class<?> clazz) {
		super(clazz);
	}

	/**
	 * ロジックを取得する。
	 * 
	 * @param clazz ロジッククラス
	 * @return ロジック
	 */
	protected final <T> T getLogic(final Class<T> clazz) {
		// TODO: 
		//@SuppressWarnings("unchecked")
		//T logic = (T) new XXXX();
		return null;
	}
}