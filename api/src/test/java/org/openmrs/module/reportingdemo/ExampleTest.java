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
package org.openmrs.module.reportingdemo;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.contrib.testdata.TestDataManager;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataUtil;
import org.openmrs.module.reporting.data.patient.definition.PatientDataSetDataDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.util.ReportUtil;
import org.openmrs.module.reporting.serializer.ReportingSerializer;
import org.openmrs.module.reportingdemo.definition.data.definition.BmiPatientDataDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Tests that the BmiPatientDataDefinitionEvaluator returns correct results
 */
public class ExampleTest extends BaseReportingDemoTest {

    @Autowired
    TestDataManager data;

    @Autowired @Qualifier("reportingCohortDefinitionService")
    CohortDefinitionService cohortDefinitionService;

    @Test
    public void shouldRetrieveAllPatientsWithEncountersOfAGivenType() throws Exception {

        EncounterType hivFollowup = MetadataUtils.existing(EncounterType.class, MetadataConstants.HIV_FOLLOWUP_ENCOUNTER);
        EncounterType hivInitial = MetadataUtils.existing(EncounterType.class, MetadataConstants.HIV_INITIAL_ENCOUNTER);

        EvaluationContext context = new EvaluationContext();

        // Show for a single encounter type

        EncounterCohortDefinition ecd = new EncounterCohortDefinition();
        ecd.setTimeQualifier(TimeQualifier.ANY);
        ecd.addEncounterType(hivFollowup);

        Cohort cohort = cohortDefinitionService.evaluate(ecd, context);

        // Show how this differs for another encounter type

        System.out.println("Followup Cohort has size: " + cohort.size());

        ecd.setEncounterTypeList(Arrays.asList(hivInitial));
        cohort = cohortDefinitionService.evaluate(ecd, context);

        System.out.println("Initial Cohort has size: " + cohort.size());

        // Now lets parameterize this

        ecd.setEncounterTypeList(null);
        ecd.addParameter(new Parameter("encounterTypeList", "Encounter Types", List.class));
        ecd.addParameter(new Parameter("onOrAfter", "On or after", Date.class));
        ecd.addParameter(new Parameter("onOrBefore", "On or before", Date.class));

        context.addParameterValue("encounterTypeList", Arrays.asList(hivFollowup));

        for (int i=1; i<=11; i++) {
            Date startDate = DateUtil.getDateTime(2015, i, 1);
            Date endDate = DateUtil.getDateTime(2015, i+1, 1);
            context.addParameterValue("onOrAfter", startDate);
            context.addParameterValue("onOrBefore", endDate);
            cohort = cohortDefinitionService.evaluate(ecd, context);
            System.out.println("Parameterized Followup Cohort (2015-" + i + ") has size: " + cohort.size());
        }
    }

    @Test
    public void shouldTestSerializedReport() throws Exception {

        String reportXml = ReportUtil.readStringFromResource("org/openmrs/module/reportingdemo/test-serialized-report.xml");

        ReportingSerializer serializer = new ReportingSerializer();
        ReportData reportData = serializer.deserialize(reportXml, ReportData.class);
    }

}
