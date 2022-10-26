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
 * Created on Jan 13, 2022
 *
 * All sources, binaries and HTML pages (C) copyright 2022 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 *
 * @author amorgan
 */

package org.apache.openaz.xacml.std;

import java.util.Collection;
import java.util.Map;

import org.apache.openaz.xacml.api.Permissions;
import org.apache.openaz.xacml.api.PoliciesAndObligations;
import org.apache.openaz.xacml.util.Wrapper;

public class StdPermissions extends Wrapper<Permissions> implements Permissions {
    /**
     * Creates an immutable <code>StdPemissions</code> by wrapping
     * another {@link org.apache.openaz.xacml.api.Permissions}. By
     * creating this wrapper, the caller is stating they will not
     * modify the wrapped <code>Permissions</code> any further.
     *
     * @param permissions the <code>Permissions</code> object to wrap
     */
    public StdPermissions(Permissions permissions) {
        super(permissions);
    }

    public StdPermissions(Map<String, PoliciesAndObligations> policiesAndObligationsMap) {
        this(new StdMutablePermissions(policiesAndObligationsMap));
    }
        
    @Override
    public Collection<String> getActions() {
        return this.getWrappedObject().getActions();
    }
    
    @Override
    public PoliciesAndObligations getPoliciesAndObligations(String action) {
        return this.getWrappedObject().getPoliciesAndObligations(action);
    }

    @Override
    public Map<String, PoliciesAndObligations> getPoliciesAndObligations() {
        return this.getWrappedObject().getPoliciesAndObligations();
    }
}
