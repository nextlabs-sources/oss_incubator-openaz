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

package org.apache.openaz.pepapi.std;

import org.apache.openaz.pepapi.ObligationRouter;
import org.apache.openaz.pepapi.PepConfig;
import org.apache.openaz.pepapi.PepResponse;
import org.apache.openaz.pepapi.PepResponseFactory;
import org.apache.openaz.xacml.api.Result;

final class StdPepResponseFactory implements PepResponseFactory {

    private PepConfig pepConfig;

    private ObligationRouter obligationRouter;

    StdPepResponseFactory(PepConfig pepConfig, ObligationRouter obligationRouter) {
        this.pepConfig = pepConfig;
        this.obligationRouter = obligationRouter;
    }

    @Override
    public PepResponse newPepResponse(Result result) {
        return StdPepResponse.newInstance(pepConfig, obligationRouter, result);
    }
}
