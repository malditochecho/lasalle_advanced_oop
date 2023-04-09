package pa3.project;

import java.util.ArrayList;
import java.util.HashMap;

public class Department {
    // attributes
    private HashMap<String, Course> courseMap;
    private ArrayList<Professor> listOfProfs;

    // getters
    public HashMap<String, Course> getCourseMap() {
        return courseMap;
    }
    public ArrayList<Professor> getListOfProfs() {
        return listOfProfs;
    }

    // setters
    public void setCourseMap(HashMap<String, Course> courseMap) {
        this.courseMap = courseMap;
    }

    // constructor
    public Department(ArrayList<Professor> listOfProfs) {
        this.listOfProfs = listOfProfs;
        this.courseMap = new HashMap<>();
    }
    public Department(HashMap<String, Course> courseMap, ArrayList<Professor> listOfProfs) {
        this.courseMap = courseMap;
        this.listOfProfs = listOfProfs;
    }
}
