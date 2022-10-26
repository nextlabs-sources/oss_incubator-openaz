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

/**
 * Defines the API for objects that represent XACML Pemissions
 */

public interface PermissionsResponse {
    /**
     * Gets the <code>Collection</code> of {@link PermissionsResult}s objects in this <code>PermissionsResponse</code>. If there are
     * no <code>Result</code>s, this method must return an empty <code>Collection</code>.
     *
     * @return the <code>Collection</code> of {@link PermissionsResult}s objects in this <code>PermissionsResponse</code>.
     */
    Collection<PermissionsResult> getPermissionsResults();
    
    /**
     * Get the {@link org.apache.openaz.xacml.api.Status} of this <code>PermissionsResponse</code>
     */
    Status getStatus();

    /**
     * {@inheritDoc} Implementations of this interface must override the <code>equals</code> method with the
     * following semantics: Two <code>Response</code>s (<code>r1</code> and <code>r2</code>) are equal if:
     * {@code r1.getResults()} is pairwise equal to {@code r2.getResults()}
     */
    @Override
    boolean equals(Object obj);
}
