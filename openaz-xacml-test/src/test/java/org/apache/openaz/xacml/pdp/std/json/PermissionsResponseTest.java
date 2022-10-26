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
 * Created on Jan 23, 2022
 *
 * All sources, binaries and HTML pages (C) copyright 2022 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 *
 * @author amorgan
 */

package org.apache.openaz.xacml.pdp.std.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.math.BigInteger;

import org.apache.openaz.xacml.api.Attribute;
import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.Decision;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.api.Obligation;
import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.apache.openaz.xacml.std.StdAttribute;
import org.apache.openaz.xacml.std.StdAttributeCategory;
import org.apache.openaz.xacml.std.StdAttributeValue;
import org.apache.openaz.xacml.std.StdIdReference;
import org.apache.openaz.xacml.std.StdMutableAdvice;
import org.apache.openaz.xacml.std.StdMutableAttribute;
import org.apache.openaz.xacml.std.StdMutableAttributeAssignment;
import org.apache.openaz.xacml.std.StdMutableMissingAttributeDetail;
import org.apache.openaz.xacml.std.StdMutableObligation;
import org.apache.openaz.xacml.std.StdMutablePermissions;
import org.apache.openaz.xacml.std.StdMutablePermissionsResponse;
import org.apache.openaz.xacml.std.StdMutablePermissionsResult;
import org.apache.openaz.xacml.std.StdMutablePoliciesAndObligations;
import org.apache.openaz.xacml.std.StdMutableStatus;
import org.apache.openaz.xacml.std.StdMutableStatusDetail;
import org.apache.openaz.xacml.std.StdPermissionsResult;
import org.apache.openaz.xacml.std.StdStatusCode;
import org.apache.openaz.xacml.std.StdVersion;
import org.apache.openaz.xacml.std.datatypes.DataTypes;
import org.apache.openaz.xacml.std.datatypes.StringNamespaceContext;
import org.apache.openaz.xacml.std.datatypes.XPathExpressionWrapper;
import org.apache.openaz.xacml.std.json.JSONPermissionsResponse;
import org.apache.openaz.xacml.std.json.JSONStructureException;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PermissionsResponseTest {
    String jsonPermissionsResponse;
    StdMutablePermissionsResponse permissionsResponse;
    StdMutablePermissionsResult permissionsResult;

    @Test
    public void testMinimalResponse() {
        // null response
        try {
            jsonPermissionsResponse = JSONPermissionsResponse.toString(null, false);
            fail("Operation should throw exception");
        } catch (JSONStructureException e) {
            // correct behavior
        } catch (Exception e) {
            fail("Failed convert from object to JSON: " + e);
        }

        permissionsResponse = new StdMutablePermissionsResponse();
        try {
            jsonPermissionsResponse = JSONPermissionsResponse.toString(permissionsResponse, false);
            fail("Operation should thorw exception");
        } catch (JSONStructureException e) {
            // correct behavior
        } catch (Exception e) {
            fail("Failed convert from object to JSON: " + e);
        }

        permissionsResult = new StdMutablePermissionsResult();
        permissionsResult.setPermissionsResult(Decision.PERMIT, new StdMutablePermissions());
        permissionsResponse = new StdMutablePermissionsResponse(permissionsResult);
        
        try {
            jsonPermissionsResponse = JSONPermissionsResponse.toString(permissionsResponse, false);
            String expected = "{\"Status\":{\"StatusCode\":{\"Value\":\"urn:oasis:names:tc:xacml:1.0:status:ok\"}},\"Response\":[{\"ActionsAndObligations\":{\"allow\":[]}}]}";
            assertEquals(expected, jsonPermissionsResponse);
        } catch(Exception e) {
            fail("operation failed: " + e);
        }
    }

    @Test
    public void testCompleteResponse() {
        // Dial up a fully loaded multi-response
        StdMutablePermissionsResponse response = new StdMutablePermissionsResponse();

        // Status object
        StdMutableStatus status = new StdMutableStatus(StdStatusCode.STATUS_CODE_OK);
        response.setStatus(status);

        List<String> policies = new ArrayList<String>();
        policies.add("policyA");
        policies.add("policyB");
        policies.add("policyC");

        List<Obligation> obligations = new ArrayList<Obligation>();
        StdMutableObligation obligation = new StdMutableObligation();
        obligation.setId(new IdentifierImpl("obligation1"));
        obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
                                              XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE,
                                              XACML3.ID_SUBJECT,
                                              "obligation-issuer1",
                                              new StdAttributeValue<String>(
                                                  DataTypes.DT_STRING
                                                  .getId(),
                                                  "Bart")));
        
        obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
                                              XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE,
                                              XACML3.ID_SUBJECT,
                                              "obligation-issuer2",
                                              new StdAttributeValue<String>(
                                                  DataTypes.DT_STRING
                                                  .getId(),
                                                  "Ned")));

        obligations.add(obligation);

        obligation = new StdMutableObligation();
        obligation.setId(new IdentifierImpl("obligation2"));
        obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
                                              XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE,
                                              XACML3.ID_SUBJECT,
                                              "obligation-issuer1",
                                              new StdAttributeValue<String>(
                                                  DataTypes.DT_STRING.getId(),
                                                  "Scooter")));
        obligations.add(obligation);
        
        StdMutablePoliciesAndObligations policiesAndObligations = new StdMutablePoliciesAndObligations();
        policiesAndObligations.addPolicies(policies);
        policiesAndObligations.addObligations(obligations);

        StdMutablePermissions permissions = new StdMutablePermissions();
        permissions.setPoliciesAndObligations("OPEN", policiesAndObligations);

        StdMutablePermissionsResult permissionsResult = new StdMutablePermissionsResult();
        permissionsResult.setPermissionsResult(Decision.PERMIT, permissions);

        response.add(permissionsResult);

        try {
            jsonPermissionsResponse = JSONPermissionsResponse.toString(response, false);
            System.out.println(jsonPermissionsResponse);
            String expectedJson = "{\"Status\":{\"StatusCode\":{\"Value\":\"urn:oasis:names:tc:xacml:1.0:status:ok\"}},\"Response\":[{\"ActionsAndObligations\":{\"allow\":[{\"Action\":\"OPEN\",\"Obligations\":[{\"AttributeAssignment\":[{\"Category\":\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\",\"Issuer\":\"obligation-issuer1\",\"AttributeId\":\"urn:oasis:names:tc:xacml:1.0:subject\",\"Value\":\"Bart\",\"DataType\":\"http://www.w3.org/2001/XMLSchema#string\"},{\"Category\":\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\",\"Issuer\":\"obligation-issuer2\",\"AttributeId\":\"urn:oasis:names:tc:xacml:1.0:subject\",\"Value\":\"Ned\",\"DataType\":\"http://www.w3.org/2001/XMLSchema#string\"}],\"Id\":\"obligation1\"},{\"AttributeAssignment\":[{\"Category\":\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\",\"Issuer\":\"obligation-issuer1\",\"AttributeId\":\"urn:oasis:names:tc:xacml:1.0:subject\",\"Value\":\"Scooter\",\"DataType\":\"http://www.w3.org/2001/XMLSchema#string\"}],\"Id\":\"obligation2\"}],\"MatchingPolicies\":[\"policyA\",\"policyB\",\"policyC\"]}]}}]}";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode treeFromObjects = mapper.readTree(jsonPermissionsResponse);
            JsonNode treeFromString = mapper.readTree(expectedJson);
            assertTrue(treeFromObjects.equals(treeFromString));
        } catch (Exception e) {
            fail("operation failed: " + e);
        }
    }
}
