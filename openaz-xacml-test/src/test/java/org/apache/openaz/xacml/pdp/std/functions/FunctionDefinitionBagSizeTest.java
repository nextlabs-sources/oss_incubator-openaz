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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.pdp.policy.Bag;
import org.apache.openaz.xacml.pdp.policy.ExpressionResult;
import org.apache.openaz.xacml.pdp.policy.FunctionArgument;
import org.apache.openaz.xacml.pdp.policy.FunctionArgumentBag;
import org.apache.openaz.xacml.pdp.std.StdFunctions;
import org.apache.openaz.xacml.pdp.std.functions.FunctionDefinitionBagSize;
import org.apache.openaz.xacml.std.StdAttributeValue;
import org.apache.openaz.xacml.std.datatypes.DataTypes;
import org.junit.Test;

/**
 * Test of PDP Functions (See XACML core spec section A.3) TO RUN - use jUnit In Eclipse select this file or
 * the enclosing directory, right-click and select Run As/JUnit Test
 */
public class FunctionDefinitionBagSizeTest {

    /*
     * variables useful in the following tests
     */
    List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();

    @Test
    public void testString() {
        String v1 = new String("abc");
        String v2 = new String("def");
        Integer vOtherType = new Integer(11);

        Bag bag0 = new Bag();
        Bag bag1 = new Bag();
        bag1.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));
        Bag bag2 = new Bag();
        bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));
        bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v2));
        Bag bagOtherType = new Bag();
        bagOtherType.add(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), vOtherType));

        FunctionArgumentBag attrBag0 = new FunctionArgumentBag(bag0);
        FunctionArgumentBag attrBag1 = new FunctionArgumentBag(bag1);
        FunctionArgumentBag attrBag2 = new FunctionArgumentBag(bag2);
        FunctionArgumentBag attrBagOtherType = new FunctionArgumentBag(bagOtherType);

        FunctionDefinitionBagSize<?> fd = (FunctionDefinitionBagSize<?>)StdFunctions.FD_STRING_BAG_SIZE;

        // check identity and type of the thing created
        assertEquals(XACML3.ID_FUNCTION_STRING_BAG_SIZE, fd.getId());
        assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());

        // just to be safe... If tests take too long these can probably be eliminated
        assertFalse(fd.returnsBag());

        // bag with only one
        arguments.clear();
        arguments.add(attrBag1);
        ExpressionResult res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        assertEquals(BigInteger.class, res.getValue().getValue().getClass());
        BigInteger resValue = (BigInteger)res.getValue().getValue();
        assertEquals(BigInteger.valueOf(1), resValue);

        // null bag
        arguments.clear();
        arguments.add(null);
        res = fd.evaluate(null, arguments);
        assertFalse(res.isOk());
        assertEquals("function:string-bag-size Got null argument", res.getStatus().getStatusMessage());
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode()
            .getStatusCodeValue().stringValue());

        // bag with exactly one but of other type in it
        arguments.clear();
        arguments.add(attrBagOtherType);
        res = fd.evaluate(null, arguments);
        // NOTE: Size does not care about content type!
        assertTrue(res.isOk());
        assertEquals(BigInteger.class, res.getValue().getValue().getClass());
        resValue = (BigInteger)res.getValue().getValue();
        assertEquals(BigInteger.valueOf(1), resValue);

        // bag with none
        arguments.clear();
        arguments.add(attrBag0);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        assertEquals(BigInteger.class, res.getValue().getValue().getClass());
        resValue = (BigInteger)res.getValue().getValue();
        assertEquals(BigInteger.valueOf(0), resValue);

        // bag with multiple
        arguments.clear();
        arguments.add(attrBag2);
        res = fd.evaluate(null, arguments);
        assertTrue(res.isOk());
        assertEquals(BigInteger.class, res.getValue().getValue().getClass());
        resValue = (BigInteger)res.getValue().getValue();
        assertEquals(BigInteger.valueOf(2), resValue);
    }

    //
    //
    // REST OF DATA TYPES OMITTED
    // because they "should" all work the same
    //
    //

}
