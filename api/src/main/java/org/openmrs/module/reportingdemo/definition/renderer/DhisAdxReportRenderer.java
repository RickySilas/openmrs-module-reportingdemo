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
package org.openmrs.module.reportingdemo.definition.renderer;

import org.openmrs.annotation.Handler;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.renderer.RenderingException;
import org.openmrs.module.reporting.report.renderer.ReportDesignRenderer;
import org.openmrs.module.reporting.report.renderer.ReportRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * ReportRenderer that renders to ADX format suitable for submission to DHIS2
 */
@Handler
@Localized("reportingdemo.DhisXmlReportRenderer")
public class DhisAdxReportRenderer extends ReportDesignRenderer {

	/**
	 * @see ReportRenderer#getFilename(ReportRequest)
	 */
    @Override
	public String getFilename(ReportRequest request) {
		return getFilenameBase(request) + ".xml";
	}

	/**
	 * @see ReportRenderer#getRenderedContentType(ReportRequest)
	 */
	public String getRenderedContentType(ReportRequest request) {
		return "text/xml";
	}
	
	/**
	 * @see ReportRenderer#render(ReportData, String, OutputStream)
	 */
	public void render(ReportData results, String argument, OutputStream out) throws IOException, RenderingException {

        Writer w = new OutputStreamWriter(out, "UTF-8");
        w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        w.write("<adx xmlns=\"urn:ihe:qrph:adx:2015\"\n");

        // TODO: Here, you would actually iterate over the data sets in the report (eg. one per location)
        w.write("\t<group orgUnit=\"BOBS_CLINIC\" period=\"2015-01-01/P1M\" dataSet=\"MALARIA\" mechanism=\"PEPFAR\">\n");

        // TODO: Here, you would actually iterate over the values in the given dataset and populate based on column definition and data value
        w.write("\t\t<dataValue dataElement=\"MAL04\" value=\"10\" ageGroup=\"under5\" sex=\"M\" />\n");
        w.write("\t\t<dataValue dataElement=\"MAL04\" value=\"10\" ageGroup=\"under5\" sex=\"F\"/>\n");

        w.write("\t</group>\n");
        w.write("</adx>");

		w.flush();
	}
}
