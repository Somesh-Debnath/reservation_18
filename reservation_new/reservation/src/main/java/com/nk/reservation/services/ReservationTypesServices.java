package com.nk.reservation.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.repository.ReservationTypesRepository;

@Service
public class ReservationTypesServices implements IReservationTypesServices {

    @Autowired
    private ReservationTypesRepository reservationTypesRepo;

    public ReservationTypesServices(ReservationTypesRepository reservationTypesRepo) {
        this.reservationTypesRepo = reservationTypesRepo;
    }

    @Override
    public List<ReservationTypes> getReservationTypes() {
        return reservationTypesRepo.findAll();
    }

    //Not required
    @Override
    public void addIntoReservationType(ReservationTypes reservationTypes) {
        
        reservationTypesRepo.save(reservationTypes);
    }
    
}
