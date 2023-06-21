package com.nk.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nk.reservation.entity.ReservationDocs;

public interface ReservationDocsRepository extends JpaRepository<ReservationDocs, Integer>{
    
}
