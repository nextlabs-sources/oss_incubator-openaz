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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.openaz.xacml.api.Decision;
import org.apache.openaz.xacml.api.Permissions;
import org.apache.openaz.xacml.api.PermissionsResult;
import org.apache.openaz.xacml.util.ListUtil;
import org.apache.openaz.xacml.util.ObjUtil;

public class StdMutablePermissionsResult implements PermissionsResult {
    private static final Map<Decision, Permissions> EMPTY_PERMISSIONS_MAP = Collections.unmodifiableMap(new HashMap<Decision, Permissions>());

    private Map<Decision, Permissions> permissionsByDecision = EMPTY_PERMISSIONS_MAP;

    public StdMutablePermissionsResult() {
        permissionsByDecision = EMPTY_PERMISSIONS_MAP;
    }

    public StdMutablePermissionsResult(Map<Decision, Permissions> permissionsByDecision) {
        this.permissionsByDecision = permissionsByDecision;
    }

    public StdMutablePermissionsResult(PermissionsResult result) {
        setPermissions(result.getPermissions());
    }
    
    public void setPermissionsResult(Decision decision, Permissions permissions) {
        if (decision == null) {
            return;
        }
        
        if (permissionsByDecision == EMPTY_PERMISSIONS_MAP) {
            permissionsByDecision = new HashMap<Decision, Permissions>();
        }

        permissionsByDecision.put(decision, permissions);
    }

    @Override
    public Collection<Decision> getDecisions() {
        return permissionsByDecision.keySet();
    }
    
    @Override
    public Permissions getPermissions(Decision decision) {
        // Return empty Permissions object?
        return permissionsByDecision.get(decision);
    }

    public void setPermissions(Map<Decision, Permissions> permissionsByDecision) {
        if (permissionsByDecision == EMPTY_PERMISSIONS_MAP) {
            permissionsByDecision = new HashMap<Decision, Permissions>();
        }
        
        this.permissionsByDecision.putAll(permissionsByDecision);
    }
    
    @Override
    public Map<Decision, Permissions> getPermissions() {
        return permissionsByDecision == EMPTY_PERMISSIONS_MAP ? permissionsByDecision : Collections.unmodifiableMap(permissionsByDecision);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !(obj instanceof PermissionsResult)) {
            return false;
        } else {
            PermissionsResult result = (PermissionsResult)obj;
            if (ListUtil.equalsAllowNulls(this.getPermissions().keySet(), result.getPermissions().keySet())) {
                for (Decision decision: this.getPermissions().keySet()) {
                    if (!ObjUtil.equalsAllowNull(this.getPermissions(decision), result.getPermissions(decision))) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (getPermissions() != null) {
            result = 31 * result + getPermissions().hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean needsComma = false;
        if (getPermissions() != null) {
            if (needsComma) {
                sb.append(',');
            }

            sb.append("permissions={");
            
            for (Decision decision: getPermissions().keySet()) {
                sb.append("decision=");
                sb.append(decision);
                sb.append(',');
                sb.append("permissions=");
                sb.append(getPermissions(decision));
            }
                
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }
}
