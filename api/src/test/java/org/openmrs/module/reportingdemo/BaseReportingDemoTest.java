package org.openmrs.module.reportingdemo;

import org.junit.Before;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;
import org.springframework.test.context.ContextConfiguration;

import java.util.Properties;

@ContextConfiguration(locations = {"classpath:openmrs-servlet.xml"}, inheritLocations = true)
@SkipBaseSetup
public abstract class BaseReportingDemoTest extends BaseModuleContextSensitiveTest {

	@Override
	public Boolean useInMemoryDatabase() {
		return false;
	}

	@Override
	public Properties getRuntimeProperties() {
		Properties p = super.getRuntimeProperties();
        p.setProperty("connection.url", "jdbc:mysql://localhost:3306/openmrs_reportingdemo?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8");
        p.setProperty("connection.username", "openmrs");
        p.setProperty("connection.password", "openmrs");
        p.setProperty("junit.username", "junit");
        p.setProperty("junit.password", "Test123");
		return p;
	}

    @Before
    public void openSessionAndAuthenticate() throws Exception {
        if (!Context.isSessionOpen()) {
            Context.openSession();
        }
        Context.clearSession();
        authenticate();
    }

	@Override
	public void deleteAllData() throws Exception {
	}
}
