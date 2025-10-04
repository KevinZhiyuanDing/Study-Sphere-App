package cpen.ADT;

/**
 * Represents a UBC course with a specific subject, course code, and year level.
 */
public class Course {
    private String subject;      // The subject of the course (e.g., "MATH")
    private String courseCode;   // The course code (e.g., "100")
    private int yearLevel;       // The year level for the course (e.g., 1)

    /**
     * Constructs a new course with specified subject, course code, and year level.
     *
     * @param subject    The subject of the course
     * @param courseCode The course code
     * @param yearLevel  The academic year level
     */
    public Course(String subject, String courseCode, int yearLevel) {
        this.subject = subject;
        this.courseCode = courseCode;
        this.yearLevel = yearLevel;
    }

    /**
     * Gets the subject of the course.
     *
     * @return The subject of the course
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the course.
     *
     * @param subject The new subject of the course
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the course code.
     *
     * @return The course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Sets the course code.
     *
     * @param courseCode The new course code
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * Gets the year level of the course.
     *
     * @return The year level
     */
    public int getYearLevel() {
        return yearLevel;
    }

    /**
     * Sets the year level of the course.
     *
     * @param yearLevel The new year level
     */
    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }
}