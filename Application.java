package pa3.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

import pa3.project.datastructures.PQueue;

public class Application {
    public static void main(String[] args) {
        // A. Class diagram
        // B. Class implementations

        // C. Main application: question a
        ArrayList listOfProfs = new ArrayList<Professor>();
        PQueue profProcessingQueue = new PQueue(100);
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
                setOfDisciplines.addAll(List.of(professorSplit[4].split(",")));
                Professor professor = new Professor(
                    Short.parseShort(professorSplit[0]),
                    professorSplit[1],
                    Float.parseFloat(professorSplit[2]),
                    hiringDate,
                    setOfDisciplines);
                listOfProfs.add(professor);
                profProcessingQueue.enqueue(professor);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // C. Main application: question b
        Department csDepartment = new Department(listOfProfs);

        // C. Main application: question c
        ArrayList<Course> listOfCourses = new ArrayList<>();
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
        HashMap<String, Course> csCourseMap = new HashMap<>();
        for (Course c:listOfCourses) {
            csCourseMap.put(c.getId(), c);
        }
        csDepartment.setCourseMap(csCourseMap);

        // D. Matching algorithm: question a
        while(!profProcessingQueue.isEmpty()) {
            Professor prof = (Professor) profProcessingQueue.dequeue();
            short profId = prof.getId();
            try{
                File file = new File(String.format("src/pa3/project/data/%d_selection.txt", profId));
                Scanner scanner = new Scanner(file);
                byte lineNumber = 1;
                int maxRequestedHours;
                int totalAssignedHours = 0;
                while (scanner.hasNextLine()) {
                    if(lineNumber == 1) {
                        maxRequestedHours = Integer.parseInt(scanner.nextLine());
                        lineNumber++;
                    }
                    else {
                        String line = scanner.nextLine();
                        String[] selectionSplit = line.split(", ");
                        String courseSelectionId = selectionSplit[0];
                        byte requestedGroups = Byte.parseByte(selectionSplit[1]);
                        Course c = csDepartment.getCourseMap().get(courseSelectionId);
                        if(c == null) continue;
                        Course takenCourse = new Course(c);
                        takenCourse.setNumOfGroups((byte)0);
                        while(requestedGroups > 0) {
                            if(c == null) {
                                System.out.println(String.format("The course %s not exist.", courseSelectionId));
                            } else if (c.getNumOfGroups() == 0) {
                                System.out.println(String.format("The course %s is full.", courseSelectionId));
                            }
                            else {
                                String courseDisciple = c.getDisciple();
                                if(prof.getSetOfDisciplines().contains(courseDisciple)){
                                    if(c.getWeeklyHours() > totalAssignedHours){
                                        takenCourse.setNumOfGroups((byte)(takenCourse.getNumOfGroups() + 1));
                                        requestedGroups--;
                                    }
                                }
                            }
                        }
                        prof.addCourseToListOfAffectedCourses(takenCourse);
                        lineNumber++;
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Print the affectations
        for (Professor p:csDepartment.getListOfProfs()) {
            System.out.println("\n" + p.getName());
            if(p.getListOfAffectedCourses().size() == 0)
                System.out.println("No courses assigned.");
            else {
                for (Course c:p.getListOfAffectedCourses()) {
                    System.out.println(c.getNumOfGroups() + " groups of the course " + c.getId() + " were assigned.");
                }
            }
        }

        // TESTING THE PRIORITY QUEUE METHODS
        //System.out.println("Testing methods:");
        //System.out.println("\ndisplayElement(5999)");
        //listOfProfs.displayElement(5999);
        //System.out.println("\ndisplayHigherPriorityElements(8.0)");
        //Professor tempProf = new Professor(
        //        (short)1,
        //        "Foo",
        //        8.0f,
        //        LocalDate.now(),
        //        new HashSet<>());
        //listOfProfs.displayHigherPriorityElements(tempProf);
        //System.out.println("\ndisplayLowerPriorityElements(8.0)");
        //listOfProfs.displayLowerPriorityElements(tempProf);
    }
}
