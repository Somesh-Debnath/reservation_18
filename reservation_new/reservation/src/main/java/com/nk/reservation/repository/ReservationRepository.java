package com.nk.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nk.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    
    @Query("select r from Reservation r where r.travelRequestId = ?1")
    List<Reservation> findByTravelRequestId(int travelRequestId);
    
}