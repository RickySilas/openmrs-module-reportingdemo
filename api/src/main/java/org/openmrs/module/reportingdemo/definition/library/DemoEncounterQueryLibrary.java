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

import org.openmrs.EncounterType;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.query.encounter.definition.BasicEncounterQuery;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
import org.openmrs.module.reporting.query.encounter.definition.MappedParametersEncounterQuery;
import org.openmrs.module.reportingdemo.MetadataConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DemoEncounterQueryLibrary extends BaseDefinitionLibrary<EncounterQuery> {

    @Override
    public String getKeyPrefix() {
        return "reportingdemo.encounterQuery.";
    }

	@Override
	public Class<? super EncounterQuery> getDefinitionType() {
		return EncounterQuery.class;
	}

    @DocumentedDefinition
	public EncounterQuery getHivEncountersAtLocationDuringPeriod() {
		BasicEncounterQuery q = new BasicEncounterQuery();
        q.setEncounterTypes(getHivEncounterTypes());
		q.addParameter(new Parameter("onOrAfter", "On or after", Date.class));
		q.addParameter(new Parameter("onOrBefore", "On or before", Date.class));
		q.addParameter(new Parameter("locationList", "Locations", Date.class));
		return new MappedParametersEncounterQuery(q, ObjectUtil.toMap("onOrAfter=startDate,onOrBefore=endDate,locationList=location"));
	}

    // Convenience methods

    protected List<EncounterType> getHivEncounterTypes() {
        List<EncounterType> l = new ArrayList<EncounterType>();
        l.add(MetadataUtils.existing(EncounterType.class, MetadataConstants.HIV_INITIAL_ENCOUNTER));
        l.add(MetadataUtils.existing(EncounterType.class, MetadataConstants.HIV_FOLLOWUP_ENCOUNTER));
        return l;
    }
}
