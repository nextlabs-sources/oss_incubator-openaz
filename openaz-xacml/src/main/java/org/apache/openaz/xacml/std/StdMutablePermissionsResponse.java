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

package org.apache.openaz.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.openaz.xacml.api.PermissionsResponse;
import org.apache.openaz.xacml.api.PermissionsResult;
import org.apache.openaz.xacml.api.Status;
import org.apache.openaz.xacml.util.ListUtil;

public class StdMutablePermissionsResponse implements PermissionsResponse {
    private static final List<PermissionsResult> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<PermissionsResult>());

    private Status status;
    private List<PermissionsResult> results;

    public StdMutablePermissionsResponse() {
        this.results = EMPTY_LIST;
    }

    public StdMutablePermissionsResponse(PermissionsResult result) {
        this(StdStatus.STATUS_OK, result);
    }
    
    public StdMutablePermissionsResponse(Status status, PermissionsResult result) {
        this.status = status;
        
        if (result != null) {
            results = new ArrayList<PermissionsResult>();
            results.add(result);
        } else {
            results = EMPTY_LIST;
        }
    }

    public StdMutablePermissionsResponse(Status status, Collection<PermissionsResult> results) {
        this.status = status;
        
        if (results != null && !results.isEmpty()) {
            this.results = new ArrayList<PermissionsResult>(results);
        } else {
            this.results = EMPTY_LIST;
        }
    }
    
    public StdMutablePermissionsResponse(PermissionsResponse response) {
        this(response.getStatus(),
             response.getPermissionsResults());
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    @Override
    public Collection<PermissionsResult> getPermissionsResults() {
        return results;
    }

    public void add(PermissionsResult result) {
        if (results == EMPTY_LIST) {
            results = new ArrayList<PermissionsResult>();
        }

        results.add(result);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !(obj instanceof PermissionsResponse)) {
            return false;
        } else {
            return ListUtil.equalsAllowNulls(this.getPermissionsResults(), ((PermissionsResponse)obj).getPermissionsResults());
        }
    }

    @Override
    public int hashCode() {
        int result = 18;
        if (getPermissionsResults() != null) {
            result = 31 * result + getPermissionsResults().hashCode();
        }

        return result;
    }
}
