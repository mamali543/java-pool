package ex05;

import java.util.Scanner;

public class Program {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        //The Scanner class in Java is a utility class in the java.util package that allows you to read input
        Scanner scanner = new Scanner(System.in);
        //s holds the class, and it's corresponding day and the students indexes which represent the status
        String[][] classTable = new String[10][13];
        //Create a list of students
        String[] students = createStudentList(scanner);

        //Populate the timetable
        populateTimetable(scanner, classTable);

        //Record attendance
        recordAttendance(scanner, students, classTable);

        //Display the timetable with attendance
        displayTimetable(classTable, students);
        scanner.close();
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
    private static void displayTimetable(String[][] classTable, String[] students) {
        //First September Week
        String[] str = {"TU", "WE", "tH", "FR", "SA", "SU", "MO"};
        System.out.printf("%10s", " ");
        int[][] help = new int[40][40];
        int col =0;
        //get all the days I have to print
        for (int i =0; i <30; i++)
        {
            String a = str[i%7];
            for (int j = 0; j < classTable.length; j++)
            {
                if (classTable[j] != null && a.equals(classTable[j][0]))
                {
                    System.out.print(classTable[j][1]+":00 "+classTable[j][0]+" "+(i+1)+"|");
                    help[col][0] = atoi(classTable[j][1]);
                    help[col][1] = (i+1);
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
                        if (classTable[k][2] != null && classTable[k][2].equals(itoa(help[j][1])) && classTable[k][1].equals(itoa(help[j][0])))
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
    static void populateTimetable(Scanner scanner, String[][] classTable) {
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
            classTable[index][0] = dayOfWeek;
            classTable[index][1] = hour;
            index++;
        }
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
            day1 = getDayOfWeek(atoi(day));
            status = getRecord(attendanceDetails);
            attendanceDetails = getRemainingAttendanceDetails(attendanceDetails, status);
            /*loop over classTable and check if weekday and hour equals to record in class-table then add the weekday to classTable*/
            for (int i =0; i < classTable.length; i++)
                if (classTable[i][0]!=null && classTable[i][0].equals(day1) && classTable[i][1].equals(hour) )
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
            /*loop over classTable and check if a class matches the student attendance, if so update the student status in the class*/
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
    static int atoi(String str)
    {
        char[] charArray = str.toCharArray();
        int result = 0;
        for (int i =0; i < charArray.length; i++)
        {
            char c = charArray[i];
            result = (result * 10) + (c - '0');
        }
        return result;
    }
    static String itoa(int num)
    {
        if (num == 0 )
            return "0";
        int tmp = num;
        int cnt = 0;
        while (tmp > 0)
        {
            tmp /=10;
            cnt++;
        }
        char[] charArray = new char[cnt];
        cnt -=1;
        while (num > 0)
        {
            charArray[cnt--] = (char)((num%10)+'0');
            num /=10;
        }
        return new String(charArray);
    }
}

