package com.nk.reservation.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation_docs")
public class ReservationDocs{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int reservationId;
    private String documentURL;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reservationId",insertable = false,updatable = false)
    private Reservation reservation;

    //insert into this rtablre
    // INSERT INTO `reservation_docs` (`id`, `reservation_id`, `document_url`) VALUES ('1', '1', 'https://www.google.com');

    //delete all content from this table
    



}