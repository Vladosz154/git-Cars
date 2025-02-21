package com.example.demo.services;

import com.example.demo.model.Car;
import com.example.demo.model.CarImage;
import com.example.demo.repositories.CarImageRepository;
import com.example.demo.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarImageRepository carImageRepository;

    private final String uploaDir = System.getProperty("user.dir") + "/uploads";


    public void addCar(Car car) {
        carRepository.save(car);
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public void updateCar(Car car) {
        carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public void saveImages(MultipartFile[] files, Car car) throws IOException {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(uploaDir, fileName);
                System.out.println("Сохранение файла: " + filePath.toString());
                Files.copy(file.getInputStream(), filePath);


                CarImage image = new CarImage();
                image.setFileName(fileName);
                car.addImage(image);

                carImageRepository.save(image);
            }
        }
    }

    public void deleteImage(Long imageId) {
        Optional<CarImage> imageOptional = carImageRepository.findById(imageId);
        if (imageOptional.isPresent()) {
            Path filePath = Paths.get(uploaDir, imageOptional.get().getFileName());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            carImageRepository.deleteById(imageId);
        }
    }

    public void updateImage(Long imageId, MultipartFile newPhoto) throws IOException {
        Optional<CarImage> imageOptional = carImageRepository.findById(imageId);
        if (imageOptional.isPresent()) {
            CarImage carImage = imageOptional.get();

            // Удаляем старый файл
            Path oldFilePath = Paths.get(uploaDir, carImage.getFileName());
            Files.deleteIfExists(oldFilePath);

            // Сохраняем новый файл
            String newFileName = newPhoto.getOriginalFilename();
            Path newFilePath = Paths.get(uploaDir, newFileName);
            Files.copy(newPhoto.getInputStream(), newFilePath);

            // Обновляем данные
            carImage.setFileName(newFileName);
            carImageRepository.save(carImage);
        }
    }

    public void addImage(Long carId, MultipartFile newPhoto) throws IOException {
        Optional<Car> carOptional = carRepository.findById(carId);
        if (carOptional.isPresent() && !newPhoto.isEmpty()) {
            Car car = carOptional.get();

            // Сохраняем файл на диск
            String fileName = newPhoto.getOriginalFilename();
            Path filePath = Paths.get(uploaDir, fileName);
            Files.copy(newPhoto.getInputStream(), filePath);

            // Создаем новую сущность CarImage
            CarImage carImage = new CarImage();
            carImage.setFileName(fileName);
            carImage.setCar(car);

            // Сохраняем изображение
            car.addImage(carImage);
            carImageRepository.save(carImage);
        } else {
            throw new IllegalArgumentException("Автомобиль с ID " + carId + " не найден или файл пустой");
        }
    }



}

