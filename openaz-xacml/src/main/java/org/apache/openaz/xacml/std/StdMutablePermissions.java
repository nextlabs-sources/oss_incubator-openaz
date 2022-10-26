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
 * Created on Jan 14, 2022
 *
 * All sources, binaries and HTML pages (C) copyright 2022 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 *
 * @author amorgan
 */

package org.apache.openaz.xacml.std;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.openaz.xacml.api.Permissions;
import org.apache.openaz.xacml.api.PoliciesAndObligations;

public class StdMutablePermissions implements Permissions {
    private static final Map<String, PoliciesAndObligations> EMPTY_POLICIES_AND_OBLIGATIONS = Collections.unmodifiableMap(new HashMap<String, PoliciesAndObligations>());

    private Map<String, PoliciesAndObligations> policiesAndObligationsMap;

    /**
     * Creates a new <code>StdMutablePermissions</code> with no data
     */
    public StdMutablePermissions() {
        policiesAndObligationsMap = EMPTY_POLICIES_AND_OBLIGATIONS;
    }

    public StdMutablePermissions(Map<String, PoliciesAndObligations> policiesAndObligationsMap) {
        if (policiesAndObligationsMap != null && !policiesAndObligationsMap.isEmpty()) {
            this.policiesAndObligationsMap = new HashMap<String, PoliciesAndObligations>(policiesAndObligationsMap);
        } else {
            this.policiesAndObligationsMap = EMPTY_POLICIES_AND_OBLIGATIONS;
        }
    }
    
    public void setPoliciesAndObligations(String action, PoliciesAndObligations policiesAndObligations) {
        if (policiesAndObligationsMap == EMPTY_POLICIES_AND_OBLIGATIONS) {
            policiesAndObligationsMap = new HashMap<String, PoliciesAndObligations>();
        }

        policiesAndObligationsMap.put(action, policiesAndObligations);
    }

    @Override
    public Collection<String> getActions() {
        return policiesAndObligationsMap.keySet();
    }
    
    @Override
    public PoliciesAndObligations getPoliciesAndObligations(String action) {
        return policiesAndObligationsMap.get(action);
    }

    public void setPoliciesAndObligations(Map<String, PoliciesAndObligations> policiesAndObligationsMap) {
        this.policiesAndObligationsMap = policiesAndObligationsMap;
    }
    
    @Override
    public Map<String, PoliciesAndObligations> getPoliciesAndObligations() {
        return policiesAndObligationsMap == EMPTY_POLICIES_AND_OBLIGATIONS ? policiesAndObligationsMap : Collections.unmodifiableMap(policiesAndObligationsMap);
    }
}
