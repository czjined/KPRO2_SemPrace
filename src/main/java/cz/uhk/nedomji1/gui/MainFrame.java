package cz.uhk.nedomji1.gui;


import cz.uhk.nedomji1.data.Person;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {

    Container c = getContentPane();
    AbstractTableModel tableModel;
    ArrayList tabData = new ArrayList();
    String[] header = {"Prijmeni", "Jmeno", "Email", "Telefon"};
    JTable table;

    JButton btnAdd = new JButton("Pridat");
    JButton btnUpdate = new JButton("Update");
    JButton btnDel = new JButton("Smazat");
    JButton btnFilter = new JButton("Filtrovat");


    public MainFrame(ArrayList tabData) throws HeadlessException {
        super("Okno evidence");
        this.tabData = tabData;
        createTabModel(tabData);
        initWindow();
        pack();

    }

    private AbstractTableModel createTabModel(final ArrayList seznam) {
         tableModel = new AbstractTableModel() {
             @Override
             public int getRowCount() {
                 return seznam.size();
             }

             @Override
             public int getColumnCount() {
                 return header.length;
             }

             @Override
             public Object getValueAt(int i, int i1) {
                 Person person = (Person) seznam.get(i);
                 String value = null;
                 switch (i1) {
                     case 3:
                         value = person.getPhoneNumber();
                         break;
                     case 2:
                         value = person.getEmail();
                         break;
                     case 1:
                         value = person.getFirstName();
                         break;
                     case 0:
                         value = person.getLastName();
                         break;
                 }
                 return value;
             }

             @Override
             public String getColumnName(int column) {
                 return header[column];
             }

             @Override
             public Class<?> getColumnClass(int columnIndex) {
                 return super.getColumnClass(columnIndex);
             }

             @Override
             public boolean isCellEditable(int rowIndex, int columnIndex) {
                 return true;
             }

             @Override
             public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                 Person personUpdated = (Person) tabData.get(rowIndex);
                 switch (columnIndex) {
                     case 3:
                         personUpdated.setPhoneNumber((String) aValue);
                         break;
                     case 2:
                         personUpdated.setEmail((String) aValue);
                         break;
                     case 1:
                         personUpdated.setFirstName((String) aValue);
                         break;
                     case 0:
                         personUpdated.setLastName((String) aValue);
                         break;
                 }

                 tabData.set(rowIndex,personUpdated);
                 System.out.println("Edituji personu:  "+personUpdated);
             }
         };



        return tableModel;
    }

    private void initWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200,800));
        setLocationRelativeTo(null);
        JPanel innerPanel = new JPanel(new BorderLayout());
//        innerPanel.setMaximumSize(new Dimension(800,600));
        add(innerPanel,"Center");

        // Severni panel
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.WHITE);
        northPanel.setBorder(BorderFactory.createTitledBorder("North panel"));
        innerPanel.add(northPanel, BorderLayout.NORTH);

        // Jizni panel s tlacitky
        JPanel btPanel = new JPanel();
        btnUpdate.addActionListener(this);
        btPanel.add(btnUpdate);
        btnAdd.addActionListener(this);
        btPanel.add(btnAdd);
        btnDel.addActionListener(this);
        btnDel.setMnemonic('d');
        btPanel.add(btnDel);
        btnFilter.addActionListener(this);
        btPanel.add(btnFilter);
        innerPanel.add(btPanel, BorderLayout.SOUTH);

        // Stredni cast s tabulkou
        table = new JTable(tableModel);
        innerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        /**
         * Listener pro primou editaci poli v tabulce
         */
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    final int row = e.getFirstRow();
                    final int column = e.getColumn();
                    table.setValueAt("VALUE", row, column);

                }
            }
        });

        // Inicializace razeni dle sloupcu
        TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<AbstractTableModel>(tableModel);
        table.setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
    }

    public void actionPerformed(ActionEvent e) {
        int pos;

        if (e.getSource() == btnAdd) {          // pro tlacitko Pridat
            System.out.println("Zmacknuto tlacitko ADD.");
            String prijmeni = JOptionPane.showInputDialog("Zadej prijmeni:");
            String jmeno = JOptionPane.showInputDialog("Zadej jmeno:");
            tabData.add(new Person(jmeno, prijmeni));
            tableModel.fireTableDataChanged();

        } else if (e.getSource() == btnFilter) { // pro tlacitko Filtr
            System.out.println("Zmacknuto tlacitko FILTER.");
            int r = table.getSelectedRow();
            int c = table.getSelectedColumn();
            String text = (String) table.getValueAt(r,c);
                TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<AbstractTableModel>(tableModel);
                table.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter(text));

        } else if (e.getSource() == btnUpdate) {   // pro tlacitko Update
            System.out.println("Zmacknuto tlacitko SORT.");
            TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<AbstractTableModel>(tableModel);
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter(""));

        } else if (e.getSource() == btnDel) {   // pro tlacitko Smazat
            System.out.println("Zmacknuto tlacitko DEL.");
            if ((pos = table.getSelectedRow()) != -1) {
                if (JOptionPane.showConfirmDialog(this,"Opravdu chcete smazat zaznam?","Potvrdte",JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
                {tabData.remove(pos);}
                tableModel.fireTableDataChanged();
            }
        }
    }

}
