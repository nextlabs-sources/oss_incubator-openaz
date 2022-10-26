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
 *                        AT&T - PROPRIETARY
 *          THIS FILE CONTAINS PROPRIETARY INFORMATION OF
 *        AT&T AND IS NOT TO BE DISCLOSED OR USED EXCEPT IN
 *             ACCORDANCE WITH APPLICABLE AGREEMENTS.
 *
 *          Copyright (c) 2013 AT&T Knowledge Ventures
 *              Unpublished and Not for Publication
 *                     All Rights Reserved
 */
package org.apache.openaz.xacml.pdp.policy.dom;

import org.apache.openaz.xacml.pdp.policy.PolicyIdReference;
import org.apache.openaz.xacml.pdp.policy.PolicySet;
import org.apache.openaz.xacml.std.StdStatusCode;
import org.apache.openaz.xacml.std.dom.DOMIdReferenceMatch;
import org.apache.openaz.xacml.std.dom.DOMProperties;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.w3c.dom.Node;

/**
 * DOMPolicyIdReference extends {@link org.apache.openaz.xacml.pdp.policy.PolicyIdReference} with methods
 * for creation from DOM {@link org.w3c.dom.Node}s.
 */
public class DOMPolicyIdReference {

    protected DOMPolicyIdReference() {
    }

    /**
     * Creates a new <code>PolicyIdReference</code> parsed from the given <code>Node</code> representing a
     * XACML PolicyIdReference element.
     *
     * @param nodePolicyIdReference the <code>Node</code> representing the XACML PolicyIdReference element
     * @return a new <code>PolicyIdReference</code> parsed from the given <code>Node</code>
     * @throws DOMStructureException if there is an error parsing the <code>Node</code>
     */
    public static PolicyIdReference newInstance(Node nodePolicyIdReference, PolicySet policySetParent)
        throws DOMStructureException {
        PolicyIdReference domPolicyIdReference = new PolicyIdReference(policySetParent);

        try {
            domPolicyIdReference.setIdReferenceMatch(DOMIdReferenceMatch.newInstance(nodePolicyIdReference));
        } catch (DOMStructureException ex) {
            domPolicyIdReference.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
            if (DOMProperties.throwsExceptions()) {
                throw ex;
            }
        }

        return domPolicyIdReference;
    }

    public static boolean repair(Node nodePolicyIdReference) throws DOMStructureException {
        return DOMIdReferenceMatch.repair(nodePolicyIdReference);
    }
}
