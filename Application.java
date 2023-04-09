package pa3.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

import pa3.project.datastructures.PQueue;

public class Application {
    public static void main(String[] args) {
        // C. Main application: question a
        ArrayList<Professor> listOfProfs = new ArrayList<>();
        PQueue profProcessingQueue = new PQueue(100);
        try(Scanner scanner = new Scanner(new File("src/pa3/project/data/profs.txt"))){
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] professorSplit = line.split(":");
                String[] dateSplit = professorSplit[3].split("-");
                LocalDate hiringDate = LocalDate.of(
                        Integer.parseInt(dateSplit[2]),
                        Integer.parseInt(dateSplit[1]),
                        Integer.parseInt(dateSplit[0]));
                Set<String> setOfDisciplines = new HashSet<>(List.of(professorSplit[4].split(",")));
                Professor professor = new Professor(
                    Short.parseShort(professorSplit[0]),
                    professorSplit[1],
                    Float.parseFloat(professorSplit[2]),
                    hiringDate,
                    setOfDisciplines);
                listOfProfs.add(professor);
                profProcessingQueue.enqueue(professor);
            }
        } catch (FileNotFoundException ignored) {}

        // C. Main application: question b
        Department csDepartment = new Department(listOfProfs);

        // C. Main application: question c
        ArrayList<Course> listOfCourses = new ArrayList<>();
        try(Scanner scanner = new Scanner(new File("src/pa3/project/data/courses_f22.txt"))){
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
        } catch (FileNotFoundException ignored) {}
        HashMap<String, Course> csCourseMap = new HashMap<>();
        for (Course c:listOfCourses)
            csCourseMap.put(c.getId(), c);
        csDepartment.setCourseMap(csCourseMap);

        // D. Matching algorithm: question a
        while(!profProcessingQueue.isEmpty()) {
            Professor prof = profProcessingQueue.dequeue();
            short profId = prof.getId();
            try(Scanner scanner = new Scanner(new File(String.format("src/pa3/project/data/%d_selection.txt", profId)))){
                int maxRequestedHours = Integer.parseInt(scanner.nextLine());
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] selectionSplit = line.split(", ");
                    String courseSelectionId = selectionSplit[0];
                    byte requestedGroups = Byte.parseByte(selectionSplit[1]);
                    Course courseRequested = csDepartment.getCourseMap().get(courseSelectionId);
                    if (courseRequested == null) continue;
                    else if (courseRequested.getNumOfGroups() == 0) continue;
                    else if (!prof.getSetOfDisciplines().contains(courseRequested.getDiscipline()))  continue;
                    else if (maxRequestedHours == 0) continue;
                    else if(courseRequested.getWeeklyHours() > maxRequestedHours) continue;
                    else {
                        Course courseAssigned = new Course(courseRequested);
                        int howManyCoursesCanFit = maxRequestedHours / courseRequested.getWeeklyHours();
                        int groupsAssigned = Math.min(Math.min(howManyCoursesCanFit, courseRequested.getNumOfGroups()), requestedGroups);
                        courseAssigned.setNumOfGroups((byte) groupsAssigned);
                        courseRequested.setNumOfGroups((byte) (courseRequested.getNumOfGroups() - groupsAssigned));
                        prof.addCourseToListOfAffectedCourses(courseAssigned);
                        maxRequestedHours -= groupsAssigned * courseRequested.getWeeklyHours();
                    }
                }
            } catch (FileNotFoundException ignored) {}
        }

        // Print the affectations
        for (Professor p:csDepartment.getListOfProfs()) {
            System.out.printf("\n%s (id %s)%n",p.getName(), p.getId());
            if(p.getListOfAffectedCourses().size() == 0)
                System.out.println("No courses assigned.");
            else {
                for (Course c : p.getListOfAffectedCourses())
                    System.out.printf("[%s] %s (%d group(s))%n", c.getId(), c.getTitle(), c.getNumOfGroups());
                int totalTakenHours = 0;
                for (Course c : p.getListOfAffectedCourses()) {
                    totalTakenHours += c.getNumOfGroups() * c.getWeeklyHours();
                }
                System.out.printf("%s hours in total%n", totalTakenHours);
            }
        }
    }
}
