import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CourseEnrollmentSystem {

    public static class Student {
        private static ArrayList<Student> students = new ArrayList<>(); // Static list to store all students
        private static int nextId = 1; // Static variable to track next available student ID

        private int id;
        private String name;
        private ArrayList<Course> enrolledCourses;
        private ArrayList<Integer> grades; // List to store grades

        public Student(String name) {
            this.id = nextId++; // Assign unique student ID
            this.name = name;
            this.enrolledCourses = new ArrayList<>();
            this.grades = new ArrayList<>(); // Initialize grades list
            students.add(this); // Add this student to the list of students
        }

        public static Student createStudentFromInput(Scanner scanner) {
            System.out.print("Enter student name: ");
            String studentName = scanner.nextLine().trim();

            // Check if student name already exists
            for (Student student : students) {
                if (student.getName().equalsIgnoreCase(studentName)) {
                    System.out.println("Student with the same name already exists. Using existing student ID.");
                    return student; // Return existing student
                }
            }

            // If no existing student found, create a new one
            return new Student(studentName);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ArrayList<Course> getEnrolledCourses() {
            return enrolledCourses;
        }

        public void enrollInCourse(Course course) {
            enrolledCourses.add(course);
        }

        public void assignGrade(Course course, int grade) {
            if (enrolledCourses.contains(course)) {
                course.setGrade(this, grade);
                addGrade(grade);
            } else {
                System.out.println("Student " + name + " is not enrolled in course " + course.getName());
            }
        }

        public void addGrade(int grade) {
            grades.add(grade); // Add grade to the List
        }

        public double calculateAverageGrade() {
            if (grades.isEmpty()) {
                return 0.0; // Return 0 if no grades are available
            }

            int sum = 0;
            for (int grade : grades) {
                sum += grade; // Calculate sum of grades
            }

            return (double) sum / grades.size(); // Calculate average based on Array size
        }

        public static ArrayList<Student> getStudents() {
            return students;
        }
    }

    public static class Course {
        private static int nextId = 1; // Static variable to track next available course ID
        private int id;
        private String courseCode;
        private String name;
        private int maxCapacity;
        private ArrayList<Student> enrolledStudents;
        private HashMap<Student, Integer> grades;

        public Course(String courseCode, String name, int maxCapacity) {
            this.id = nextId++; // Assign unique course ID
            this.courseCode = courseCode;
            this.name = name;
            this.maxCapacity = maxCapacity;
            this.enrolledStudents = new ArrayList<>();
            this.grades = new HashMap<>();
        }

        public int getId() {
            return id;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public String getName() {
            return name;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public ArrayList<Student> getEnrolledStudents() {
            return enrolledStudents;
        }

        public void enrollStudent(Student student) {
            if (enrolledStudents.size() < maxCapacity) {
                enrolledStudents.add(student);
                student.enrollInCourse(this);
            } else {
                System.out.println("Course " + name + " is already full.");
            }
        }

        public void setGrade(Student student, int grade) {
            if (enrolledStudents.contains(student)) {
                grades.put(student, grade);
                System.out.println("Grade " + grade + " assigned to student " + student.getName() + " for course " + name);
            } else {
                System.out.println("Student " + student.getName() + " is not enrolled in course " + name);
            }
        }

        public int getGrade(Student student) {
            if (grades.containsKey(student)) {
                return grades.get(student);
            } else {
                throw new IllegalArgumentException("Student " + student.getName() + " has not been assigned a grade for course " + name);
            }
        }
    }

    public static class CourseManagement {
        private static ArrayList<Course> courses = new ArrayList<>();

        public static void addCourse(String courseCode, String name, int maxCapacity) {
            for (Course course : courses) {
                if (course.getName().equalsIgnoreCase(name) || course.getCourseCode().equalsIgnoreCase(courseCode)) {
                    System.out.println("Course already exists.");
                    return;
                }
            }
            Course newCourse = new Course(courseCode, name, maxCapacity);
            courses.add(newCourse);
            System.out.println("Course " + name + " added successfully.");
        }

        public static void enrollStudent(Student student, Course course) {
            course.enrollStudent(student);
        }

        public static void assignGrade(Student student, Course course, int grade) {
            course.setGrade(student, grade);
        }

        public static ArrayList<Course> getCourses() {
            return courses;
        }
    }

    public static class AdministratorInterface {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Administrator Interface:");
                System.out.println("1. Add Course");
                System.out.println("2. Enroll Student");
                System.out.println("3. Assign Grade");
                System.out.println("4. Calculate Average Grade");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            addCourse(scanner);
                            break;
                        case 2:
                            enrollStudent(scanner);
                            break;
                        case 3:
                            assignGrade(scanner);
                            break;
                        case 4:
                            calculateAverageGrade(scanner);
                            break;
                        case 5:
                            System.out.println("Exiting Administrator Interface.");
                            scanner.close();
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Consume invalid input
                }
            }
        }

        private static void addCourse(Scanner scanner) {
            System.out.print("Enter course code: ");
            String courseCode = scanner.nextLine();
            System.out.print("Enter course name: ");
            String courseName = scanner.nextLine();
            int maxCapacity = 0;
            boolean validCapacity = false;
            while (!validCapacity) {
                try {
                    System.out.print("Enter maximum capacity: ");
                    maxCapacity = Integer.parseInt(scanner.nextLine());
                    validCapacity = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer for maximum capacity.");
                }
            }
            CourseManagement.addCourse(courseCode, courseName, maxCapacity);
        }

        private static void enrollStudent(Scanner scanner) {
            Student student = Student.createStudentFromInput(scanner);
            System.out.print("Enter course name to enroll: ");
            String courseToEnroll = scanner.nextLine();
            boolean foundCourse = false;
            for (Course course : CourseManagement.getCourses()) {
                if (course.getName().equalsIgnoreCase(courseToEnroll)) {
                    CourseManagement.enrollStudent(student, course);
                    foundCourse = true;
                    System.out.println("Student Enrolled! ID#: " + student.getId());
                    break;
                }
            }
            if (!foundCourse) {
                System.out.println("Course not found.");
            }
        }

        private static void assignGrade(Scanner scanner) {
            int studentID = 0;
            boolean validId = false;
            while (!validId) {
                try {
                    System.out.print("Enter student ID: ");
                    studentID = Integer.parseInt(scanner.nextLine());
                    validId = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer for student ID.");
                }
            }
            System.out.print("Enter course name: ");
            String courseNameToGrade = scanner.nextLine();
            int grade = 0;
            boolean validGrade = false;
            while (!validGrade) {
                try {
                    System.out.print("Enter grade: ");
                    grade = Integer.parseInt(scanner.nextLine());
                    validGrade = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer for grade.");
                }
            }
            boolean foundStudent = false;
            boolean foundCourseForGrade = false;
            for (Student stu : Student.getStudents()) {
                if (stu.getId() == studentID) {
                    foundStudent = true;
                    for (Course c : stu.getEnrolledCourses()) {
                        if (c.getName().equalsIgnoreCase(courseNameToGrade)) {
                            CourseManagement.assignGrade(stu, c, grade);
                            foundCourseForGrade = true;
                            break;
                        }
                    }
                    break;
                }
            }
            if (!foundStudent) {
                System.out.println("Student not found.");
            } else if (!foundCourseForGrade) {
                System.out.println("Course not found for the given student.");
            }
        }

        private static void calculateAverageGrade(Scanner scanner) {
            int studentIDForAverageGrade = 0;
            boolean validId = false;
            while (!validId) {
                try {
                    System.out.print("Enter student ID: ");
                    studentIDForAverageGrade = Integer.parseInt(scanner.nextLine());
                    validId = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer for student ID.");
                }
            }

            boolean foundStudentForGrade = false;
            for (Student stu : Student.getStudents()) {
                if (stu.getId() == studentIDForAverageGrade) {
                    double averageGrade = stu.calculateAverageGrade();
                    System.out.println("Overall average grade for student " + stu.getName() + ": " + averageGrade);
                    foundStudentForGrade = true;
                    break;
                }
            }
            if (!foundStudentForGrade) {
                System.out.println("Student not found.");
            }
        }
    }
}
