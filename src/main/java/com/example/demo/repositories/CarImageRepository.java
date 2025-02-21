package com.example.demo.repositories;

import com.example.demo.model.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {

}
