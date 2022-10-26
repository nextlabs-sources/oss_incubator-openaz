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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openaz.pepapi.*;
import org.apache.openaz.xacml.api.PermissionsResponse;
import org.apache.openaz.xacml.api.PermissionsResult;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.Response;
import org.apache.openaz.xacml.api.Result;
import org.apache.openaz.xacml.api.pdp.PDPEngine;
import org.apache.openaz.xacml.api.pdp.PDPEngineFactory;
import org.apache.openaz.xacml.api.pdp.PDPException;
import org.apache.openaz.xacml.std.json.JSONRequest;
import org.apache.openaz.xacml.std.json.JSONResponse;
import org.apache.openaz.xacml.std.json.JSONStructureException;
import org.apache.openaz.xacml.util.FactoryException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

final class StdPepAgent implements PepAgent {

    private static final Log logger = LogFactory.getLog(StdPepAgent.class);

    private Properties xacmlProperties;

    private PepConfig pepConfig;

    private PDPEngine pdpEngine;

    private PDPEngineFactory pdpEngineFactory;

    private List<ObligationStoreAware> obligationHandlers;

    private PepRequestFactory pepRequestFactory;

    private PepResponseFactory pepResponseFactory;
    
    private PepPermissionsResponseFactory pepPermissionsResponseFactory;

    StdPepAgent() {
        obligationHandlers = new ArrayList<ObligationStoreAware>();
    }

    void initialize() {
        assert pdpEngineFactory != null;

        // Instantiate PDPEngine
        if (pdpEngine == null) {
            try {
                pdpEngine = pdpEngineFactory.newEngine(xacmlProperties);
            } catch (FactoryException e) {
                throw new PepException(e);
            }
        }

        List<ObjectMapper> objectMappers = new ArrayList<ObjectMapper>();
        for (String mapperClassName : pepConfig.getMapperClassNames()) {
            Class<? extends ObjectMapper> clazz = (Class<? extends ObjectMapper>)PepUtils
                .loadClass(mapperClassName);
            objectMappers.add(PepUtils.instantiateClass(clazz));
        }
        MapperRegistry mapperRegistry = StdMapperRegistry.newInstance(pepConfig, objectMappers);

        ObligationRouter oRouter = null;
        if (!obligationHandlers.isEmpty()) {
            ObligationHandlerRegistry oHandlerRegistry = StdObligationHandlerRegistry
                .newInstance(obligationHandlers);
            ThreadLocalObligationStore oStore = ThreadLocalObligationStore.newInstance();
            for (ObligationStoreAware oHandler : obligationHandlers) {
                oHandler.setObligationStore(oStore);
            }
            oRouter = StdObligationRouter.newInstance(oHandlerRegistry, oStore);
        }

        // Instantiate PepRequestFactory
        pepRequestFactory = new StdPepRequestFactory(pepConfig, mapperRegistry);
        // Instantiate PepResponseFactory
        pepResponseFactory = new StdPepResponseFactory(pepConfig, oRouter);
        // Instantiate PepPermissionsResponseFactory
        pepPermissionsResponseFactory = new StdPepPermissionsResponseFactory();
    }

    @Override
    public PepResponse decide(Object... objects) {
        return decide(pepRequestFactory.newPepRequest(objects)).get(0);
    }
    
    @Override
    public PepPermissionsResponse decidePermissions(Object... objects) {
        return decidePermissions(pepRequestFactory.newPepRequest(objects)).get(0);
    }

    @Override
    public PepResponse simpleDecide(String subjectId, String actionId, String resourceId) {
        return decide(Subject.newInstance(subjectId), Action.newInstance(actionId),
                      Resource.newInstance(resourceId));
    }

    @Override
    public List<PepResponse> bulkDecide(List<?> actionResourcePairs, Object... objects) {
        return decide(pepRequestFactory.newBulkPepRequest(actionResourcePairs, objects));
    }

    private List<PepResponse> decide(PepRequest pepRequest) { //NOPMD
        List<PepResponse> pepResponses = new ArrayList<PepResponse>();
        Request request = pepRequest.getWrappedRequest();

        // Log request
        if (logger.isDebugEnabled()) {
            logRequest(request);
        }

        Response response;
        try {
            response = pdpEngine.decide(request);
        } catch (PDPException e) {
            logger.error(e);
            throw new PepException(e);
        }

        // Log the response
        if (logger.isDebugEnabled()) {
            logResponse(response);
        }

        for (Result result : response.getResults()) {
            pepResponses.add(pepResponseFactory.newPepResponse(result));
        }
        return pepResponses;
    }

    private List<PepPermissionsResponse> decidePermissions(PepRequest pepRequest) { // NOPMD
        List<PepPermissionsResponse> pepPermissionsResponses = new ArrayList<PepPermissionsResponse>();
        Request request = pepRequest.getWrappedRequest();

        if (logger.isDebugEnabled()) {
            logRequest(request);
        }

        PermissionsResponse response;

        try {
            response = pdpEngine.decidePermissions(request);
        } catch (PDPException e) {
            logger.error(e);
            throw new PepException(e);
        }

        for (PermissionsResult result : response.getPermissionsResults()) {
            pepPermissionsResponses.add(pepPermissionsResponseFactory.newPepPermissionsResponse(result));
        }
        return pepPermissionsResponses;
    }
    
    private void logRequest(Request request) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JSONRequest.convert(request, out);
            logger.debug(out.toString("UTF-8"));
        } catch (IOException e) {
            logger.debug("Error printing XACML request in JSON", e);
        } catch (JSONStructureException e) {
            logger.debug("Error printing XACML request in JSON", e);
        }
    }

    private void logResponse(Response response) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JSONResponse.convert(response, out);
            logger.debug(out.toString("UTF-8"));
        } catch (IOException e) {
            logger.debug("Error printing XACML response in JSON", e);
        } catch (JSONStructureException e) {
            logger.debug("Error printing XACML response in JSON", e);
        }
    }

    public PDPEngine getPdpEngine() {
        return pdpEngine;
    }

    public PepConfig getPepConfig() {
        return pepConfig;
    }

    void setPdpEngineFactory(PDPEngineFactory pdpEngineFactory) {
        this.pdpEngineFactory = pdpEngineFactory;
    }

    void setPepConfig(PepConfig pepConfig) {
        this.pepConfig = pepConfig;
    }

    void setXacmlProperties(Properties properties) {
        this.xacmlProperties = properties;
    }

    void setObligationHandlers(List<ObligationStoreAware> obligationHandlers) {
        if (obligationHandlers != null) {
            this.obligationHandlers = new ArrayList<ObligationStoreAware>();
            this.obligationHandlers.addAll(obligationHandlers);
        }
    }
}
