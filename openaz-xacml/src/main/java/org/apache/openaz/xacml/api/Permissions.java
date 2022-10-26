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
 * Defines the API for objects that represent XACML Permissions
 * elements.  These associate actions with the obligations and
 * (optionally) policies that would be produced/evaluated if a query
 * was sent with that particular action
 */
public interface Permissions {
    /**
     * Get all the actions associated with these permissions
     */
    Collection<String> getActions();
    
    /**
     * Get the {@link org.apache.openaz.xacml.api.PoliciesAndObligations} associated with a particular
     * action.
     * @return the <code>PoliciesAndObligations</code> associated with <code>action</code>
     */
    PoliciesAndObligations getPoliciesAndObligations(String action);
    
    /**
     * Get the all the {@link org.apache.openaz.xacml.api.PoliciesAndObligations} objects
     * @return a map from actions (String) to <code>PoliciesAndObligations</code> objects
     */
    Map<String, PoliciesAndObligations> getPoliciesAndObligations();
}
