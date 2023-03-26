package pa3.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class Professor implements Comparable<Professor>
{
    // attributes
    private short id;
    private String name;
    private float seniority;
    private LocalDate hiringDate;
    private Set<String> setOfDisciplines;
    private ArrayList<Course> listOfAffectedCourses;

    // getters and setters
    public short getId() { return id; }

    // constructor
    public Professor(short id, String name, float seniority, LocalDate hiringDate, Set<String> setOfDisciplines) {
        this.id = id;
        this.name = name;
        this.seniority = seniority;
        this.hiringDate = hiringDate;
        this.setOfDisciplines = setOfDisciplines;
        this.listOfAffectedCourses = null;
    }

    // methods
    @Override
    public int compareTo(Professor o) {
        if(this.seniority > ((Professor)o).seniority)
            return 1;
        else if(this.seniority < ((Professor)o).seniority)
            return -1;
        else
            return this.hiringDate.compareTo(((Professor)o).hiringDate);
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }
}
