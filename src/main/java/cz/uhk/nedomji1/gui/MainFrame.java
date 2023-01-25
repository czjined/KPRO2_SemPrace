package cz.uhk.nedomji1.gui;


import cz.uhk.nedomji1.data.People;
import cz.uhk.nedomji1.data.Person;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {

    private String dataFileCSV;
    Container c = getContentPane();
    AbstractTableModel tableModel;
    List<Person> tabData = new ArrayList();
    String[] header = {"Prijmeni", "Jmeno", "Email", "Telefon"};
    JTable table;

    JButton btnAdd = new JButton("Pridat");
    JButton btnUpdate = new JButton("Update");
    JButton btnDel = new JButton("Smazat");
    JButton btnFilter = new JButton("Filtrovat");
    JButton btnFind = new JButton("Hledat");


    public MainFrame() throws HeadlessException {
        super("Seznam osob");

        // Prvotni nacteni seznamu osob
        // TODO vylepseni: otevirat aplikaci s naposledy otevrenym seznamem - File In/Out Stream to bin?
        dataFileCSV = "SampleAddressList.csv";
        People testPeople = new People();
        tabData = testPeople.readPeopleFromCSV(dataFileCSV);

        // Vykresleni tabulky a inicializace
        createTabModel((ArrayList) tabData);
        initWindow();
        pack();

    }

    // TODO vylepseni: Table model jako samostatna ext. trida
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
//                 int modelRow = table.convertRowIndexToModel(rowIndex);         // Zde nefunguje!
//                 int modelColumn = table.convertColumnIndexToModel(columnIndex);
                Person personUpdated = tabData.get(rowIndex);
                System.out.println("Budu upravovat personu:  " + tabData.get(rowIndex));
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
                System.out.println("Zadana persona:  " + personUpdated);
                tabData.set(rowIndex, personUpdated);
                System.out.println("Vysledna model persona:  " + tabData.get(rowIndex));
            }
        };


        return tableModel;
    }

    private void initWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);
        JPanel innerPanel = new JPanel(new BorderLayout());
        add(innerPanel, "Center");

        createMenu();

        // Severni panel - vypis otevreneho datoveho souboru
        JPanel northPanel = new JPanel();
        JTextArea txtNadpis = new JTextArea(dataFileCSV);
        txtNadpis.setEditable(false);
        northPanel.setBackground(Color.WHITE);
//        northPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        northPanel.add(txtNadpis);
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
        btnFind.addActionListener(this);
        btPanel.add(btnFind);
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
                if (e.getType() == TableModelEvent.INSERT) {
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
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }

    public void actionPerformed(ActionEvent e) {
        int pos;

        if (e.getSource() == btnAdd) {          // pro tlacitko Pridat
            System.out.println("Zmacknuto tlacitko ADD.");
            String prijmeni = JOptionPane.showInputDialog("Zadej prijmeni:");
            String jmeno = JOptionPane.showInputDialog("Zadej jmeno:");
            // TODO vylepseni: Zadavat vse v jednom dialogu s vice txt poli.
            tabData.add(new Person(jmeno, prijmeni));
            tableModel.fireTableDataChanged();
            // TODO chyba: nezobrazuje se pridana osoba, prestoze je v modelu - ??
        } else if (e.getSource() == btnFilter) { // pro tlacitko Filtr
            System.out.println("Zmacknuto tlacitko FILTER.");
            int r = table.getSelectedRow();
            int c = table.getSelectedColumn();
            if (r != -1) {
                String text = (String) table.getValueAt(r, c);
                TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<AbstractTableModel>(tableModel);
                table.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter(text));
                System.out.println("ModelRow 1. radku: " + table.convertRowIndexToModel(0));
            }
        } else if (e.getSource() == btnFind) { // pro tlacitko Hledat
            System.out.println("Zmacknuto tlacitko HLEDAT.");
            String findText = JOptionPane.showInputDialog("Hledany text:");
            TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<AbstractTableModel>(tableModel);
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter(findText));
//            System.out.println("ModelRow 1. radku: " + table.convertRowIndexToModel(1));

        } else if (e.getSource() == btnUpdate) {   // pro tlacitko Update
            System.out.println("Zmacknuto tlacitko SORT.");
            TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<AbstractTableModel>(tableModel);
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter(""));

        } else if (e.getSource() == btnDel) {   // pro tlacitko Smazat
            System.out.println("Zmacknuto tlacitko DEL.");
            if ((pos = table.getSelectedRow()) != -1) {
                if (JOptionPane.showConfirmDialog(this, "Opravdu chcete smazat zaznam?", "Potvrdte", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    tabData.remove(pos);
                }
                tableModel.fireTableDataChanged();
            }
        }
    }

    /**
     * Vytvari a inicializuje menu
     */
    private void createMenu() {
        JMenuBar bar = new JMenuBar();

        //File menu
        JMenu fileMenu = new JMenu("Soubor");
        fileMenu.setMnemonic('S');

        final JMenuItem addNew = new JMenuItem("Novy");
        final JMenuItem addOpen = new JMenuItem("Otevri");
        final JMenuItem addSave = new JMenuItem("Uloz");
        // TODO vylepseni: Bylo by dobre mit i ikony - neni cas  :o(


        fileMenu.add(addNew);
        fileMenu.addSeparator();
        fileMenu.add(addOpen);
        fileMenu.addSeparator();
        fileMenu.add(addSave);

        //Program menu
        JMenu appMenu = new JMenu("Program");
        appMenu.setMnemonic('P');
        final JMenuItem addAbout = new JMenuItem("O programu");
        final JMenuItem addExit = new JMenuItem("Ukoncit");

        appMenu.add(addAbout);
        appMenu.addSeparator();
        appMenu.add(addExit);


        bar.add(fileMenu);
        bar.add(appMenu);

        setJMenuBar(bar);


        // Action Listeners pro funkce v MENU

        addNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Stisknuto v Menu SOUBOR ->  " + e.getActionCommand());
                String fileName = JOptionPane.showInputDialog(c, "Jmeno noveho CSV souboru?\n" +
                        "(bez diakritiky, vcetne cele cesty)");
                if (createFile(fileName)) {
                    dataFileCSV = fileName;
                    tabData.clear();
                    // TODO chyba: Neproveden update textu v NORTH panelu!
                    tableModel.fireTableDataChanged();
                }
            }
        });

        addOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Stisknuto v Menu SOUBOR ->  " + e.getActionCommand());
                People testPeople = new People();
                dataFileCSV = JOptionPane.showInputDialog(c, "Jmeno souboru?\n" +
                        "... vcetne cele cesty, pokud neni v adresari projektu");
                // TODO vylepseni: Krasne by bylo vybirat primo z file manageru!
                tabData.clear();
                tabData = testPeople.readPeopleFromCSV(dataFileCSV);
                System.out.println("Nacteno " + tabData.size() + "osob v novem modelu z " + dataFileCSV);
                tableModel.fireTableStructureChanged();
                tableModel.fireTableDataChanged();
                // TODO chyba: nezobrazuji se nova data!
            }
        });

        addSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Stisknuto v Menu SOUBOR ->  " + e.getActionCommand());
                People testPeople = new People();
                testPeople.writePeopleToCSV(tabData, dataFileCSV);
            }
        });

        addAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Stisknuto v Menu SOUBOR ->  " + e.getActionCommand());
                // TODO vylepseni: Na tyto dialogy vytvorit metodu/tridu s parametry textu?
                JOptionPane.showMessageDialog(c,
                        "P E R S O N A L  M A N A G E R\n(C) Jiri Nedomlel, FIM UHK",
                        "O programu",
                        JOptionPane.INFORMATION_MESSAGE + JOptionPane.OK_OPTION
                );
            }
        });

        addExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Stisknuto v Menu SOUBOR ->  " + e.getActionCommand());
                System.exit(0);
            }
        });

    }

    private boolean createFile(String fileName) {

        File file = new File(fileName); //initialize File object and passing path as argument
        boolean result = false;
        try {
            result = file.createNewFile();  //creates a new file
            if (result)      // test if successfully created a new file
            {
                System.out.println("file created " + file.getCanonicalPath()); //returns the path string
            } else {
                System.out.println("File already exist at location: " + file.getCanonicalPath());
                JOptionPane.showConfirmDialog(c, "Tento soubor jiz existuje.", "Upozorneni", JOptionPane.OK_OPTION);
            }
        } catch (IOException e) {
            e.printStackTrace();    //prints exception if any
        }
        return result;
    }
}


