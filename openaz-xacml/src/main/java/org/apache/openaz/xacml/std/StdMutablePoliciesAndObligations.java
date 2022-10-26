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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import org.apache.openaz.xacml.api.Obligation;
import org.apache.openaz.xacml.api.PoliciesAndObligations;

public class StdMutablePoliciesAndObligations implements PoliciesAndObligations {
    private static final List<Obligation> EMPTY_OBLIGATION_LIST = Collections.unmodifiableList(new ArrayList<Obligation>());
    private static final List<String> EMPTY_POLICY_LIST = Collections.unmodifiableList(new ArrayList<String>());

    private List<Obligation> obligations = EMPTY_OBLIGATION_LIST;
    private List<String> policies = EMPTY_POLICY_LIST;

    public StdMutablePoliciesAndObligations() {
    }

    public StdMutablePoliciesAndObligations(PoliciesAndObligations policiesAndObligations) {
        addPolicies(policiesAndObligations.getPolicies());
        addObligations(policiesAndObligations.getObligations());
    }

    public StdMutablePoliciesAndObligations(Collection<String> policies, Collection<Obligation> obligations) {
        addPolicies(policies);
        addObligations(obligations);
    }
    
    /**
     * Adds a copy of the given <code>Collection</code> of {@link
     * org.apache.openaz.xacml.api.Obligation}s to this
     * <code>StdMutablePoliciesAndObligations</code>.
     *
     * @param obligationsIn the <code>Collection</code> of <code>Obligation</code>s to add
     */
    public void addObligations(Collection<Obligation> obligationsIn) {
        if (obligationsIn != null && obligationsIn.size() > 0) {
            if (this.obligations == EMPTY_OBLIGATION_LIST) {
                this.obligations = new ArrayList<Obligation>();
            }
            this.obligations.addAll(obligationsIn);
        }
    }
    
    /**
     * Adds a copy of the given <code>Collection</code> of
     * <code>String</code>s of policy names to this
     * <code>StdMutablePoliciesAndObligations</code>.
     *
     * @param policy names the <code>Collection</code> of <code>String</code>s to add
     */
    public void addPolicies(Collection<String> policiesIn) {
        if (policiesIn != null && policiesIn.size() > 0) {
            if (policies == EMPTY_POLICY_LIST) {
                policies = new ArrayList<String>();
            }
            policies.addAll(policiesIn);
        }
    }

    @Override
    public Collection<Obligation> getObligations() {
        return obligations == EMPTY_OBLIGATION_LIST ? obligations : Collections.unmodifiableCollection(obligations);
    }

    @Override
    public Collection<String> getPolicies() {
        return policies == EMPTY_POLICY_LIST ? policies : Collections.unmodifiableCollection(policies);
    }
}
