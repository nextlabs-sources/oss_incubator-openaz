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

import org.apache.openaz.xacml.api.Obligation;
import org.apache.openaz.xacml.api.PoliciesAndObligations;
import org.apache.openaz.xacml.util.Wrapper;

public class StdPoliciesAndObligations extends Wrapper<PoliciesAndObligations> implements PoliciesAndObligations {
    /**
     * Creates an immutable <code>PoliciesAndObligations</code> with the given
     * {@link org.apache.openaz.xacml.api.PoliciesAndObligations}.
     *
     * @param policiesAndObligations the
     * <code>PoliciesAndObligations</code> for the new
     * <code>PoliciesAndObligations</code>
     */
    public StdPoliciesAndObligations(PoliciesAndObligations policiesAndObligations) {
        super(policiesAndObligations);
    }

    /**
     * Creates an immutable <code>StdPemissions</code> with the given
     * <code>Collection</code>s of policis and obligations
     *
     * @param policiesAndObligations the
     * <code>PoliciesAndObligations</code> for the new
     * <code>PoliciesAndObligations</code>
     */
    public StdPoliciesAndObligations(Collection<String> policies, Collection<Obligation> obligations) {
        this(new StdMutablePoliciesAndObligations(policies, obligations));
    }

    @Override
    public Collection<String> getPolicies() {
        return this.getWrappedObject().getPolicies();
    }

    @Override
    public Collection<Obligation> getObligations() {
        return this.getWrappedObject().getObligations();
    }
}
