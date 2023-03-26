package pa3.project;

import java.util.ArrayList;
import java.util.HashMap;

public class Department {
    // attributes
    private HashMap<String, Course> courseMap;
    private ArrayList<Professor> listOfProfs;

    // constructor
    public Department() {
        this.courseMap = new HashMap<String, Course>();
        this.listOfProfs = new ArrayList<Professor>();
    }
}
