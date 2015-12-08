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
package org.openmrs.module.reportingdemo.definition.library;

import org.openmrs.Program;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.MappedParametersCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.ProgramEnrollmentCohortDefinition;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reportingdemo.MetadataConstants;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Defines all of the General Cohort Definition instances we want to expose
 */
@Component
public class DemoCohortDefinitionLibrary extends BaseDefinitionLibrary<CohortDefinition> {

    public static final String PREFIX = "reportingdemo.cohortDefinition.";

    @Override
    public Class<? super CohortDefinition> getDefinitionType() {
        return CohortDefinition.class;
    }

    @Override
    public String getKeyPrefix() {
        return PREFIX;
    }

	@DocumentedDefinition
	public CohortDefinition getChildrenAtEnd() {
		return getAgeByEndDate(null, 14);
	}

	@DocumentedDefinition
	public CohortDefinition getAdultsAtEnd() {
		return getAgeByEndDate(15, null);
	}

    // Convenience methods

    public CohortDefinition getAgeByEndDate(Integer minAge, Integer maxAge) {
        AgeCohortDefinition cd = new AgeCohortDefinition();
        cd.setMinAge(minAge);
        cd.setMaxAge(maxAge);
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        return convert(cd, ObjectUtil.toMap("effectiveDate=endDate"));
    }

    @DocumentedDefinition(value = "enrolledInHivProgramByEndDate")
    public CohortDefinition getEverEnrolledInHivProgramByEndDate() {
        ProgramEnrollmentCohortDefinition cd = new ProgramEnrollmentCohortDefinition();
        cd.setPrograms(Arrays.asList(MetadataUtils.existing(Program.class, MetadataConstants.HIV_PROGRAM)));
        cd.addParameter(new Parameter("enrolledOnOrBefore", "enrolledOnOrBefore", Date.class));
        return convert(cd, ObjectUtil.toMap("enrolledOnOrBefore=endDate"));
    }

    public CohortDefinition convert(CohortDefinition cd, Map<String, String> renamedParameters) {
        return new MappedParametersCohortDefinition(cd, renamedParameters);
    }
}
