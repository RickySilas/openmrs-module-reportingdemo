package org.openmrs.module.reportingdemo;

import org.junit.Test;
import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public class ExampleTest extends BaseReportingDemoTest {

    @Test
    public void demonstrateBuiltInCohortDefinitions() throws Exception {

        int startingAge = 0;
        int ageRange = 10;
        int maxAge = 80;

        EvaluationContext context = new EvaluationContext();

        for (int i=startingAge; i<=maxAge; i+=ageRange) {
            int ageFrom = startingAge;
            int ageTo = startingAge + ageRange - 1;

            AgeCohortDefinition ageCohort = new AgeCohortDefinition();
            ageCohort.setMinAge(ageFrom);
            ageCohort.setMaxAge(ageTo);


        }


    }


}
