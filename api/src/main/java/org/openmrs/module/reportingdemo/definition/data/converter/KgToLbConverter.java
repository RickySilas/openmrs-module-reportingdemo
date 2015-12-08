/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.reportingdemo.definition.data.converter;

import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Example converter that changes a weight from kilograms to pounds
 */
public class KgToLbConverter implements DataConverter  {

	//***** CONSTRUCTORS *****

	/**
	 * Default constructor
	 */
	public KgToLbConverter() { }

	//***** INSTANCE METHODS *****

	/**
	 * @see DataConverter#convert(Object)
	 * @should convert a PatientIdentifier to a PIH Malawi standard representation
	 */
	public Object convert(Object original) {
		Double kgs = (Double)original;
		if (kgs != null) {
            return kgs * 2.20462;
		}
		return null;
	}

    /**
     * @see DataConverter#getInputDataType()
     */
    public Class<?> getInputDataType() {
        return Double.class;
    }

	/**
	 * @see DataConverter#getDataType()
	 */
	public Class<?> getDataType() {
		return Double.class;
	}
}