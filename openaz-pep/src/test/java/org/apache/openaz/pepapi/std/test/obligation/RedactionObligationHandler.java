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

package org.apache.openaz.pepapi.std.test.obligation;

import junit.framework.Assert;

import org.apache.openaz.pepapi.Obligation;
import org.apache.openaz.pepapi.ObligationHandler;
import org.apache.openaz.pepapi.ObligationStore;

import java.util.Map;
import java.util.Set;

public class RedactionObligationHandler implements ObligationHandler {

    private ObligationStore obligationStore;

    public void enforce() {
        Set<Obligation> obligationSet = obligationStore.getHandlerObligations(this.getClass());
        if (obligationSet.size() == 1) {
            for (Obligation obligation : obligationSet) {
                Assert.assertEquals("urn:oasis:names:tc:xacml:2.0:obligation:obligation-2",
                                    obligation.getId());
            }
        } else {
            Assert.assertFalse(true);
        }
    }

    @Override
    public boolean match(Obligation obligation) {
        Map<String, Object[]> map = obligation.getAttributeMap();
        return map.containsKey("urn:oasis:names:tc:xacml:1.0:subject:age");
    }

    @Override
    public void setObligationStore(ObligationStore obligationStore) {
        this.obligationStore = obligationStore;
    }
}
