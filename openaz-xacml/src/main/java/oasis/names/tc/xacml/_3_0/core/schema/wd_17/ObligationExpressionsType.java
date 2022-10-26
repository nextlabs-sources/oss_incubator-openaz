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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.05.23 at 02:47:00 PM EDT
//

package oasis.names.tc.xacml._3_0.core.schema.wd_17;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ObligationExpressionsType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ObligationExpressionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}ObligationExpression" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObligationExpressionsType", propOrder = {
                                                          "obligationExpression"
})
public class ObligationExpressionsType {

    @XmlElement(name = "ObligationExpression", required = true)
    protected List<ObligationExpressionType> obligationExpression;

    /**
     * Gets the value of the obligationExpression property.
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification
     * you make to the returned list will be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the obligationExpression property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getObligationExpression().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionType }
     */
    public List<ObligationExpressionType> getObligationExpression() {
        if (obligationExpression == null) {
            obligationExpression = new ArrayList<ObligationExpressionType>();
        }
        return this.obligationExpression;
    }

}
