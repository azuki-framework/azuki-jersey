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

import org.azkfw.lang.LoggingObject;

/**
 * このクラスは、フィルタ機能を実装する為の基底クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2015/02/02
 * @author Kawakicchi
 */
public class AbstractFilter extends LoggingObject {

	/**
	 * コンストラクタ
	 */
	public AbstractFilter() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name 名前
	 */
	public AbstractFilter(final String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param clazz クラス
	 */
	public AbstractFilter(final Class<?> clazz) {
		super(clazz);
	}
}
