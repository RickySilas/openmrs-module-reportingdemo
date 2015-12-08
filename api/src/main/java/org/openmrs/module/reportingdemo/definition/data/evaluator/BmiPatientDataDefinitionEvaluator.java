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
package org.openmrs.module.reportingdemo.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.evaluator.PatientDataEvaluator;
import org.openmrs.module.reporting.data.patient.service.PatientDataService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reportingdemo.definition.data.definition.BmiPatientDataDefinition;
import org.openmrs.module.reportingdemo.definition.library.DemoPatientDataLibrary;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates a BmiDataDefinition to produce a PatientData
 */
@Handler(supports = BmiPatientDataDefinition.class, order = 50)
public class BmiPatientDataDefinitionEvaluator implements PatientDataEvaluator {

	@Autowired
	private PatientDataService patientDataService;

    @Autowired
    DemoPatientDataLibrary demoPatientData;
	
	@Override
	public EvaluatedPatientData evaluate(PatientDataDefinition definition, EvaluationContext context) throws EvaluationException {

		EvaluatedPatientData pd = new EvaluatedPatientData(definition, context);

        Map<Integer, Object> wts = evaluateObs(demoPatientData.getLatestWeight(), context);
        Map<Integer, Object> hts = evaluateObs(demoPatientData.getLatestHeight(), context);

        for (Integer pId : wts.keySet()) {
            Double wt = (Double) wts.get(pId);
            Double ht = (Double) hts.get(pId);
            if (wt != null && ht != null) {
                double bmi = wt/Math.pow(ht/100, 2);
                pd.addData(pId, bmi);
            }
        }

		return pd;
	}

    protected Map<Integer, Object> evaluateObs(PatientDataDefinition pdd, EvaluationContext context) throws EvaluationException {
        return patientDataService.evaluate(pdd, context).getData();
    }
}
