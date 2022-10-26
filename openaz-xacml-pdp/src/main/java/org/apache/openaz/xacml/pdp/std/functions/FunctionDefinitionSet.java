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
package org.apache.openaz.xacml.pdp.std.functions;

import java.util.ArrayList;
import java.util.List;

import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.DataType;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.pdp.eval.EvaluationContext;
import org.apache.openaz.xacml.pdp.policy.Bag;
import org.apache.openaz.xacml.pdp.policy.ExpressionResult;
import org.apache.openaz.xacml.pdp.policy.FunctionArgument;
import org.apache.openaz.xacml.std.StdStatus;
import org.apache.openaz.xacml.std.StdStatusCode;

/**
 * FunctionDefinitionSet implements {@link org.apache.openaz.xacml.pdp.policy.FunctionDefinition} to
 * implement the XACML Set predicates as functions taking two arguments of <code>Bag</code> the same primitive
 * type and returning either a <code>Boolean</code> or a <code>Bag</code> of the same primitive type.
 * <P>
 * The ipAddress, dnsName and xPathExpression do not have set functions defined for them in section 10.2.8 of
 * the Release 3 XACML spec. In the first implementation of XACML we had separate files for each XACML
 * Function. This release combines multiple Functions in fewer files to minimize code duplication. This file
 * supports the following XACML codes: string-bag boolean-bag integer-bag double-bag time-bag date-bag
 * dateTime-bag anyURI-bag hexBinary-bag base64Binary-bag dayTimeDuration-bag (version 1 and3)
 * yearMonthDuration-bag (version 1 and 3) x500Name-bag rfc822Name-bag
 *
 * @param <I> the java class for the data type of the function Input arguments
 * @param <O> the java class for the data type of the function Output
 */
public class FunctionDefinitionSet<O, I> extends FunctionDefinitionBase<O, I> {

    /**
     * List of comparison operations.
     */
    public enum OPERATION {
        INTERSECTION,
        AT_LEAST_ONE_MEMBER_OF,
        UNION,
        SUBSET,
        SET_EQUALS
    };

    // the operation for this instance of the class
    private OPERATION operation;

    /**
     * Constructor - need dataType input because of java Generic type-erasure during compilation.
     *
     * @param idIn
     * @param dataTypeArgsIn
     */
    public FunctionDefinitionSet(Identifier idIn, DataType<O> dataTypeIn, DataType<I> dataTypeArgsIn,
                                 OPERATION opIn) {
        super(idIn, dataTypeIn, dataTypeArgsIn, opIn == OPERATION.INTERSECTION || opIn == OPERATION.UNION
            ? true : false);
        operation = opIn;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

        if (arguments == null || arguments.size() != 2) {
            return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR,
                                                           this.getShortFunctionId()
                                                               + " Expected 2 arguments, got "
                                                               + ((arguments == null) ? "null" : arguments
                                                                   .size())));
        }

        // get first bag
        FunctionArgument bagArgument = arguments.get(0);
        ConvertedArgument<Bag> convertedBagArgument = new ConvertedArgument<Bag>(bagArgument, null, true);

        if (!convertedBagArgument.isOk()) {
            return ExpressionResult.newError(getFunctionStatus(convertedBagArgument.getStatus()));
        }

        Bag bag1 = convertedBagArgument.getBag();
        List<AttributeValue<?>> list1 = bag1.getAttributeValueList();

        // get second bag
        bagArgument = arguments.get(1);
        convertedBagArgument = new ConvertedArgument<Bag>(bagArgument, null, true);

        if (!convertedBagArgument.isOk()) {
            return ExpressionResult.newError(getFunctionStatus(convertedBagArgument.getStatus()));
        }

        Bag bag2 = convertedBagArgument.getBag();
        List<AttributeValue<?>> list2 = bag2.getAttributeValueList();

        // arguments are ready BUT they have NOT had duplicates removed

        ExpressionResult expressionResult = null;

        // some functions return a bag rather than boolean
        Bag outBag;
        List<AttributeValue<?>> outList;

        switch (operation) {
        case INTERSECTION:
            outList = new ArrayList<AttributeValue<?>>();

            for (AttributeValue<?> element : list1) {
                if (outList.contains(element)) {
                    continue;
                }
                if (list2.contains(element)) {
                    outList.add(element);
                }
            }

            // now have the intersection; put it in a bag

            outBag = new Bag();

            for (AttributeValue<?> element : outList) {
                outBag.add(element);
            }

            expressionResult = ExpressionResult.newBag(outBag);
            return expressionResult;

        case AT_LEAST_ONE_MEMBER_OF:
            // look for elements from the first list in the second.
            // duplicates do not matter because if the element is not there it does not matter that we look
            // for it again,
            // and if it is there we stop the first time we see it.
            // If the first bag is empty, this should fail because no element from the first set can be found
            // in the second set
            // (because there IS no element in first set).
            for (AttributeValue<?> element : list1) {
                if (list2.contains(element)) {
                    return ER_TRUE;
                }
            }
            // did not find any element from list 1 in list 2
            return ER_FALSE;

        case UNION:
            outList = new ArrayList<AttributeValue<?>>();

            for (AttributeValue<?> element : list1) {
                if (outList.contains(element)) {
                    continue;
                }
                outList.add(element);
            }
            for (AttributeValue<?> element : list2) {
                if (outList.contains(element)) {
                    continue;
                }
                outList.add(element);
            }

            // now have the intersection; put it in a bag

            outBag = new Bag();

            for (AttributeValue<?> element : outList) {
                outBag.add(element);
            }

            expressionResult = ExpressionResult.newBag(outBag);
            return expressionResult;

        case SUBSET:
            // all elements from list 1 must exist in list 2.
            // duplicates do not matter because if an element is not found the first time we stop immediately,
            // and if it is found the first time it will also be found for the duplicate.
            // If the first set is empty we return TRUE because all elements (i.e. none) in the first set are
            // in the second.
            for (AttributeValue<?> element : list1) {
                if (!list2.contains(element)) {
                    return ER_FALSE;
                }
            }
            // all elements in list1 were found
            return ER_TRUE;

        case SET_EQUALS:
            // we cannot do a direct one-to-one compare because the lists may contain duplicates. Also they
            // may not be ordered the same.
            // So we ask:
            // are all elements in list 1 in list 2 (ignoring duplicates)
            // are all elements in list 2 in list 1 (ignoring duplicates)
            for (AttributeValue<?> element : list1) {
                if (!list2.contains(element)) {
                    return ER_FALSE;
                }
            }
            for (AttributeValue<?> element : list2) {
                if (!list1.contains(element)) {
                    return ER_FALSE;
                }
            }
            // all elements in each are part of the other
            return ER_TRUE;
        }

        // all cases should have been covered by above - should never get here
        return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this
            .getShortFunctionId() + " Could not evaluate Set function " + operation));

    }

}
