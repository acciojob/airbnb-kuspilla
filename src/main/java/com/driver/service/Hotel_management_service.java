package com.driver.service;


import com.driver.controllers.HotelManagementController;
import com.driver.model.Booking;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Random;
import java.util.List;;
@Service
public class Hotel_management_service {
    HotelManagementController repo = new HotelManagementController();

    public HashSet<Integer> random_id = new HashSet<>();

    public int generate_random_id(){

        Random ram = new Random();
        int id = ram.nextInt(1000);
        while( random_id.contains(id)){
            id = ram.nextInt();
        }
        return id;
    }

}
