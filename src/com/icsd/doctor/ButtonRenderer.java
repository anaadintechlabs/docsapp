package com.icsd.doctor;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class ButtonRenderer extends JButton implements TableCellRenderer {

	public ButtonRenderer() {
	    setOpaque(true);
	  }
		public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
					if (isSelected) {
						System.out.println(value);
						setForeground(table.getSelectionForeground());
						setBackground(table.getSelectionBackground());
					} else{
						setForeground(table.getForeground());
						setBackground(UIManager.getColor("Button.background"));
					}
						setText( (value ==null) ? "hjhjhj" : value.toString() );
						return this;
		}

}
