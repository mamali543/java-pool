package ex05;

import java.sql.SQLSyntaxErrorException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Program {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //s holds the class, and it's corresponding day and the students indexes which represent the status
        String[][] classTable = new String[10][13];
        //Create a list of students
        String[] students = createStudentList(scanner);

        //Populate the timetable
        populateTimetable(scanner, classTable);

//        createBase(timetable, students);
        //Record attendance
        recordAttendance(scanner, students, classTable);
//        for (int i = 0; i < timetable.length; i++)
//        {
//            for (int j = 0; j < timetable[i].length; j++)
//                System.out.println("timetable[i][j]: "+ timetable[i][j]);
//        }
//        System.out.println("-------------------------------------------------------------------");
//        for (int i =0 ; i< s.length; i++)
//        {
//            for (int j =0 ; j < s[i].length ;j++)
//                System.out.println("s["+i+"]["+j+"]: "+ s[i][j]);
//        }
        //Display the timetable with attendance
        displayTimetable(classTable, students);
        scanner.close();
    }

    private static void displayTimetable(String[][] classTable, String[] students) {
        //First September Week
        String[] str = {"TU", "WE", "tH", "FR", "SA", "SU", "MO"};
        System.out.printf("%10s", " ");
        int[] help = new int[40];
        int col =0;
        for (int i =0; i <30; i++)
        {
            String a = str[i%7];
            for (int j = 0; j < classTable.length; j++)
            {
                if (classTable[j] != null && a.equals(classTable[j][0]))
                {
                    System.out.print(classTable[j][1]+":00 "+classTable[j][0]+" "+(i+1)+"|");
                    help[col] = (i+1);
                    col++;
                }
            }
        }
        System.out.println();
        for(int i = 0; i < students.length;i++ )
        {
            if (students[i] != null)
            {
                System.out.printf("%-10s", students[i]);
                for (int j = 0; j < col; j++)
                {
                    boolean f = false;
                    for (int k = 0; k < classTable.length; k++)
                    {
                        if (classTable[k][2] != null && classTable[k][2].equals(String.valueOf(help[j])))
                        {
                            if (classTable[k][3+i] !=null && classTable[k][3+i].equals("HERE") )
                                System.out.printf("%10s", " " + "1|");
                            else if (classTable[k][3+i] !=null && classTable[k][3+i].equals("NOT_HERE")) {
                                System.out.printf("%10s", " " + "-1|");
                            }
                            else
                                System.out.printf("%10s", " " + "|");
                            f = true;
                        }
                    }
                    if (!f)
                        System.out.printf("%10s", " " + "|");
                }
                System.out.println();
            }
        }
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
        for (int i = 0; i < t.length; i++)
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
            t[i] = classDetails.toCharArray()[i];
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

     static void populateTimetable(Scanner scanner, String[][] classTable) {
        String[][] timetable = new String[10][2]; //10 slots for classes, 2 for hour and day_of_week
        System.out.println("Enter class details (hour, day_of_week) for each class (enter '.' to finish):");
        int index = 0;
        int days = 0;
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
//            boolean found =false;
//            for (int i = 0; i < timetable.length; i++ )
//            {
//                if (timetable[i][1]!=null && timetable[i][1].equals(dayOfWeek))
//                {
//                    int target = parseInt(timetable[i][0]);
//                    timetable[i][target] = hour;
//                    timetable[i][0] = String.valueOf(target+1);
//                    found = true;
//                }
//            }
//            if (found == false)
//            {
//                timetable[days][0] = "3";
//                timetable[days][1] = dayOfWeek;
//                timetable[days][2] = hour;
//                days++;
//            }
            timetable[index][0] = hour;
            timetable[index][1] = dayOfWeek;
            classTable[index][0] = dayOfWeek;
            classTable[index][1] = hour;
            index++;
        }
//         for (int i = 0; i < timetable.length; i++)
//         {
//             for (int j =0 ; j < timetable[i].length ;j++)
//                 System.out.print("   " + timetable[i][j] + "   |");
//             System.out.println("");
//         }
    }
    static int number_of_columns(String[][] timeTable){
        int total = 0;
        for (int i = 0; i < timeTable.length; i++) {
            if (timeTable[i][0] == null) {
                break;
            }
            total += parseInt(timeTable[i][0]) - 2;
        }
        return total + 1;
    }

    static String[] check_if_found(String day, String[][] timeTable) {
        for (int i = 0; i < timeTable.length; i++) {
            if (timeTable[i][1] == null)
                break;
            if (timeTable[i][1].equals(day))
                return timeTable[i];
        }
        return null;
    }
    static String[][] createBase(String[][] timeTable, String[] users){
        int columns = number_of_columns(timeTable);
        String[][] base = new String[users.length+1][30];
        String[] str = {"TU", "WE", "tH", "FR", "SA", "SU", "MO"};
        int base_index = 1;

        for (int i = 1; i <= 30; i++){
            String day = str[(i-1) % 7];
            String[] classes = check_if_found(day, timeTable);
            if (classes != null){
                for (int j = 2; j < parseInt(classes[0]); j++) {
                    base[0][base_index] = classes[j] + classes[1] + String.valueOf(i);
                    base_index += 1;
                }
            }
        }

        for (int i = 0; i < base.length; i++)
        {
            for (int j =0 ; j < base[i].length ;j++)
                System.out.print("   " + base[i][j] + "   |");
            System.out.println("");
        }

        return base;
    }

    static void recordAttendance(Scanner scanner, String[] students, String[][] classTable) {
        System.out.println("Record attendance for each class (format: student hour day status):");
        while (true) {
            System.out.print("Enter attendance details (enter '.' to finish): ");
            String attendanceDetails = scanner.nextLine();
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
//            System.out.println(ANSI_BLUE+"Details: <<"+studentName+ ">>"+hour+ ">>"+day1+ ">>"+status+ ">>"+ANSI_RESET);
            for (int i =0; i < classTable.length; i++)
                if (classTable[i][0]!=null && classTable[i][0].equals(day1))
                    classTable[i][2] = day;
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
            for (int i = 0; i < classTable.length; i++) {
                if (classTable[i][0] != null && classTable[i][1] != null && classTable[i][1].equals(hour) && classTable[i][0].equals(day1)) {
                    classTable[i][3 + studentIndex] = status;
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

