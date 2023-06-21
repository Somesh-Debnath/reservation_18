package com.nk.reservation.services;

import java.util.List;

import com.nk.reservation.entity.ReservationTypes;

public interface IReservationTypesServices{

    public void addIntoReservationType(ReservationTypes reservationTypes);

    public List<ReservationTypes> getReservationTypes();
}
