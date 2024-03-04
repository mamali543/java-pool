package ex05;

import java.util.Calendar;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;

public class Program {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Create a list of students
        String[] students = createStudentList(scanner);

        //Populate the timetable
        String[][] timetable = populateTimetable(scanner);
//        for (int i =0; i < timetable.length; i++)
//        {
//            for (int j =0 ; j < timetable[i].length; j++)
//            {
//                System.out.println("timetable:["+i+"]["+j+"]: "+ timetable[i][j]);
//            }
//        }
//        System.exit(-1);

        //Record attendance
        recordAttendance(scanner, timetable, students);

        //Display the timetable with attendance
        displayTimetable(timetable, students);
        scanner.close();
    }
                                                /*Create Students List*/
     static boolean isValidName(String name) {
        char[] charName = name.toCharArray();
        for (char c : charName)
        {
            if (c == ' ')
                return false;
        }
        return name.length() <= 10;
    }
     static String[] createStudentList(Scanner scanner) {
        System.out.println("student names (maximum 10 characters, no spaces, enter '.' to finish):");
        String[] students = new String[10];
        int index = 0;
        while (index < students.length) {
            String name = scanner.nextLine();
            if (name.equals(".")) {
                break;
            }
            if (!isValidName(name)) {
                System.err.println(ANSI_RED+"Error: Invalid name! enter a name without spaces and maximum 10 characters."+ANSI_RESET);
                System.exit(-1);
            }
            students[index] = name;
            index++;
        }
        return students;
    }

                                        /*Populate classes TimeTable*/
    private static boolean isValidDay(String dayOfWeek) {
        return dayOfWeek.equals("MO") || dayOfWeek.equals("TU") || dayOfWeek.equals("WE") ||
                dayOfWeek.equals("TH") || dayOfWeek.equals("FR") || dayOfWeek.equals("SA") ||
                dayOfWeek.equals("SU");
    }

    private static boolean isValidHour(String hour) {
        if (hour.length()==0 || hour.length() > 2)
            return false;
        char[] t = hour.toCharArray();
        for (int i = 0; i < t.length; i++)
        {
            if (t[i] < '0' || t[i] > '9')
                return false;
        }
        int a = 0;
        for (int i =0; i < t.length; i++)
        {
            a = a * 10 + (t[i]-'0');
        }
        return (a>=1 && a<=6) || (a>=13 && a <=18);
    }

    static String extractDay(String classDetails, int spaceIndex) {
        char[] t = new char[classDetails.length()-spaceIndex-1];
        for (int i = spaceIndex + 1, j = 0; i < classDetails.length(); i++, j++)
        {
            t[j] = (classDetails.toCharArray())[i];
        }
        return new String(t);
    }

    static String extractHour(String classDetails, int spaceIndex) {

        char[] t = new char[spaceIndex];
        for (int i = 0; i < spaceIndex; i++)
        {
            t[i] = classDetails.toCharArray()[i];
        }
        return new String(t);
    }

    private static int findSpaceIndex(String classDetails) {
        char[] t = classDetails.toCharArray();
        for (int i =0; i < t.length; i++)
        {
            if (t[i] == ' ')
                return i;
        }
        return -1;
    }

     static String[][] populateTimetable(Scanner scanner) {
        String[][] timetable = new String[10][12]; //10 slots for classes, 2 for hour and day_of_week
        System.out.println("Enter class details (hour, day_of_week) for each class (enter '.' to finish):");
        int index = 0;
        while (index < 10) { // Maximum 10 classes
            String classDetails = scanner.nextLine();
            if (classDetails.equals(".")) {
                break;
            }
            // Validate class details format
            int spaceIndex = findSpaceIndex(classDetails);
            if (spaceIndex == -1 || spaceIndex == classDetails.length() - 1) {
                System.err.println(ANSI_RED+"Invalid class details format! Please enter hour and day of the week separated by space."+ANSI_RESET);
                System.exit(-1);
            }
            String hour = extractHour(classDetails, spaceIndex);
            String dayOfWeek = extractDay(classDetails, spaceIndex);
            if (!isValidHour(hour))
            {
                System.err.println(ANSI_RED+"Invalid hour format!"+ANSI_RESET);
                System.exit(-1);
            }
            // Validate day of the week
            if (!isValidDay(dayOfWeek))
            {
                System.err.println(ANSI_RED+"Invalid day format!"+ANSI_RESET);
                System.exit(-1);
            }
            timetable[index][0] = hour;
            timetable[index][1] = dayOfWeek;
            index++;
        }
        return timetable;
    }

    static void recordAttendance(Scanner scanner, String[][] timetable, String[] students) {
        System.out.println("Record attendance for each class (format: student hour day status):");
        while (true) {
            System.out.print("Enter attendance details (enter '.' to finish): ");
            String attendanceDetails = scanner.nextLine();
            System.out.println("attendanceDetails: "+ attendanceDetails);
            if (attendanceDetails.equals(".")) {
                break;
            }
            String studentName = "";
            String hour = "";
            String day = "";
            String day1 = "";
            String status = "";
            // Split the input into parts
            studentName = getRecord(attendanceDetails);
            attendanceDetails = getRemainingAttendanceDetails(attendanceDetails, studentName);
            hour = getRecord(attendanceDetails);
            attendanceDetails = getRemainingAttendanceDetails(attendanceDetails, hour);
            day = getRecord(attendanceDetails);
            attendanceDetails = getRemainingAttendanceDetails(attendanceDetails, day);
            day1 = getDayOfWeek(parseInt(day));
            status = getRecord(attendanceDetails);
            attendanceDetails = getRemainingAttendanceDetails(attendanceDetails, status);
            System.out.println(ANSI_BLUE+"Details: <<"+studentName+ ">>"+hour+ ">>"+day1+ ">>"+status+ ">>"+ANSI_RESET);
            // Find the student index
            int studentIndex = -1;
            for (int i = 0; i < students.length; i++) {
                if (students[i] != null && students[i].equals(studentName)) {
                    studentIndex = i;
                    break;
                }
            }
            if (attendanceDetails.length() != 0 || !isValidHour(hour) || !isValidDayNumber(day) || !isValidStatus(status) || studentIndex == -1) {
                System.err.println(ANSI_RED+"Invalid attendance details format! Please enter student name, hour, day, and status separated by space."+ANSI_RESET);
                System.exit(-1);
            }
            // Find the class in the timetable
            boolean found = false;
            System.out.println("timetable[0][1].equals(day): "+timetable[0][1].equals(day1));
            for (int i = 0; i < timetable.length; i++) {
                if (timetable[i][0] != null && timetable[i][1] != null && timetable[i][0].equals(hour) && timetable[i][1].equals(day1)) {
                    timetable[i][2 + studentIndex] = status;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.err.println(ANSI_RED+"Class not found in timetable! Invalid Details."+ANSI_RESET);
                System.exit(-1);
            }
        }
    }

    static String getDayOfWeek(int day) {
        // Zeller's Congruence algorithm
        int h = (day + 2 * (13 + 1) / 5 + 2020 + 2020 / 4 - 2020 / 100 + 2020 / 400) % 7;
        String[] daysOfWeek = {"SU", "MO", "TU", "WE", "TH", "FR", "SA"};
        return daysOfWeek[h-1];
    }

    private static boolean isValidStatus(String status) {
        if (!status.equals("HERE") && !status.equals("NOT_HERE"))
            return false;
        return true;
    }

    private static boolean isValidDayNumber(String day) {
        if (day.length()==0 || day.length() > 2)
            return false;
        char[] t = day.toCharArray();
        for (int i = 0; i < t.length; i++)
            if (t[i] < '0' || t[i] > '9')
                return false;
        int a = 0;
        for (int i =0; i < t.length; i++)
            a = a * 10 + (t[i]-'0');
        return (a>=1 && a<=30);
    }
    private static String getRemainingAttendanceDetails(String attendanceDetails, String studentName) {
        String s = "";
        char[] a = attendanceDetails.toCharArray();
        char[] b = studentName.toCharArray();

        int studentLength = b.length;
        for (int i = studentLength+1; i < a.length; i++)
        {
            s+=a[i];
        }
        return s;
    }

    private static String getRecord(String attendanceDetails) {
        int index = 0;
        String s = "";
        while (index < attendanceDetails.length() && attendanceDetails.toCharArray()[index] != ' ')
        {

            s+=attendanceDetails.toCharArray()[index];
            index++;
        }
        return s;
    }

}

