package pa3.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

import pa3.project.datastructures.PQueue;

public class Application {
    public static void main(String[] args) {
        // LISTS OF PROFESSORS AND COURSES
        PQueue listOfProfs = new PQueue(100);
        ArrayList<Course> listOfCourses = new ArrayList<>();

        // READING THE PROFESSORS FILE AND ADDING THEM TO THE QUEUE
        try{
            File file = new File("src/pa3/project/data/profs.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] professorSplit = line.split(":");
                // formatting data
                String[] dateSplit = professorSplit[3].split("-");
                LocalDate hiringDate = LocalDate.of(
                        Integer.parseInt(dateSplit[2]),
                        Integer.parseInt(dateSplit[1]),
                        Integer.parseInt(dateSplit[0]));
                Set<String> setOfDisciplines = new HashSet<>();
                for (String discipline : professorSplit[4].split(",")) {
                    setOfDisciplines.add(discipline);}
                Professor professor = new Professor(
                    Short.parseShort(professorSplit[0]),
                    professorSplit[1],
                    Float.parseFloat(professorSplit[2]),
                    hiringDate,
                    setOfDisciplines);
                listOfProfs.enqueue(professor);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // READING THE COURSES FILE AND ADDING THEM TO THE LIST
        try{
        File file = new File("src/pa3/project/data/courses_f22.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] courseSplit = line.split(": ");
            Course course = new Course(
                courseSplit[0],
                courseSplit[1],
                courseSplit[2],
                Byte.parseByte(courseSplit[3]),
                Byte.parseByte(courseSplit[4]));
            listOfCourses.add(course);
        }
        scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // TESTING THE PRIORITY QUEUE METHODS
        System.out.println("\ndisplayElement");
        listOfProfs.displayElement(5999);

        System.out.println("\ndisplayHigherPriorityElements");
        Professor tempProf = new Professor(
                (short)1,
                "Foo",
                4.0f,
                LocalDate.now(),
                new HashSet<>());
        listOfProfs.displayHigherPriorityElements(tempProf);

        System.out.println("\ndisplayHigherPriorityElements");
        listOfProfs.displayLowerPriorityElements(tempProf);
    }
}
