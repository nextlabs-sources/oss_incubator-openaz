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

package org.apache.openaz.pepapi;

import java.util.Collection;
import java.util.Map;

import org.apache.openaz.xacml.api.Decision;
import org.apache.openaz.xacml.api.PermissionsResult;

public interface PepPermissionsResponse {
    /**
     * Get all the actions that would yield the specified decision
     */
    Collection<Action> getActionsForDecision(Decision decision);

    /**
     * Get the obligations associated with this response given the action
     *
     * @return a collection of <code>Obligation</code> that would be returned if
     * the caller sent the original query along with this action
     *
     * @param decision the decision
     * @param action the action
     */
    Map<String, Obligation> getObligations(Decision decision, Action action) throws PepException;

    /**
     * Get the policies that would be invoked from this action
     *
     * @param decision the decision
     * @param action the action
     */
    Collection<String> getPolicies(Decision decision, Action action) throws PepException;

    /**
     * Get the advice associated with this response given the action
     *
     * @return a collection of <code>Advice</code> that would be returned if
     * the caller sent the original query along with this action
     *
     * @param decision the decision
     * @param action the action
     */
    Collection<Advice> getAdvice(Decision decision, Action action) throws PepException;

    /**
     * Return the <code>PermissionsResult</code> object
     */
    PermissionsResult getWrappedResult();
}
