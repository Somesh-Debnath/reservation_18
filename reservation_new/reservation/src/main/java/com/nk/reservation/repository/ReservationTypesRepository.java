package com.nk.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nk.reservation.entity.ReservationTypes;

public interface ReservationTypesRepository extends JpaRepository<ReservationTypes, Integer>{
    
}
