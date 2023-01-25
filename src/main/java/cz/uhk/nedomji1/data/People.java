package cz.uhk.nedomji1.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class People {

    private List<Person> personList = new ArrayList<Person>();

    public void People() {
    }

    public void People(List<Person> personList) {
        this.personList = personList;
    }

    public int getListLenght() {
        return personList.size();
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void addPerson(Person person) {
        personList.add(person);
    }

    public Person getPerson(int position) {
        return personList.get(position);
    }

    public void removePerson(int position) {
        personList.remove(position);
    }

    public void removePerson(Person person) {
        personList.remove(person);
    }


    public void writePeopleToCSV(List<Person> listOsob, String filePath) {

        File file = new File(filePath);

        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = (CSVWriter) new CSVWriterBuilder(outputFile)
                    .withSeparator(';')
                    .build();

            // vytvoreni delimited listu pro zapis do file
            List<String[]> entries = new ArrayList<>();
            String[] textEntry = new String[4];

            for (Person itemPerson : listOsob) {
                textEntry[0] = itemPerson.getFirstName();
                textEntry[1] = itemPerson.getLastName();
                textEntry[2] = itemPerson.getEmail();
                textEntry[3] = itemPerson.getPhoneNumber();
                entries.add(textEntry);
                writer.writeNext(textEntry);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Person> readPeopleFromCSV(String filePath) {

        List<Person> peopleRead = new ArrayList<Person>();

        try {
            FileReader fileReader = new FileReader(filePath);
            CSVReader reader = new CSVReader(fileReader);
            String[] nextRecord;
            int krok = 1;

            while ((nextRecord = reader.readNext()) != null) {
                for (String cell : nextRecord) {
                    String[] personString = cell.split(";");
                    Person prs = new Person(personString[0], personString[1], personString[2], personString[3]);
                    peopleRead.add(prs);
                }
                krok++;
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("ReadCSV method -> FileNotFoundException  activated");
        } finally {
            return peopleRead;
        }

    }


//    Pripraveno pro pripadnou nutnost sortovat model data:
//
//    public void sortPeople() {
//        List<String> BySurname = new ArrayList<String>();
//        for (Person person : personList) {
//            BySurname.add(person.getLastName());
//        }
//        Collections.sort(BySurname);
//        for (String surname : BySurname) {
//            for (int i = 0; i < personList.size(); i++) {
//                if (personList.get(i).getLastName().equalsIgnoreCase(surname)) {
//                    personList.add(personList.get(i));
//                    personList.remove(i);
//                    continue;
//                }
//            }
//        }
//    }

//    Pripraveno pro pridani Id ke kazde Person, pokud bude treba k zajisteni
//    konzistence dat pri upravach ve View:
//
//    private void assignId(List listToAssign) {
//        for (int i = 0; i < listToAssign.size(); i++) {
//            Person member = (Person) listToAssign.get(i);
//            member.setId(i);
//        }
//    }

}
