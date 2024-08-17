package com.ader.classes;

public class Car {
    private String mark;
    private int year;
    private int price;

    public Car(){
        this.mark = "name none";
        this.year = 0;
        this.price = 0;
    }

    public Car(String mark, int year, int price) {
        this.mark = mark;
        this.year = year;
        this.price = price;
    }

    public int yearsPassed(){
        return 2024 - year;
    }

    @Override
    public String toString() {
        return "Car[" +
                "mark='" + mark + '\'' +
                ", year=" + year +
                ", price=" + price +
                ']';
    }
}
