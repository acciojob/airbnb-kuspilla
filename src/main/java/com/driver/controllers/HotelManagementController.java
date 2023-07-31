package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.service.Hotel_management_service;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("/hotel")
@Repository


public class HotelManagementController {
   public HashMap<Integer, User> userDB =new HashMap<>();
   public HashMap<String, Hotel> hotelDB = new HashMap<>();
   public HashMap<String, Booking> bookingDb = new HashMap<>();

    @PostMapping("/add-hotel")
    public String addHotel(@RequestBody Hotel hotel) {

        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.

        if (hotel == null) return "FAILURE";
        try {
            if (userDB.containsKey(hotel.getHotelName())) return "FAILURE";
            else {
                hotelDB.put(hotel.getHotelName(), hotel);
             return "SUCCESS";
            }
        }
         catch(Exception e){
             return "FAILURE";
         }
    }

    @PostMapping("/add-user")
    public Integer addUser(@RequestBody User user){

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        userDB.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }


    @GetMapping("/get-hotel-with-most-facilities")
    public String getHotelWithMostFacilities(){

        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)

        String hotel_name = "";
        int max = 0;
        for( String names : hotelDB.keySet()){
            Hotel hotel = hotelDB.get(names);
            List<Facility> temp = hotel.getFacilities();
            int size = temp.size();
            if( max < size){
                max = size;
                hotel_name = hotel.getHotelName();
            }
        }
        return hotel_name;
    }

    @PostMapping("/book-a-room")
    public int bookARoom(@RequestBody Booking booking){

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid

//        for( String name : hotelDB.keySet()){
//            Hotel hotel = hotelDB.get(name);
//            if(hotel.getAvailableRooms() >= 1){
//                int rooms = hotel.getAvailableRooms();
//                int cost = hotel.getPricePerNight();
//
//                if( rooms >= booking.getNoOfRooms()){
//                    if( cost == booking.getAmountToBePaid()){
//                        bookingDb.put( booking.getBookingId(), booking);
//                      int prasent_rooms = hotel.getAvailableRooms() - booking.getNoOfRooms();
//                      hotel.setAvailableRooms(prasent_rooms);
//                      return booking.getAmountToBePaid();
//                    }
//                }
//
//            }
//        }
//
//
//        return -1;
        Hotel_management_service service = new Hotel_management_service();
        HashSet<Integer>  repo_id= service.random_id;
        int no_of_rooms = booking.getNoOfRooms();
        int id = service.generate_random_id();
        int price_per_night =  get_room_price(booking ,id+"");
        if(price_per_night==-1)
            return -1;
        int amount_to_be_pair = no_of_rooms*price_per_night;
        return amount_to_be_pair;

    }

    public int get_room_price(Booking book,String id)
    {
        Integer temp = hotelDB.get(book.getHotelName()).getAvailableRooms();
                int avialable_rooms = temp == null ? 0:temp;
        if(book.getNoOfRooms()>avialable_rooms )
            return -1;
        bookingDb.put(id,book);
        String hot_name = book.getHotelName();
        int hot_price_per_night = hotelDB.get(hot_name).getPricePerNight();
        return hot_price_per_night;
    }
    
    @GetMapping("/get-bookings-by-a-person/{aadharCard}")
    public int getBookings(@PathVariable("aadharCard")Integer aadharCard)
    {
        //In this function return the bookings done by a person
        int count =0;
         for( String booking_id : bookingDb.keySet()){

             Booking book_p = bookingDb.get(booking_id);
             if( book_p.getBookingAadharCard() == aadharCard) {
                 count++;
             }
         }
        return count;
    }

    @PutMapping("/update-facilities")
    public Hotel updateFacilities(List<Facility> newFacilities,String hotelName){

        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible

        Hotel hotel = hotelDB.get(hotelName);
        List<Facility> ans = hotel.getFacilities();
        for( Facility fac : newFacilities){
            if(ans.contains(fac) == false ){
                ans.add(fac);
            }
        }
           hotel.setFacilities(ans);
        return hotel;
    }

}
