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

package org.apache.openaz.xacml.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.pdp.policy.ExpressionResult;
import org.apache.openaz.xacml.pdp.policy.FunctionArgument;
import org.apache.openaz.xacml.pdp.policy.FunctionArgumentAttributeValue;
import org.apache.openaz.xacml.pdp.std.StdFunctions;
import org.apache.openaz.xacml.pdp.std.functions.FunctionDefinitionLogical;
import org.apache.openaz.xacml.std.datatypes.DataTypes;
import org.junit.Test;

/**
 * Test of PDP Functions (See XACML core spec section A.3) TO RUN - use jUnit In Eclipse select this file or
 * the enclosing directory, right-click and select Run As/JUnit Test
 */
public class FunctionDefinitionLogicalTest {

    /*
     * variables useful in the following tests
     */
    List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();

    // use the same args for each test
    FunctionArgumentAttributeValue attrT = null;
    FunctionArgumentAttributeValue attrF = null;

    public FunctionDefinitionLogicalTest() {
        try {
            attrT = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(true));
            attrF = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(false));
        } catch (Exception e) {
            fail("creating attribute e=" + e);
        }
    }

    @Test
    public void testOR() {
        FunctionArgumentAttributeValue attr5 = null;
        try {
            attr5 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));

        } catch (Exception e) {
            fail("creating attribute e=" + e);
        }

        FunctionDefinitionLogical fd = (FunctionDefinitionLogical)StdFunctions.FD_OR;

        // check identity and type of the thing created
        assertEquals(XACML3.ID_FUNCTION_OR, fd.getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeArgs().getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());

        // just to be safe... If tests take too long these can probably be eliminated
        assertFalse(fd.returnsBag());

        // test normal
        arguments.add(attrT);
        arguments.add(attrF);
        ExpressionResult res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        Boolean resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        arguments.clear();
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(false), resValue);

        // test no args
        arguments.clear();
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(false), resValue);

        // first true, second error
        arguments.clear();
        arguments.add(attrT);
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        // first false, second error
        arguments.clear();
        arguments.add(attrF);
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:or Got null argument", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // multiple false
        arguments.clear();
        arguments.add(attrF);
        arguments.add(attrF);
        arguments.add(attrF);
        arguments.add(attrF);
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(false), resValue);

        // non-boolean
        arguments.clear();
        arguments.add(attrF);
        arguments.add(attr5);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:or Expected data type 'boolean' saw 'integer'", res.getStatus()
            .getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // first arg error
        arguments.clear();
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:or Got null argument", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

    }

    @Test
    public void testAND() {
        FunctionArgumentAttributeValue attr5 = null;
        try {
            attr5 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));

        } catch (Exception e) {
            fail("creating attribute e=" + e);
        }

        FunctionDefinitionLogical fd = (FunctionDefinitionLogical)StdFunctions.FD_AND;

        // check identity and type of the thing created
        assertEquals(XACML3.ID_FUNCTION_AND, fd.getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeArgs().getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());

        // just to be safe... If tests take too long these can probably be eliminated
        assertFalse(fd.returnsBag());

        // test normal
        arguments.add(attrT);
        arguments.add(attrF);
        ExpressionResult res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        Boolean resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(false), resValue);

        arguments.clear();
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(false), resValue);

        // test no args
        arguments.clear();
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        // first true, second error
        arguments.clear();
        arguments.add(attrT);
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:and Got null argument", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // first false, second error
        arguments.clear();
        arguments.add(attrF);
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(false), resValue);

        // multiple true
        arguments.clear();
        arguments.add(attrT);
        arguments.add(attrT);
        arguments.add(attrT);
        arguments.add(attrT);
        arguments.add(attrT);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        // non-boolean
        arguments.clear();
        arguments.add(attrT);
        arguments.add(attr5);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:and Expected data type 'boolean' saw 'integer'", res.getStatus()
            .getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // first arg error
        arguments.clear();
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:and Got null argument", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

    }

    @Test
    public void testN_of() {
        FunctionArgumentAttributeValue attr0 = null;
        FunctionArgumentAttributeValue attr2 = null;
        try {
            attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
            attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(2));
        } catch (Exception e) {
            fail("creating attribute e=" + e);
        }

        FunctionDefinitionLogical fd = (FunctionDefinitionLogical)StdFunctions.FD_N_OF;

        // check identity and type of the thing created
        assertEquals(XACML3.ID_FUNCTION_N_OF, fd.getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeArgs().getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());

        // just to be safe... If tests take too long these can probably be eliminated
        assertFalse(fd.returnsBag());

        // test normal
        arguments.add(attr2);
        arguments.add(attrT);
        arguments.add(attrF);
        arguments.add(attrT);
        ExpressionResult res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        Boolean resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        // normal fail
        arguments.clear();
        arguments.add(attr2);
        arguments.add(attrT);
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(false), resValue);

        // null count
        arguments.clear();
        arguments.add(null);
        arguments.add(attrT);
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        // 0 count
        arguments.clear();
        arguments.add(attr0);
        arguments.add(attrT);
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        // bad object type for count
        arguments.clear();
        arguments.add(attrT);
        arguments.add(attrT);
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:n-of For input string: \"true\"", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // count larger than list
        arguments.clear();
        arguments.add(attr2);
        arguments.add(attrT);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:n-of Expected 2 arguments but only 1 arguments in list after the count", res
            .getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // aborts after find ok
        arguments.clear();
        arguments.add(attr2);
        arguments.add(attrT);
        arguments.add(attrT);
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        // error before find ok
        arguments.clear();
        arguments.add(attr2);
        arguments.add(null);
        arguments.add(attrT);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:n-of Got null argument", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // non-boolean in list
        arguments.clear();
        arguments.add(attr2);
        arguments.add(attrT);
        arguments.add(attr0);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:n-of Expected data type 'boolean' saw 'integer'", res.getStatus()
            .getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

    }

    @Test
    public void testNot() {

        FunctionDefinitionLogical fd = (FunctionDefinitionLogical)StdFunctions.FD_NOT;

        // check identity and type of the thing created
        assertEquals(XACML3.ID_FUNCTION_NOT, fd.getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeArgs().getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());

        // just to be safe... If tests take too long these can probably be eliminated
        assertFalse(fd.returnsBag());

        // test normal
        arguments.clear();
        arguments.add(attrT);
        ExpressionResult res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        Boolean resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(false), resValue);

        arguments.clear();
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(new Boolean(true), resValue);

        // test null/0 args
        arguments.clear();
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:not Expected 1 argument, got 0", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // test 2 args
        arguments.clear();
        arguments.add(attrT);
        arguments.add(attrF);
        res = fd.evaluate(null, arguments);
        assertFalse(res.getStatus().isOk());
        assertEquals("function:not Expected 1 argument, got 2", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());
    }
}
