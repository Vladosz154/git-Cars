package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.repositories.CarRepository;
import com.example.demo.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;


    @GetMapping("/cars")
    public String getCars(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        return "cars";
    }

    @GetMapping("/addCar")
    public String addCar(Model model) {
        model.addAttribute("car", new Car());
        return "addCar";
    }

    @PostMapping("/addCar")
    public String addCar(
            @ModelAttribute("car") Car car,
            @RequestParam("photos") MultipartFile[] photos) {
        try {
            carService.addCar(car);
            carService.saveImages(photos, car);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/cars";
    }


    @GetMapping("/editCar/{id}")
    public String editCar(@PathVariable("id") Long id, Model model) {
        Optional<Car> car = carService.getCarById(id);
        if (car.isPresent()) {
            model.addAttribute("car", car.get());
            return "editCar";
        } else {
            return "redirect:/cars"; // если не найден, перенаправляем на список
        }
    }

    @PostMapping("/updateCar")
    public String updateCar(@ModelAttribute("car") Car car) {
        carService.updateCar(car);
        return "redirect:/cars";
    }

    @GetMapping("/deleteCar/{id}")
    public String deleteCar(@PathVariable("id") Long id) {
        carService.deleteCar(id); // используем сервис для удаления
        return "redirect:/cars";
    }

    @GetMapping("/view/{id}")
    public String viewCar(@PathVariable Long id, Model model) {
        Optional<Car> carOptional = carService.getCarById(id);
        if (carOptional.isPresent()) {
            model.addAttribute("car", carOptional.get());
            return "carDetail";
        } else {
            return "redirect:/cars"; // Перенаправление, если автомобиля нет
        }
    }

    @PostMapping("/deleteImage/{imageId}")
    public String deleteImage(@PathVariable Long imageId, @RequestParam("carId") Long carId) {
        carService.deleteImage(imageId);
        return "redirect:/view/" + carId; // Возврат на страницу автомобиля
    }

    @PostMapping("/updateImage")
    public String updateImage(
            @RequestParam("imageId") Long imageId,
            @RequestParam("carId") Long carId,
            @RequestParam("newPhoto") MultipartFile newPhoto) {
        try {
            carService.updateImage(imageId, newPhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/view/" + carId; // Возврат на страницу автомобиля
    }

    @PostMapping("/addImage")
    public String addImage(
            @RequestParam("carId") Long carId,
            @RequestParam("newPhoto") MultipartFile newPhoto) {
        try {
            carService.addImage(carId, newPhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/editCar/" + carId; // Возвращаемся на страницу редактирования
    }



}


