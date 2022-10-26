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
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.api.Status;
import org.apache.openaz.xacml.api.XACML;
import org.apache.openaz.xacml.pdp.eval.EvaluationContext;
import org.apache.openaz.xacml.pdp.policy.ExpressionResult;
import org.apache.openaz.xacml.pdp.policy.FunctionArgument;
import org.apache.openaz.xacml.std.StdAttributeValue;
import org.apache.openaz.xacml.std.StdStatusCode;
import org.apache.openaz.xacml.std.datatypes.DataTypeString;
import org.apache.openaz.xacml.std.datatypes.DataTypes;

/**
 * FunctionDefinitionStringNormalize extends
 * {@link org.apache.openaz.xacml.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to implement the
 * XACML String normalization predicates as functions taking one <code>String</code> arg and returning a
 * single value of the same type. In the first implementation of XACML we had separate files for each XACML
 * Function. This release combines multiple Functions in fewer files to minimize code duplication. This file
 * supports the following XACML codes: string-normalize-space string-normalize-to-lower-case
 */
public class FunctionDefinitionStringNormalize extends FunctionDefinitionHomogeneousSimple<String, String> {

    /**
     * List of string normalization operations.
     */
    public enum OPERATION {
        SPACE,
        LOWER_CASE
    };

    // operation to be used in this instance of the Arightmetic class
    private final OPERATION operation;

    // result variables used by all functions
    AttributeValue<String> result;

    /**
     * Constructor
     *
     * @param idIn
     * @param dataTypeArgsIn
     * @param op
     */
    public FunctionDefinitionStringNormalize(Identifier idIn, OPERATION op) {
        // for Arithmetic functions, the output type is the same as the input type (no mixing of Ints and
        // Doubles!)
        super(idIn, DataTypes.DT_STRING, DataTypeString.newInstance(), 1);

        // save the operation and data type to be used in this instance
        operation = op;

    }

    @Override
    public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
        List<String> convertedArguments = new ArrayList<String>();
        Status status = this.validateArguments(arguments, convertedArguments);

        /*
         * If the function arguments are not correct, just return an error status immediately
         */
        if (!status.getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
            return ExpressionResult.newError(getFunctionStatus(status));
        }

        /*
         * Now perform the requested operation.
         */
        ExpressionResult expressionResult = null;

        switch (operation) {
        case SPACE:
            result = new StdAttributeValue<String>(XACML.ID_DATATYPE_STRING, convertedArguments.get(0).trim());
            break;
        case LOWER_CASE:
            result = new StdAttributeValue<String>(XACML.ID_DATATYPE_STRING, convertedArguments.get(0)
                .toLowerCase());
            break;
        }

        expressionResult = ExpressionResult.newSingle(result);

        return expressionResult;
    }

}
