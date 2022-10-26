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

package org.apache.openaz.pepapi.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.openaz.pepapi.Action;
import org.apache.openaz.pepapi.Advice;
import org.apache.openaz.pepapi.Obligation;
import org.apache.openaz.pepapi.PepException;
import org.apache.openaz.pepapi.PepPermissionsResponse;
import org.apache.openaz.xacml.api.Decision;
import org.apache.openaz.xacml.api.PermissionsResult;

final class StdPepPermissionsResponse implements PepPermissionsResponse {
    private final PermissionsResult wrappedResult;

    static PepPermissionsResponse newInstance(PermissionsResult result) {
        return new StdPepPermissionsResponse(result);
    }

    private StdPepPermissionsResponse(PermissionsResult result) {
        this.wrappedResult = result;
    }

    @Override
    public Collection<Action> getActionsForDecision(Decision decision) {
        List<Action> actions = new ArrayList<Action>();

        for (String action : wrappedResult.getPermissions(decision).getActions()) {
            actions.add(Action.newInstance(action));
                                                      
        }
        return actions;
    }

    @Override
    public Map<String, Obligation> getObligations(Decision decision, Action action) throws PepException {
        Map<String, Obligation> obligationMap = new HashMap<String, Obligation>();

        String actionId = action.getActionIdValue();
        
        for (org.apache.openaz.xacml.api.Obligation wrappedObligation : wrappedResult.getPermissions(decision).getPoliciesAndObligations(actionId).getObligations()) {
            Obligation obligation = new StdObligation(wrappedObligation);
            obligationMap.put(obligation.getId(), obligation);
        }

        return obligationMap;
    }
    
    @Override
    public Collection<String> getPolicies(Decision decision, Action action) throws PepException {
        String actionId = action.getActionIdValue();
        
        return wrappedResult.getPermissions(decision).getPoliciesAndObligations(actionId).getPolicies();
    }
    
    @Override
    public Collection<Advice> getAdvice(Decision decision, Action action) throws PepException {
        return new ArrayList<Advice>();
    }
    
    @Override
    public PermissionsResult getWrappedResult() {
        return wrappedResult;
    }
}
