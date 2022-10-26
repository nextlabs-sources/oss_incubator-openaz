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

import java.util.Collection;

import org.apache.openaz.xacml.api.PermissionsResponse;
import org.apache.openaz.xacml.api.PermissionsResult;
import org.apache.openaz.xacml.api.Status;
import org.apache.openaz.xacml.util.Wrapper;

public class StdPermissionsResponse extends Wrapper<PermissionsResponse> implements PermissionsResponse {
    /**
     * Creates an immutable <code>StdPermissionsResponse</code> wrapping the given
     * {@link org.apache.openaz.xacml.api.PermissionsResponse}
     *
     * @param response the <code>PermissionsResponse</code> to wrap in the new <code>StdPermissionsResponse</code>
     */
    public StdPermissionsResponse(PermissionsResponse response) {
        super(response);
    }

    /**
     * Creates an new <code>StdPermissionsResponse</code> with the single given {@link orig.apache.openaz.xacml.api.PermissionsResult}
     *
     * @param result the <code>PermissionsResult/code> for the new <code>StdPermissionsResponse</code>.
     */
    public StdPermissionsResponse(PermissionsResult result) {
        this(new StdMutablePermissionsResponse(result));
    }

    /**
     * Creates an new <code>StdPermissionsResponse</code> with a coopy
     * of the {@link orig.apache.openaz.xacml.api.PermissionsResult}s
     * in the given <code>Collection</code> and a <code>Status</code>
     *
     * @param results the <code>Collection</code> of <code>PermissionsResult/code> to copy
     */
    public StdPermissionsResponse(Collection<PermissionsResult> results) {
        this (StdStatus.STATUS_OK, results);
    }
    
    public StdPermissionsResponse(Status status, Collection<PermissionsResult> results) {
        this (new StdMutablePermissionsResponse(status, results));
    }
    

    @Override
    public Status getStatus() {
        return this.getWrappedObject().getStatus();
    }
    
    @Override
    public Collection<PermissionsResult> getPermissionsResults() {
        return this.getWrappedObject().getPermissionsResults();
    }
}
