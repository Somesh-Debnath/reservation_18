package com.nk.reservation.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int travelRequestId;
    private int reservationDoneByEmployeeId;
    private LocalDate createdOn=LocalDate.now();;
    private String reservationDoneWithEntity;
    private LocalDate reservationDate;
    private int amount;
    private String confirmationID;
    private String remarks;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reservationTypeId")
    private ReservationTypes reservationTypeId;

    // @OneToMany(cascade = CascadeType.ALL)
    // @JoinColumn(name = "reservationDocs")
    // private ReservationDocs reservationDocs;

    @OneToMany(mappedBy = "reservation",fetch=FetchType.EAGER)
    private List<ReservationDocs> reservationDocs;


    //new sample json for this entity
    // {
    //     "travelRequestId": 1,
    //     "reservationDoneByEmployeeId": 1,
    //     "reservationDoneWithEntity": "Airline",
    //     "reservationDate": "2021-09-01",
    //     "amount": 1000,
    //     "confirmationID": "123456",
    //     "remarks": "No remarks",
    //     "reservationTypeId": {
    //         "id": 1,
    //         "reservationType": "Flight"
    //     },
    //     "reservationDocs": []
    // }
        


    
}
