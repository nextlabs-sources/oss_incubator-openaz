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

import org.apache.openaz.xacml.api.Decision;
import org.apache.openaz.xacml.api.Permissions;
import org.apache.openaz.xacml.api.PermissionsResult;
import org.apache.openaz.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link org.apache.openaz.xacml.pai.PermissionsResult} interface
 */
public class StdPermissionsResult extends Wrapper<PermissionsResult> implements PermissionsResult {
    /**
     * Creates an immutable <code>StdPermissionsResult</code> by
     * wrapping another {@link * org.apache.openaz.xacml.api.PermissionsResult}.
     *
     * By creating this wrapper, the caller is stating that they will
     * not modify the wrapped <code>PermissionsResult</code> any
     * further.
     */ 
    public StdPermissionsResult(PermissionsResult result) {
        super(result);
    }
    
    /**
     * Creates an immutable <code>StdPermissionsResult</code> with the given mapping from {@link org.apache.openaz.xacml.api.Decision}
     *
     * @param permissionsMap maps different <code>Decision</code>s to <code>Permissions</code> object
     */
    public StdPermissionsResult(Map<Decision, Permissions> permissionsMap) {
        this (new StdMutablePermissionsResult(permissionsMap));
    }

    @Override
    public Collection<Decision> getDecisions() {
        return this.getWrappedObject().getDecisions();
    }
    
    @Override
    public Permissions getPermissions(Decision decision) {
        return this.getWrappedObject().getPermissions(decision);
    }
    
    @Override
    public Map<Decision, Permissions> getPermissions() {
        return this.getWrappedObject().getPermissions();
    }
}
