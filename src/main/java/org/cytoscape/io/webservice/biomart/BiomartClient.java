/*
 Copyright (c) 2006, 2007, 2011, The Cytoscape Consortium (www.cytoscape.org)

 This library is free software; you can redistribute it and/or modify it
 under the terms of the GNU Lesser General Public License as published
 by the Free Software Foundation; either version 2.1 of the License, or
 any later version.

 This library is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 documentation provided hereunder is on an "as is" basis, and the
 Institute for Systems Biology and the Whitehead Institute
 have no obligations to provide maintenance, support,
 updates, enhancements or modifications.  In no event shall the
 Institute for Systems Biology and the Whitehead Institute
 be liable to any party for direct, indirect, special,
 incidental or consequential damages, including lost profits, arising
 out of the use of this software and its documentation, even if the
 Institute for Systems Biology and the Whitehead Institute
 have been advised of the possibility of such damage.  See
 the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
*/
package org.cytoscape.io.webservice.biomart;



import javax.naming.ConfigurationException;

import org.cytoscape.io.webservice.TableImportWebServiceClient;
import org.cytoscape.io.webservice.biomart.rest.BiomartRestClient;
import org.cytoscape.io.webservice.biomart.task.ImportTableTask;
import org.cytoscape.io.webservice.biomart.ui.BiomartAttrMappingPanel;
import org.cytoscape.io.webservice.swing.AbstractWebServiceGUIClient;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.task.edit.MapTableToNetworkTablesTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.osgi.framework.ServiceException;


/**
 * BioMart Web Service Client.
 * 
 */
public class BiomartClient extends AbstractWebServiceGUIClient implements TableImportWebServiceClient {
	
	private final CyTableFactory tableFactory;
	private final BiomartRestClient restClient;
	private ImportTableTask importTask;
	private final CyTableManager tableManager;
	private final MapTableToNetworkTablesTaskFactory mapNetworkAttrTF;

	/**
	 * Creates a new Biomart Client object.
	 * 
	 * @throws ServiceException
	 * @throws ConfigurationException
	 */
	public BiomartClient(final String displayName, final String description,
	                     final BiomartRestClient restClient, final CyTableFactory tableFactory,
	                     final CyTableManager tableManager,
						 final BiomartAttrMappingPanel gui,
						 final MapTableToNetworkTablesTaskFactory mapNetworkAttrTF)
	{
		super(restClient.getBaseURL(), displayName, description);

		this.tableFactory         = tableFactory;
		this.restClient           = restClient;
		this.tableManager         = tableManager;
		this.mapNetworkAttrTF     = mapNetworkAttrTF;
		
		this.gui = gui;

		// TODO: set optional parameters (Tunables?)
	}

	public BiomartRestClient getRestClient() {
		return this.restClient;
	}


	@Override
	public TaskIterator createTaskIterator(Object query) {
		if (gui == null)
			throw new IllegalStateException(
					"Could not build query because Query Builder GUI is null.");

		importTask = new ImportTableTask(restClient, (BiomartQuery) query, tableFactory,
		                                  tableManager,mapNetworkAttrTF);

		return new TaskIterator(importTask);
	}
}
