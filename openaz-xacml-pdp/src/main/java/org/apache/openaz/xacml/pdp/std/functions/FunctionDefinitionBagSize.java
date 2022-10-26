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

import java.math.BigInteger;
import java.util.List;

import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.DataType;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.api.XACML;
import org.apache.openaz.xacml.pdp.eval.EvaluationContext;
import org.apache.openaz.xacml.pdp.policy.Bag;
import org.apache.openaz.xacml.pdp.policy.ExpressionResult;
import org.apache.openaz.xacml.pdp.policy.FunctionArgument;
import org.apache.openaz.xacml.std.StdAttributeValue;
import org.apache.openaz.xacml.std.StdStatus;
import org.apache.openaz.xacml.std.StdStatusCode;
import org.apache.openaz.xacml.std.datatypes.DataTypes;

/**
 * FunctionDefinitionBagSize implements {@link org.apache.openaz.xacml.pdp.policy.FunctionDefinition} to
 * implement the XACML 'type'-bag-size predicates as functions taking one <code>Bag</code> argument and
 * returning an <code>Integer</code> representing the number of elements in the bag. In the first
 * implementation of XACML we had separate files for each XACML Function. This release combines multiple
 * Functions in fewer files to minimize code duplication. This file supports the following XACML codes:
 * string-bag-size boolean-bag-size integer-bag-size double-bag-size time-bag-size date-bag-size
 * dateTime-bag-size anyURI-bag-size hexBinary-bag-size base64Binary-bag-size dayTimeDuration-bag-size
 * (version 1 and3) yearMonthDuration-bag-size (version 1 and 3) x500Name-bag-size rfc822Name-bag-size
 * ipAddress-bag-size dnsName-bag-size
 *
 * @param <I> the java class for the data type of the function Input arguments
 */
public class FunctionDefinitionBagSize<I> extends FunctionDefinitionBase<BigInteger, I> {

    /**
     * Constructor - need dataType input because of java Generic type-erasure during compilation.
     *
     * @param idIn
     * @param dataTypeArgsIn
     */
    public FunctionDefinitionBagSize(Identifier idIn, DataType<I> dataTypeArgsIn) {
        super(idIn, DataTypes.DT_INTEGER, dataTypeArgsIn, false);
    }

    /**
     * Evaluates this <code>FunctionDefinition</code> on the given <code>List</code> of
     * {@link org.apache.openaz.xacml.pdp.policy.FunctionArgument}s.
     *
     * @param evaluationContext the {@link org.apache.openaz.xacml.pdp.eval.EvaluationContext} to use in the
     *            evaluation
     * @param arguments the <code>List</code> of <code>FunctionArgument</code>s for the evaluation
     * @return an {@link org.apache.openaz.xacml.pdp.policy.ExpressionResult} with the results of the call
     */
    @Override
    public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

        if (arguments == null || arguments.size() != 1) {
            return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR,
                                                           this.getShortFunctionId()
                                                               + " Expected 1 argument, got "
                                                               + ((arguments == null) ? "null" : arguments
                                                                   .size())));
        }

        FunctionArgument argument = arguments.get(0);
        ConvertedArgument<Bag> convertedArgument = new ConvertedArgument<Bag>(argument, null, true);

        if (!convertedArgument.isOk()) {
            return ExpressionResult.newError(getFunctionStatus(convertedArgument.getStatus()));
        }

        Bag bag = convertedArgument.getBag();

        if (bag == null) {
            return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this
                .getShortFunctionId() + " Bag is null"));

        }

        // type is correct, so create a wrapper and return it
        AttributeValue<BigInteger> resultAttributeValue = new StdAttributeValue<BigInteger>(
                                                                                            XACML.ID_DATATYPE_INTEGER,
                                                                                            BigInteger
                                                                                                .valueOf(bag
                                                                                                    .size()));
        return ExpressionResult.newSingle(resultAttributeValue);
    }

}
