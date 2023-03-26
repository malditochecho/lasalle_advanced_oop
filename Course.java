package pa3.project;

public class Course {
    // attributes
    private String id;
    private String title;
    private String disciple;
    private byte numberOfHours;
    private byte numOfGroups = 0;

    // constructor
    public Course(String id, String title, String disciple, byte numberOfHours, byte numOfGroups) {
        this.id = id;
        this.title = title;
        this.disciple = disciple;
        this.numberOfHours = numberOfHours;
        this.numOfGroups = numOfGroups;
    }
    public Course(Course course) {
        id = course.id;
        title = course.title;
        disciple = course.disciple;
        numberOfHours = course.numberOfHours;
        numOfGroups = course.numOfGroups;
    }

    // methods
    @Override
    public String toString() {
        return "Course [disciple=" + disciple + ", id=" + id + ", numOfGroups=" + numOfGroups + ", numberOfHours="
                + numberOfHours + ", title=" + title + "]";
    }
}
