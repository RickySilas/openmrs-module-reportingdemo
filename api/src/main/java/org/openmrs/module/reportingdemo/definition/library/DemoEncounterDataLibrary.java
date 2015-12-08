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

import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.encounter.definition.AgeAtEncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.ConvertedEncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class DemoEncounterDataLibrary extends BaseDefinitionLibrary<EncounterDataDefinition> {

    @Override
    public String getKeyPrefix() {
        return "reportingdemo.encounterData.";
    }

	@Override
	public Class<? super EncounterDataDefinition> getDefinitionType() {
		return EncounterDataDefinition.class;
	}

	@DocumentedDefinition(value = "ageAtEncounterDateInYears")
	public EncounterDataDefinition getAgeAtEncounterDateInYears() {
		return convert(new AgeAtEncounterDataDefinition(), new AgeConverter(AgeConverter.YEARS));
	}

    // Convenience methods

    public EncounterDataDefinition convert(EncounterDataDefinition edd, Map<String, String> renamedParameters, DataConverter converter) {
        ConvertedEncounterDataDefinition convertedDefinition = new ConvertedEncounterDataDefinition();
        convertedDefinition.setDefinitionToConvert(ParameterizableUtil.copyAndMap(edd, convertedDefinition, renamedParameters));
        if (converter != null) {
            convertedDefinition.setConverters(Arrays.asList(converter));
        }
        return convertedDefinition;
    }

    public EncounterDataDefinition convert(EncounterDataDefinition pdd, DataConverter converter) {
        return convert(pdd, null, converter);
    }
}
