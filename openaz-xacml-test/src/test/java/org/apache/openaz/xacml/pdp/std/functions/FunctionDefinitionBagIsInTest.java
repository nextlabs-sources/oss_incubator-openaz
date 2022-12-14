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
import org.apache.openaz.xacml.pdp.policy.Bag;
import org.apache.openaz.xacml.pdp.policy.ExpressionResult;
import org.apache.openaz.xacml.pdp.policy.FunctionArgument;
import org.apache.openaz.xacml.pdp.policy.FunctionArgumentAttributeValue;
import org.apache.openaz.xacml.pdp.policy.FunctionArgumentBag;
import org.apache.openaz.xacml.pdp.std.StdFunctions;
import org.apache.openaz.xacml.pdp.std.functions.FunctionDefinitionBagIsIn;
import org.apache.openaz.xacml.std.StdAttributeValue;
import org.apache.openaz.xacml.std.datatypes.DataTypes;
import org.junit.Test;

/**
 * Test of PDP Functions (See XACML core spec section A.3) TO RUN - use jUnit In Eclipse select this file or
 * the enclosing directory, right-click and select Run As/JUnit Test
 */
public class FunctionDefinitionBagIsInTest {

    /*
     * variables useful in the following tests
     */
    List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();

    @Test
    public void testString() {
        String v1 = new String("abc");
        String v2 = new String("def");
        String notInBag = new String("lmnop");
        String sameValueV1 = new String("abc");
        Integer vOtherType = new Integer(11);

        FunctionArgumentAttributeValue attrV1 = null;
        FunctionArgumentAttributeValue attrV2 = null;
        FunctionArgumentAttributeValue attrNotInBag = null;
        FunctionArgumentAttributeValue attrSameValueV1 = null;
        FunctionArgumentAttributeValue attrOtherType = null;
        try {
            attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
            attrV2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v2));
            attrNotInBag = new FunctionArgumentAttributeValue(
                                                              DataTypes.DT_STRING
                                                                  .createAttributeValue(notInBag));
            attrSameValueV1 = new FunctionArgumentAttributeValue(
                                                                 DataTypes.DT_STRING
                                                                     .createAttributeValue(sameValueV1));
            attrOtherType = new FunctionArgumentAttributeValue(
                                                               DataTypes.DT_INTEGER
                                                                   .createAttributeValue(vOtherType));
        } catch (Exception e) {
            fail("creating attributes e=" + e);
        }

        Bag bag0 = new Bag();
        Bag bag1 = new Bag();
        bag1.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));
        Bag bag2 = new Bag();
        bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));
        bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v2));
        ;

        FunctionArgumentBag attrBag0 = new FunctionArgumentBag(bag0);
        FunctionArgumentBag attrBag1 = new FunctionArgumentBag(bag1);
        FunctionArgumentBag attrBag2 = new FunctionArgumentBag(bag2);

        FunctionDefinitionBagIsIn<?> fd = (FunctionDefinitionBagIsIn<?>)StdFunctions.FD_STRING_IS_IN;

        // check identity and type of the thing created
        assertEquals(XACML3.ID_FUNCTION_STRING_IS_IN, fd.getId());
        assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());

        // just to be safe... If tests take too long these can probably be eliminated
        assertFalse(fd.returnsBag());

        // element is in bag
        arguments.clear();
        arguments.add(attrV1);
        arguments.add(attrBag2);
        ExpressionResult res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        assertEquals(Boolean.class, res.getValue().getValue().getClass());
        Boolean resValue = (Boolean)res.getValue().getValue();
        assertEquals(true, resValue);

        // element not in bag
        arguments.clear();
        arguments.add(attrNotInBag);
        arguments.add(attrBag2);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        assertEquals(Boolean.class, res.getValue().getValue().getClass());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(false, resValue);

        // different element with the same value is in bag
        arguments.clear();
        arguments.add(attrSameValueV1);
        arguments.add(attrBag2);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        assertEquals(Boolean.class, res.getValue().getValue().getClass());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(true, resValue);

        // empty bag
        arguments.clear();
        arguments.add(attrV1);
        arguments.add(attrBag0);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        assertEquals(Boolean.class, res.getValue().getValue().getClass());
        resValue = (Boolean)res.getValue().getValue();
        assertEquals(false, resValue);

        // missing arg
        arguments.clear();
        arguments.add(attrSameValueV1);
        res = fd.evaluate(null, arguments);
        assertFalse(res.isOk());
        assertEquals("function:string-is-in Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // 1st arg is bag
        arguments.clear();
        arguments.add(attrBag1);
        arguments.add(attrBag2);
        res = fd.evaluate(null, arguments);
        assertFalse(res.isOk());
        assertEquals("function:string-is-in Expected a simple value, saw a bag", res.getStatus()
            .getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // 2nd arg not bag
        arguments.clear();
        arguments.add(attrV1);
        arguments.add(attrV2);
        res = fd.evaluate(null, arguments);
        assertFalse(res.isOk());
        assertEquals("function:string-is-in Expected a bag, saw a simple value", res.getStatus()
            .getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // first arg null
        arguments.clear();
        arguments.add(null);
        arguments.add(attrBag2);
        res = fd.evaluate(null, arguments);
        assertFalse(res.isOk());
        assertEquals("function:string-is-in Got null argument", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // 2nd arg null
        arguments.clear();
        arguments.add(attrV1);
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertFalse(res.isOk());
        assertEquals("function:string-is-in Got null argument", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // first arg type does not match bag elements
        arguments.clear();
        arguments.add(attrOtherType);
        arguments.add(attrBag2);
        res = fd.evaluate(null, arguments);
        assertFalse(res.isOk());
        assertEquals("function:string-is-in Expected data type 'string' saw 'integer'", res.getStatus()
            .getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // bag has mixed element types
        // behavior not specified for this case in spec. It ASSUMES that all elements in bag are same type.

    }

    //
    //
    // REST OF DATA TYPES OMITTED
    // because they "should" all work the same
    //
    //

}
