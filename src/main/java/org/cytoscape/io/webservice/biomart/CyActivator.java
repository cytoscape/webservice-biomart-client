package org.cytoscape.io.webservice.biomart;

/*
 * #%L
 * Cytoscape Biomart Webservice Impl (webservice-biomart-client-impl)
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2006 - 2013 The Cytoscape Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 2.1 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import java.util.Properties;

import org.cytoscape.io.webservice.biomart.rest.BiomartRestClient;
import org.cytoscape.io.webservice.biomart.ui.BiomartAttrMappingPanel;
import org.cytoscape.io.webservice.swing.WebServiceGUI;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.property.CyProperty;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.edit.ImportDataTableTaskFactory;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CyActivator extends AbstractCyActivator {
	private static final Logger logger = LoggerFactory.getLogger(BiomartRestClient.class);
	
	// Since central server is down, ENSEMBL Mart is the default.
	private static final String MART_URL_PROP_NAME ="biomart.url";
	private static final String DEF_MART ="http://www.ensembl.org/biomart/martservice";
	
	@Override
	public void start(BundleContext bc) {
		// Import services
		CyProperty<Properties> cyProp = getService(bc,CyProperty.class,"(cyPropertyName=cytoscape3.props)");
		CyServiceRegistrar serviceRegistrar = getService(bc, CyServiceRegistrar.class);
		CyTableManager cyTableManagerServiceRef = getService(bc, CyTableManager.class);
		CyTableFactory cyTableFactoryServiceRef = getService(bc, CyTableFactory.class);
		ImportDataTableTaskFactory importAttrTFServiceRef = getService(bc, ImportDataTableTaskFactory.class);

		WebServiceGUI webServiceGUI = getService(bc, WebServiceGUI.class);

		// Export services
		String martUrl = cyProp.getProperties().getProperty(MART_URL_PROP_NAME);
		if(martUrl == null || martUrl.equals("")) {
			martUrl = DEF_MART;
			cyProp.getProperties().setProperty(MART_URL_PROP_NAME, martUrl);
		}
		
		logger.info("Biomart Service URL is: " + martUrl);
		
		BiomartRestClient biomartRestClient = new BiomartRestClient(martUrl);
		BiomartAttrMappingPanel biomartAttrMappingPanel = new BiomartAttrMappingPanel(webServiceGUI, serviceRegistrar);

		BiomartClient biomartClient = new BiomartClient("BioMart Client", "REST version of BioMart Web Service Client.",
				biomartRestClient, cyTableFactoryServiceRef, cyTableManagerServiceRef, biomartAttrMappingPanel,
				importAttrTFServiceRef);
		biomartAttrMappingPanel.setClient(biomartClient);

		registerAllServices(bc, biomartAttrMappingPanel, new Properties());
		registerAllServices(bc, biomartClient, new Properties());
	}
}
