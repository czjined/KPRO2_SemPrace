package cz.uhk.nedomji1;

import cz.uhk.nedomji1.data.People;
import cz.uhk.nedomji1.data.Person;
import cz.uhk.nedomji1.gui.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EvidenceApp {

    public static void main (String[] args) throws IOException {

        final People testPeople = new People();
        final List<Person> finalTestPersonList = testPeople.readPeopleFromCSV("NedoAddressList.csv");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new MainFrame((ArrayList) finalTestPersonList);
                frame.setVisible(true);
            }
        });
    }


}
