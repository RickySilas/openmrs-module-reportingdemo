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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.contrib.testdata.TestDataManager;
import org.openmrs.contrib.testdata.builder.PatientBuilder;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.data.DataUtil;
import org.openmrs.module.reporting.data.patient.service.PatientDataService;
import org.openmrs.module.reportingdemo.BaseReportingDemoTest;
import org.openmrs.module.reportingdemo.MetadataConstants;
import org.openmrs.module.reportingdemo.definition.data.definition.BmiPatientDataDefinition;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests that the BmiPatientDataDefinitionEvaluator returns correct results
 */
public class BmiPatientDataEvaluatorTest extends BaseReportingDemoTest {

    @Autowired
    TestDataManager data;

    @Test
    public void shouldCorrectlyComputeBmi() throws Exception {

        // We can use the openmrs-contrib-testutils library to easily create test data to validate against
        Patient p1 = data.randomPatient().save();

        // We can use the metadatadeploy tools to retrive existing metadata from our database by uuid
        Concept weightConcept = MetadataUtils.existing(Concept.class, MetadataConstants.WEIGHT);
        Concept heightConcept = MetadataUtils.existing(Concept.class, MetadataConstants.HEIGHT);

        Double wt1 = 50.0; // This is kg
        Double ht1 = 140.0;  // This is cm
        Double expectedBmi1 = wt1/(Math.pow(ht1/100, 2));

        Obs o1 = data.obs().person(p1).concept(weightConcept).value(wt1).obsDatetime(DateUtil.getDateTime(2015, 6, 1)).save();
        Obs o2 = data.obs().person(p1).concept(heightConcept).value(ht1).obsDatetime(DateUtil.getDateTime(2015, 6, 1)).save();

        BmiPatientDataDefinition bmiDefinition = new BmiPatientDataDefinition();

        Double actualBmi1 = DataUtil.evaluateForPatient(bmiDefinition, p1.getId(), Double.class);

        Assert.assertEquals(expectedBmi1, actualBmi1);
    }

}
