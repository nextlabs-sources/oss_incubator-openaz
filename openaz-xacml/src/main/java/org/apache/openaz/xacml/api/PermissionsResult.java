/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

/*
 * Created on Jan 12, 2022
 *
 * All sources, binaries and HTML pages (C) copyright 2022 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 *
 * @author amorgan
 */

package org.apache.openaz.xacml.api;

import java.util.Collection;
import java.util.Map;

/**
 * Defines the API for objects that represent XACML PermissionsResult
 * elements. Results specify which actions would produce each possible
 * decision, along with the oblications and relevant policies if that
 * action were used in a query
 */

public interface PermissionsResult {
    /**
     * Get the {@link org.apache.openaz.xacml.api.Permissions} associated with a particular <code>Decision</code>
     * @return the <code>Permissions</code> associated with this <code>Decision</code>
     */
    Permissions getPermissions(Decision decision);

    /**
     * Get all the <code>Decision</code>s associated with this <code>PermissionsResult</code>
     */
    Collection<Decision> getDecisions();
    
    /**
     * Get all the {@link org.apache.openaz.xacml.api.Permissions} for every <code>Decision</code>
     * @return a map from <code>Decision</code> to <code>Permissions</code>
     */
    Map<Decision, Permissions> getPermissions();
}
