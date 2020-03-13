package com.icsd.doctor;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTable;

class JTableButtonMouseListener extends MouseAdapter {
    private final JTable table;

    public JTableButtonMouseListener(JTable table) {
        this.table = table;
    }

    public void mouseClicked(MouseEvent e) {
    	System.out.println("INSIDE CLASS");
        int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the coloum of the button
        int row    = e.getY()/table.getRowHeight(); //get the row of the button
        System.out.println("COLUMN"+column+"ROW"+row);
        System.out.println("table.getRowCount()"+table.getRowCount());
        System.out.println("table.getColumnCount()"+table.getColumnCount());
                /*Checking the row or column is valid or not*/
        if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
            Object value = table.getValueAt(row, column);
            System.out.println("INSIDE IF"+value);
            Desktop desktop = Desktop.getDesktop();
            File fl = new File(value.toString());
            if(fl.exists())
				try {
					desktop.open(fl);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            if (value instanceof JButton) {
                /*perform a click event*/
                ((JButton)value).doClick();
                System.out.println("bt clicked"+value.toString());
            }
        }
    }
}
