package com.nk.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.services.IReservationTypesServices;

@RestController
@RequestMapping("/reservationType")
public class ReservationTypeController {

    @Autowired
    private IReservationTypesServices reservationTypesServices;

    @GetMapping("/e")
    public String home() {
        System.out.println("Welcome to Reservation Service, but why are you here?");
        return "Welcome to Reservation Service, but why are you here?";
    }

    @GetMapping("/")
    public List<ReservationTypes> getReservationTypes() {

        return reservationTypesServices.getReservationTypes();
    }

    @PostMapping("/")
    public void addIntoReservationType(@RequestBody ReservationTypes reservationType) {

        reservationTypesServices.addIntoReservationType(reservationType);
    }

}
