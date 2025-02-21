package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.ArrayList;


@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Марка не может быть пустой")
    @Size(min = 2, max = 50, message = "Марка должна содержать от 2 до 50 символов")
    private String brand;

    @NotNull(message = "Модель не может быть пустой")
    @Size(min = 2, max = 50, message = "Модель должна содержать от 2 до 50 символов")
    private String model;

    @NotNull(message = "Год выпуска не может быть пустым")
    @Min(value = 1900, message = "Год должен быть не ранее 1900")
    private int year;

    @NotNull(message = "Цвет не может быть пустым")
    private String color;

    @NotNull(message = "Цена не может быть пустой")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private double price;

    @NotNull(message = "Пробег не может быть пустым")
    @Min(value = 0, message = "Пробег не может быть отрицательным")
    private int mileage;

    @NotNull(message = "Владелец не может быть пустым")
    private String author;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarImage> images = new ArrayList<>();

    public List<CarImage> getImages() {
        return images;
    }

    public void setImages(List<CarImage> images) {
        this.images = images;
    }

    public void addImage(CarImage image) {
        images.add(image);
        image.setCar(this);
    }

    public void removeImage(CarImage image) {
        images.remove(image);
        image.setCar(null);
    }

    public Car() {
    }

    public Car(String brand, String model, int year, String color, String author, double price, int mileage) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.author = author;
        this.price = price;
        this.mileage = mileage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
