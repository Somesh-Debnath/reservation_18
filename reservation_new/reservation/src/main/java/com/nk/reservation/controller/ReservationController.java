package com.nk.reservation.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.exceptions.ResourceNotFoundException;
import com.nk.reservation.services.IReservationServices;
import com.nk.reservation.services.IReservationTypesServices;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    Logger logger=LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private IReservationServices reservationServices;
    @Autowired
    private IReservationTypesServices reservationTypesServices;

    @GetMapping("/types")
    public List<ReservationTypes> getReservationTypes() {
        logger.error("Reservations fetched successfully");
        return reservationTypesServices.getReservationTypes();
    }
    
    @PostMapping("/add")
    public void addNewReservation(@RequestBody Reservation reservation) {
        reservationServices.addNewReservation(reservation);
        logger.info("Reservation added successfully");
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable int id) throws ResourceNotFoundException{
        logger.info("Reservation fetched successfully");
        return reservationServices.getReservationById(id).orElse(null);
    }

    @GetMapping("/track/{travelRequestId}")
    public List<Reservation> getReservationByTravelRequestId(@PathVariable int travelRequestId) throws ResourceNotFoundException{
        logger.info("Reservation fetched successfully");
        return reservationServices.getReservationsByTravelRequestId(travelRequestId);
    }

     @GetMapping("/{reservationId}/download")
     public ResponseEntity<InputStreamResource> downloadReservationsDoc(@PathVariable("reservationId") int reservationId) {
    	 ByteArrayInputStream pdf=reservationServices.downloadReservationsDoc(reservationId);
    	 
    	 HttpHeaders httpHeaders=new HttpHeaders();
    	 httpHeaders.add("Content-Disposition","inline;lcwd.pdf");
    	 
         logger.info("doc created");
    	 return ResponseEntity
    			 .ok()
    			 .headers(httpHeaders)
    			 .contentType(MediaType.APPLICATION_PDF)
    			 .body(new InputStreamResource(pdf));
    	 
    	 
     }


    //NOT REQUIRED
    @GetMapping("/")
    public List<Reservation> getAllReservations() {
        return reservationServices.getAllReservations();
    }
}
