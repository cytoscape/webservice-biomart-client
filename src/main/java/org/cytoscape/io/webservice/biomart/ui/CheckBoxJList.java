package org.cytoscape.io.webservice.biomart.ui;

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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * <p>
 * This class is based on CheckBoxJList: from SWING HACKS
 * ISBN: 0-596-00907-0
 *     By Joshua Marinacci, Chris Adamson
 * </p>
 *
 * <p>
 *     Customized by Keiichiro Ono
 * </p>
 */
public class CheckBoxJList extends JList implements ListSelectionListener {
	
	private static final long serialVersionUID = 8204477191024405955L;
	
	public static final String LIST_UPDATED = "LIST_UPDATED";

	private HashSet<Integer> selectionCache = new HashSet<Integer>();

	/**
	 * Creates a new CheckBoxJList object.
	 */
	public CheckBoxJList() {
		super();
		setCellRenderer(new CheckBoxListCellRenderer());
		addListSelectionListener(this);
	}
	
	public void setSelectedItems(List<String> selected) {
		ListSelectionListener[] listeners = this.getListSelectionListeners();
		for(ListSelectionListener l :listeners) {
			removeListSelectionListener(l);
		}
		getSelectionModel().clearSelection();
		selectionCache.clear();
		
		for(int i=0; i<this.getModel().getSize(); i++) {
			if(selected.contains(getModel().getElementAt(i))) {
				getSelectionModel().addSelectionInterval(i, i);
				selectionCache.add(i);
			}
		}
		
		for(ListSelectionListener l :listeners) {
			addListSelectionListener(l);
		}
	}

	/**
	 *  Update the list items.
	 */
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		
		if (!lse.getValueIsAdjusting()) {
			removeListSelectionListener(this);

			// remember everything selected as a result of this action
			final HashSet<Integer> newSelections = new HashSet<Integer>();
			final int size = getModel().getSize();

			for (int i = 0; i < size; i++) {
				if (getSelectionModel().isSelectedIndex(i)) {
					newSelections.add(i);
				}
			}

			// turn on everything that was previously selected
			for (Integer index : selectionCache) {
				getSelectionModel().addSelectionInterval(index, index);
			}

			// add or remove the delta
			for (Integer index : newSelections) {
				if (selectionCache.contains(index))
					getSelectionModel().removeSelectionInterval(index, index);
				else
					getSelectionModel().addSelectionInterval(index, index);
			}

			// save selections for next time
			selectionCache.clear();

			for (int i = 0; i < size; i++) {
				if (getSelectionModel().isSelectedIndex(i)) {
					selectionCache.add(i);
				}
			}

			addListSelectionListener(this);
			firePropertyChange(LIST_UPDATED, null, null);
		}
	}

	private final class CheckBoxListCellRenderer extends JComponent implements ListCellRenderer {
		
		private static final long serialVersionUID = 3002151884598694214L;
		
		private final DefaultListCellRenderer defaultComp;
		private final JCheckBox checkbox;
		private final BorderLayout layout = new BorderLayout();

		public CheckBoxListCellRenderer() {
			setLayout(layout);
			defaultComp = new DefaultListCellRenderer();
			checkbox = new JCheckBox();
			add(checkbox, BorderLayout.WEST);
			add(defaultComp, BorderLayout.CENTER);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index,
		                                              boolean isSelected, boolean cellHasFocus) {
			defaultComp.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			checkbox.setSelected(isSelected);
			
			checkbox.setForeground(UIManager.getColor(isSelected ? "Table.selectionForeground" : "Table.foreground"));
			checkbox.setBackground(UIManager.getColor(isSelected ? "Table.selectionBackground" : "Table.background"));
			defaultComp.setForeground(UIManager.getColor(isSelected ? "Table.selectionForeground" : "Table.foreground"));
			defaultComp.setBackground(UIManager.getColor(isSelected ? "Table.selectionBackground" : "Table.background"));

			final Component[] comps = getComponents();
			final int length = comps.length;

			for (int i = 0; i < length; i++) {
				comps[i].setBackground(
						UIManager.getColor(isSelected ? "Table.selectionBackground" : "Table.background"));
				comps[i].setForeground(
						UIManager.getColor(isSelected ? "Table.selectionForeground" : "Table.foreground"));
			}

			return this;
		}
	}
}
