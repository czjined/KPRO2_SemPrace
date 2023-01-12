package cz.uhk.nedomji1;

import cz.uhk.nedomji1.data.People;
import cz.uhk.nedomji1.data.Person;
import cz.uhk.nedomji1.gui.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;


public class EvidenceApp {

    public static void main(String[] args) throws IOException {
        final People evidence = new People();
        evidence.createSampleList();
        for (Person osoba: evidence.getPersonList()) {
            System.out.println(osoba);
        }
//        Constructor[] constParam = evidence.getPerson(0).getClass().getConstructors();
//        for (int i = 0; i < constParam.length; i++) {
//            Class[] params = constParam[i].getParameterTypes();
//            if (params.length > 0) {
//                for (int j = 0; j < params.length; j++) {
//                    Field fields[] = params[j].getDeclaredFields();
//                    for (int iField = 0; iField < fields.length; iField++) {
//                        String fieldName = fields[iField].getName();
//                        System.out.println(fieldName);
//                    }
//                }
//            }
//        }




        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new MainFrame((ArrayList) evidence.getPersonList());
                frame.setVisible(true);
            }
        });
    }


}
