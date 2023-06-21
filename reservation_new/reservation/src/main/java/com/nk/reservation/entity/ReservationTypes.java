package com.nk.reservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation_types")
public class ReservationTypes{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String typeName;

    // @OneToMany(mappedBy = "reservationTypeId",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    // private List<Reservation> reservations;


    // my sql quaey for inserting into tihs table
    // INSERT INTO `reservation_types` (`id`, `type_name`) VALUES ('1', 'Business');

    //delete from this table
    // DELETE FROM `reservation_types` WHERE `reservation_types`.`id` = 1


    
}