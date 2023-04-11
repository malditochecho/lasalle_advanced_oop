package pa3.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

import pa3.project.datastructures.PQueue;

public class Application {
    public static void main(String[] args) {
        //
        // C. Main application: question a
        //

        // list of professors and a priority queue for processing
        ArrayList<Professor> listOfProfs = new ArrayList<>();
        PQueue profProcessingQueue = new PQueue(100);

        // read the professors from the file
        try(Scanner scanner = new Scanner(new File("src/pa3/project/data/profs.txt"))){
            while (scanner.hasNextLine()) {
                // read a line of the file
                String line = scanner.nextLine();

                // extract the data from the line
                String[] professorSplit = line.split(":");
                String[] dateSplit = professorSplit[3].split("-");
                LocalDate hiringDate = LocalDate.of(
                        Integer.parseInt(dateSplit[2]),
                        Integer.parseInt(dateSplit[1]),
                        Integer.parseInt(dateSplit[0]));
                Set<String> setOfDisciplines = new HashSet<>(List.of(professorSplit[4].split(",")));

                // create a professor object based on the data from the line
                Professor professor = new Professor(
                    Short.parseShort(professorSplit[0]),
                    professorSplit[1],
                    Float.parseFloat(professorSplit[2]),
                    hiringDate,
                    setOfDisciplines);

                // add the professor object to the list of professors
                listOfProfs.add(professor);

                // add the professor object to the priority queue
                profProcessingQueue.enqueue(professor);
            }
        } catch (FileNotFoundException ignored) {}


        //
        // C. Main application: question b
        //

        // create a department object with the list of professors
        Department csDepartment = new Department(listOfProfs);


        //
        // C. Main application: question c
        //

        // create a list of courses
        ArrayList<Course> listOfCourses = new ArrayList<>();

        // read the professors from the file
        try(Scanner scanner = new Scanner(new File("src/pa3/project/data/courses_f22.txt"))){
            while (scanner.hasNextLine()) {
                // read a line of the file
                String line = scanner.nextLine();

                // extract the data from the line
                String[] courseSplit = line.split(": ");

                // create a course object based on the data from the line
                Course course = new Course(
                    courseSplit[0],
                    courseSplit[1],
                    courseSplit[2],
                    Byte.parseByte(courseSplit[3]),
                    Byte.parseByte(courseSplit[4]));

                // add the course object to the list of courses
                listOfCourses.add(course);
            }
        } catch (FileNotFoundException ignored) {}
        // create a hashmap of courses (the key is the course id, and the value is the course itself)
        HashMap<String, Course> csCourseMap = new HashMap<>();
        // add the courses to the hashmap
        for (Course c:listOfCourses)
            csCourseMap.put(c.getId(), c);
        // add the hashmap to the department object
        csDepartment.setCourseMap(csCourseMap);


        //
        // D. Matching algorithm: question a
        //

        // process the professors in the priority queue
        while(!profProcessingQueue.isEmpty()) {
            // get the next professor from the priority queue (the one with the highest priority)
            Professor prof = profProcessingQueue.dequeue();

            // extract the professor's id
            short profId = prof.getId();

            // try to read the professor's selection file
            try(Scanner scanner = new Scanner(new File(String.format("src/pa3/project/data/%d_selection.txt", profId)))){
                // read the maximum number of hours the professor can take
                int maxRequestedHours = Integer.parseInt(scanner.nextLine());

                // read the professor's course selection line by line
                while (scanner.hasNextLine()) {
                    // read a line of the file
                    String line = scanner.nextLine();

                    // extract the data from the line
                    String[] selectionSplit = line.split(", ");
                    String courseSelectionId = selectionSplit[0];
                    byte requestedGroups = Byte.parseByte(selectionSplit[1]);

                    // try to find the course in the department's course map
                    Course courseRequested = csDepartment.getCourseMap().get(courseSelectionId);
                    // if the course is not found, skip to the next line
                    if (courseRequested == null) continue;
                    // if the course is found, check if the professor can take it by:
                    // 1. checking if the course has groups available
                    else if (courseRequested.getNumOfGroups() == 0) continue;
                    // 2. checking if the professor has the discipline required
                    else if (!prof.getSetOfDisciplines().contains(courseRequested.getDiscipline()))  continue;
                    // 3. checking if the professor has enough hours available
                    else if (maxRequestedHours == 0) continue;
                    // 4. assuming the professor has enough hours available, check if the professor can take 1 course at least
                    else if(courseRequested.getWeeklyHours() > maxRequestedHours) continue;

                    // if the professor can take the course, copy the course object and assign it to the professor
                    else {
                        // create a copy of the course object
                        Course courseAssigned = new Course(courseRequested);

                        // check how many groups the professor can take based on the number of hours available
                        int howManyCoursesCanFit = maxRequestedHours / courseRequested.getWeeklyHours();

                        // check how many groups the professor will take based on:
                        // 1. the number of groups the professor can take (howManyCoursesCanFit)
                        // 2. the number of groups available
                        // 3. the number of groups requested
                        int groupsAssigned = Math.min(Math.min(howManyCoursesCanFit, courseRequested.getNumOfGroups()), requestedGroups);

                        // assign the number of groups to the recently copied course object
                        courseAssigned.setNumOfGroups((byte) groupsAssigned);

                        // reduce the number of groups available in the original course object
                        courseRequested.setNumOfGroups((byte) (courseRequested.getNumOfGroups() - groupsAssigned));

                        // add the course to the professor's list of affected courses
                        prof.addCourseToListOfAffectedCourses(courseAssigned);

                        // reduce the number of hours available to the professor for the next course selection iteration
                        maxRequestedHours -= groupsAssigned * courseRequested.getWeeklyHours();
                    }
                }
            } catch (FileNotFoundException ignored) {}
        }


        //
        // Print the affectations
        //

        // print the affectations for each professor of the department
        for (Professor p:csDepartment.getListOfProfs()) {
            // print the professor's name and id
            System.out.printf("\n%s (id %s)%n",p.getName(), p.getId());

            // if the professor has no courses assigned, print a message
            if(p.getListOfAffectedCourses().size() == 0)
                System.out.println("No courses assigned.");

            // if the professor has courses assigned, print the list of courses
            else {
                for (Course c : p.getListOfAffectedCourses())
                    System.out.printf("[%s] %s (%d group(s))%n", c.getId(), c.getTitle(), c.getNumOfGroups());

                // calculate and print the total number of hours taken by the professor
                int totalTakenHours = 0;
                for (Course c : p.getListOfAffectedCourses()) {
                    totalTakenHours += c.getNumOfGroups() * c.getWeeklyHours();
                }
                System.out.printf("%s hours in total%n", totalTakenHours);
            }
        }
    }
}
