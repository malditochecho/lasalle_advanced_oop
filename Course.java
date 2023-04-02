package pa3.project;

public class Course {
    // attributes
    private String id;
    private String title;
    private String disciple;
    private byte numberOfHours;
    private byte numOfGroups = 0;

    // getters
    public String getId() {
        return id;
    }
    public String getDisciple() {
        return disciple;
    }
    public byte getNumOfGroups() {
        return numOfGroups;
    }

    // setters
    public void setNumOfGroups(byte numOfGroups) {
        this.numOfGroups = numOfGroups;
    }

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
    public int getWeeklyHours(){
        switch (this.numberOfHours)
        {
            case 45:
                return 3;
            case 60:
                return 4;
            case 75:
                return 5;
            case 90:
                return 6;
            default:
                return 0;
        }
    }
    @Override
    public String toString() {
        return "Course [disciple=" + disciple + ", id=" + id + ", numOfGroups=" + numOfGroups + ", numberOfHours="
                + numberOfHours + ", title=" + title + "]";
    }
}
