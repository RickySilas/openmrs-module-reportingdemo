/*
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
package org.openmrs.module.reportingdemo.reports;

import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.SortCriteria.SortDirection;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.library.BuiltInPatientDataLibrary;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.BaseReportManager;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.module.reportingdemo.definition.library.DemoCohortDefinitionLibrary;
import org.openmrs.module.reportingdemo.definition.library.DemoPatientDataLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HivProgramRegister extends BaseReportManager {

    @Autowired
    private BuiltInPatientDataLibrary builtInPatientData;

    @Autowired
    private DemoPatientDataLibrary demoPatientData;

	@Autowired
	private DemoCohortDefinitionLibrary demoCohortDefinitions;

	public HivProgramRegister() {}

	@Override
	public String getUuid() {
		return "eb6c9c1f-62f7-49fc-a8fd-748e77b9f906";
	}

	@Override
	public String getName() {
		return "HIV Program Register";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> l = new ArrayList<Parameter>();
		l.add(ReportingConstants.END_DATE_PARAMETER);
		return l;
	}

	@Override
	public ReportDefinition constructReportDefinition() {

		ReportDefinition rd = new ReportDefinition();
		rd.setUuid(getUuid());
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.setParameters(getParameters());

		PatientDataSetDefinition dsd = new PatientDataSetDefinition();
		dsd.setName(getName());
		dsd.addParameters(getParameters());
		dsd.addSortCriteria("HCC #", SortDirection.ASC);

		rd.addDataSetDefinition("patients", Mapped.mapStraightThrough(dsd));

		// Rows are defined as all patients who ever have been in PRE-ART or Exposed Child
		CohortDefinition baseCohort = demoCohortDefinitions.getEverEnrolledInHivProgramByEndDate();
		dsd.addRowFilter(Mapped.mapStraightThrough(baseCohort));

		// Columns to include

		addColumn(dsd, "PID", builtInPatientData.getPatientId());
		addColumn(dsd, "OMRS_ID", demoPatientData.getOpenmrsId());
		addColumn(dsd, "GIVEN_NAME", builtInPatientData.getPreferredGivenName());
		addColumn(dsd, "LAST_NAME", builtInPatientData.getPreferredFamilyName());
		addColumn(dsd, "BIRTHDATE", demoPatientData.getBirthdate());
		addColumn(dsd, "AGE_YRS", demoPatientData.getAgeAtEndInYears());
		addColumn(dsd, "AGE_MTHS", demoPatientData.getAgeAtEndInMonths());
		addColumn(dsd, "GENDER", builtInPatientData.getGender());
		addColumn(dsd, "CITY", demoPatientData.getCity());
        addColumn(dsd, "WT", demoPatientData.getLatestWeight());
        addColumn(dsd, "HT", demoPatientData.getLatestHeight());
        addColumn(dsd, "BMI", demoPatientData.getBmi());

		return rd;
	}

    @Override
    public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
        List<ReportDesign> l = new ArrayList<ReportDesign>();
        l.add(ReportManagerUtil.createExcelDesign("ae928860-4a4e-48d4-bbc2-50902babcfc0", reportDefinition));
        return l;
    }

	@Override
	public String getVersion() {
		return "1.0";
	}

    protected void addColumn(PatientDataSetDefinition dsd, String columnName, PatientDataDefinition pdd) {
        dsd.addColumn(columnName, pdd, Mapped.straightThroughMappings(pdd));
    }

}
