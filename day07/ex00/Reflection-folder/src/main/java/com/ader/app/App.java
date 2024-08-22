package com.ader.app;

import java.lang.reflect.*;
import java.util.Scanner;
import java.util.Arrays;

import com.ader.classes.Car;
import com.ader.classes.User;
public class App 
{
    private static final User user = new User();
    private static final Car car = new Car();
    private static Class<?> classInput;
    public static void main( String[] args ) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);

        //get classes packages
        Class<?> classCar = Class.forName("com.ader.classes.Car");
        Class<?> classUser = Class.forName("com.ader.classes.User");

        //get classe names
        String simpleUserClass = classUser.getSimpleName();
        String simpleCarClass = classCar.getSimpleName();

        System.out.println( "Classes:\n"+simpleCarClass+"\n"+simpleUserClass+"\n"+"-------------------\n"+"Enter class name:\n-->");
        
        String classInputString  = scanner.nextLine();
        
        System.out.println("-----------------------\nfields:");
        
        classInput = Class.forName("com.ader.classes."+classInputString);
        
        Field[] fields = classInput.getDeclaredFields();
        Method[] methods = classInput.getDeclaredMethods();

        for (Field field : fields) {
            String type = (field.getAnnotatedType()).toString();
            if (type.contains("java.lang.")) {
                type = type.substring(10);
            }
            System.out.println("\t" + type + " " + field.getName());
        }

        System.out.println("methods:");

        for (Method method : methods) {
            String type = (method.getAnnotatedReturnType()).toString();
            if (type.contains("java.lang.")) {
                type = type.substring(10);
            }
            String temp = Arrays.toString(method.getParameterTypes());
            temp = temp.substring(1, temp.length() - 1);
            System.out.println("\t" + type + " " + method.getName() + "(" + temp + ")");
        }

        System.out.println("---------------------\nLet’s create an object.");

        for (Field field : fields) {
            System.out.print(field.getName() + ":\n-> ");
            typeForInput(field, scanner);
        }

        ShowClassToString();

        System.out.println("---------------------\nEnter name of the field for changing:\n-->");

        String inputField = scanner.nextLine();
        for (Field field: fields){
            if (inputField.equals(field.getName().toString()))
            {
                System.out.println("\nEnter String value:\n-->");
                typeForInput(field, scanner);
            }
        }

        ShowClassToString();

        System.out.print("---------------------\nEnter name of the method for call:\n-> ");
        inputField = scanner.nextLine();
        for (Method method : methods) {
            String temp = Arrays.toString(method.getParameterTypes());
            temp = temp.substring(1, temp.length() - 1);
            if (inputField.equals(method.getName() + "(" + temp + ")")) {
                int anInt = 0;
                if (!temp.equals("")) {
                    System.out.print("Enter " + temp + " value:\n-> ");
                    anInt = scanner.nextInt();
                }
                System.out.println("Method returned:");
                inputMethod(method, anInt);
            }
        }
    }

    private static void typeForInput(Field field, Scanner input) {
        String type = (field.getAnnotatedType()).toString();
        if (type.contains("java.lang.")) {
            type = type.substring(10);
        }
        field.setAccessible(true);
        if (type.equals("String")) {
            String inOb = input.nextLine();
            try {
                if(classInput.getSimpleName().equals("User")){
                    field.set(user, inOb);
                }
                if(classInput.getSimpleName().equals("Car")){
                    field.set(car, inOb);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else if (type.equals("Integer") || type.equals("int")) {
            int inOb = input.nextInt();
            input.nextLine();
            try {
                if(classInput.getSimpleName().equals("User")){
                    field.set(user, inOb);
                }
                if(classInput.getSimpleName().equals("Car")){
                    field.set(car, inOb);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void ShowClassToString(){
        if(classInput.getSimpleName().equals("User")) {
            System.out.println("Object created: " + user.toString());
        } else if (classInput.getSimpleName().equals("Car")) {
            System.out.println("Object created: " + car.toString());
        }
    }

    private static void inputMethod(Method method, int anInt){
        switch ((method.getName()).toString()) {
            case "addMonth" -> System.out.println(user.addMonth(anInt));
            case "yearBirthday" -> System.out.println(user.yearBirthday());
            case "yearPassed" -> System.out.println(car.yearPassed());
        }
    }
}


// mvn exec:java -Dexec.mainClass="com.ader.app.App"