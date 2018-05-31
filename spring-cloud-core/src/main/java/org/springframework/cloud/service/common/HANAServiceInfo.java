/*
 *  Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo;

/**
 * @author Johannes Roesch
 */
@ServiceInfo.ServiceLabel("hana") public class HANAServiceInfo
		extends RelationalServiceInfo {
	public static final String HANA_SCHEME = "sap";

	public HANAServiceInfo(String id, String url, String jdbcUrl) {
		super(id, url, jdbcUrl, HANA_SCHEME);
	}
}
