package com.Inventory.event.topics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReservationTopics {

    @Value("${kafka.topics.reservation-created}")
    public String reservationCreated;

    @Value("${kafka.topics.reservation-confirmed}")
    public String reservationConfirmed;

    @Value("${kafka.topics.reservation-canceled}")
    public String reservationCanceled;

    @Value("${kafka.topics.reservation-extended}")
    public String reservationExtended;
}
