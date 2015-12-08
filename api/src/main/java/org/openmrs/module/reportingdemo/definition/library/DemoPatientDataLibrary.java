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

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAddress;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.common.Birthdate;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.PropertyConverter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PersonToPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PreferredIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ObsForPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredAddressDataDefinition;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reportingdemo.MetadataConstants;
import org.openmrs.module.reportingdemo.definition.data.definition.BmiPatientDataDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Component
public class DemoPatientDataLibrary extends BaseDefinitionLibrary<PatientDataDefinition> {

    @Override
    public String getKeyPrefix() {
        return "reportingdemo.patientData.";
    }

	@Override
	public Class<? super PatientDataDefinition> getDefinitionType() {
		return PatientDataDefinition.class;
	}

	@DocumentedDefinition("birthdate")
	public PatientDataDefinition getBirthdate() {
		return convert(new BirthdateDataDefinition(), new PropertyConverter(Birthdate.class, "birthdate"));
	}

	@DocumentedDefinition("city")
	public PatientDataDefinition getCity() {
        PreferredAddressDataDefinition d = new PreferredAddressDataDefinition();
        PropertyConverter converter = new PropertyConverter(PersonAddress.class, "cityVillage");
        return convert(d, converter);
	}

	// Demographic Data

	@DocumentedDefinition("ageAtEndInYears")
	public PatientDataDefinition getAgeAtEndInYears() {
		return getAgeAtEnd(new AgeConverter(AgeConverter.YEARS));
	}

	@DocumentedDefinition("ageAtEndInMonths")
	public PatientDataDefinition getAgeAtEndInMonths() {
        return getAgeAtEnd(new AgeConverter(AgeConverter.MONTHS));
	}

    @DocumentedDefinition("openmrsId")
    public PatientDataDefinition getOpenmrsId() {
        PreferredIdentifierDataDefinition def = new PreferredIdentifierDataDefinition();
        def.setIdentifierType(MetadataUtils.existing(PatientIdentifierType.class, MetadataConstants.OPENMRS_ID));
        return convert(def, new PropertyConverter(PatientIdentifier.class, "identifier"));
    }

    // Program Details

    // TODO: Add program enrollment date and current status


	// Vitals

    @DocumentedDefinition
    public PatientDataDefinition getLatestHeightObs() {
        return getMostRecentObsByEndDate(MetadataUtils.existing(Concept.class, MetadataConstants.HEIGHT));
    }

	@DocumentedDefinition("latestHeight")
	public PatientDataDefinition getLatestHeight() {
		return convert(getLatestHeightObs(), new PropertyConverter(Obs.class, "valueNumeric"));
	}

    @DocumentedDefinition("latestHeightDate")
    public PatientDataDefinition getLatestHeightDate() {
        return convert(getLatestHeight(), new PropertyConverter(Obs.class, "obsDatetime"));
    }

    @DocumentedDefinition
    public PatientDataDefinition getLatestWeightObs() {
        return getMostRecentObsByEndDate(MetadataUtils.existing(Concept.class, MetadataConstants.WEIGHT));
    }

	@DocumentedDefinition("latestWeight")
	public PatientDataDefinition getLatestWeight() {
		return convert(getLatestWeightObs(), new PropertyConverter(Obs.class, "valueNumeric"));
	}

	@DocumentedDefinition("latestWeight.date")
	public PatientDataDefinition getLatestWeightDate() {
		return convert(getLatestWeightObs(), new PropertyConverter(Obs.class, "obsDatetime"));
	}

    @DocumentedDefinition("bmi")
    public PatientDataDefinition getBmi() {
        return new BmiPatientDataDefinition();
    }

    // Convenience methods

    public PatientDataDefinition getMostRecentObsByEndDate(Concept question) {
        return getMostRecentObsByEndDate(question, null);
    }

    public PatientDataDefinition getMostRecentObsByEndDate(Concept question, DataConverter converter) {
        ObsForPersonDataDefinition def = new ObsForPersonDataDefinition();
        def.setWhich(TimeQualifier.LAST);
        def.setQuestion(question);
        def.addParameter(new Parameter("onOrBefore", "On or Before", Date.class));
        return convert(def, ObjectUtil.toMap("onOrBefore=endDate"), converter);
    }

    protected PatientDataDefinition getAgeAtEnd(DataConverter converter) {
        AgeDataDefinition ageDataDefinition = new AgeDataDefinition();
        ageDataDefinition.addParameter(new Parameter("effectiveDate", "effectiveDate", Date.class));
        return convert(ageDataDefinition, ObjectUtil.toMap("effectiveDate=endDate"), converter);
    }

    public PatientDataDefinition convert(PatientDataDefinition pdd, Map<String, String> renamedParameters, DataConverter converter) {
        ConvertedPatientDataDefinition convertedDefinition = new ConvertedPatientDataDefinition();
        convertedDefinition.setDefinitionToConvert(ParameterizableUtil.copyAndMap(pdd, convertedDefinition, renamedParameters));
        if (converter != null) {
            convertedDefinition.setConverters(Arrays.asList(converter));
        }
        return convertedDefinition;
    }

    public PatientDataDefinition convert(PatientDataDefinition pdd, DataConverter converter) {
        return convert(pdd, null, converter);
    }

    public PatientDataDefinition convert(PersonDataDefinition pdd, Map<String, String> renamedParameters, DataConverter converter) {
        return convert(new PersonToPatientDataDefinition(pdd), renamedParameters, converter);
    }

    public PatientDataDefinition convert(PersonDataDefinition pdd, DataConverter converter) {
        return convert(pdd, null, converter);
    }
}
