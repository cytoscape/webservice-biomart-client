package org.cytoscape.io.webservice.biomart.task;

import java.awt.Window;

import javax.swing.SwingUtilities;

import org.cytoscape.io.webservice.biomart.ui.BiomartAttrMappingPanel;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class ShowBiomartDialogTask extends AbstractTask {

	private static final Logger logger = LoggerFactory.getLogger(ShowBiomartDialogTask.class);

	private final BiomartAttrMappingPanel panel;
	private final LoadRepositoryTask loadTask;

	public ShowBiomartDialogTask(final BiomartAttrMappingPanel panel, final LoadRepositoryTask loadTask) {
		this.panel = panel;
		this.loadTask = loadTask;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		final LoadRepositoryResult result = loadTask.getResult();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panel.initDataSources(result);
				logger.info("BioMart Client initialized.");
				((Window)panel.getRootPane().getParent()).toFront();
			}
		});
	}
}
