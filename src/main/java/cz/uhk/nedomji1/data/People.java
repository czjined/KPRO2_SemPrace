package cz.uhk.nedomji1.data;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class People {

    private List<Person> personList = new ArrayList<Person>();

    public void People() {


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

    private void assignId(List listToAssign){
        for (int i = 0; i < listToAssign.size(); i++) {
            Person member = (Person) listToAssign.get(i);
            member.setId(i);
        }
    }

    public void createSampleList() throws IOException {
        personList.add(new Person("Jiri", "Nedomlel"));
        personList.add(new Person("Pavel", "Resch"));
        personList.add(new Person("Kamil", "Novak"));
        personList.add(new Person("Anna", "Kolinska"));
        personList.add(new Person("Eva", "Adamova"));
        personList.add(new Person("Martin", "Just"));
        personList.add(new Person("Kamil", "Barabas"));
        personList.add(new Person("Jiri", "Novak"));
//        sortPeople();
        writePeopleToCSV(personList, "sampleFile.csv");
        assignId(personList);
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
            int ukazatel = 0;
            for (Person itemPerson: listOsob) {
                textEntry[0] = itemPerson.getFirstName();
                textEntry[1] = itemPerson.getLastName();
                textEntry[2] = itemPerson.getEmail();
                textEntry[3] = itemPerson.getPhoneNumber();
                System.out.println(textEntry[1]);
                entries.add(textEntry);
                writer.writeNext(textEntry);
                ukazatel++;
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void sortPeople() {
        List<String> BySurname = new ArrayList<String>();
        for (Person person : personList) {
            BySurname.add(person.getLastName());
        }
        Collections.sort(BySurname);
        for (String surname : BySurname) {
            for (int i=0; i < personList.size(); i++) {
                if (personList.get(i).getLastName().equalsIgnoreCase(surname)) {
                    personList.add(personList.get(i));
                    personList.remove(i);
                    continue;
                }
            }
        }
    }

}
