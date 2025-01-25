package MicroservicePago.event.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@RequiredArgsConstructor
@Service
public class ReservationEventProducer {

    @Value("${kafka.topics.reservation-created}")
    private String topicReservationCreated;
    @Value("${kafka.topics.reservation-confirmed}")
    private String topicReservationConfirmed;

    @Value("${kafka.topics.reservation-canceled}")
    private String topicReservationCanceled;

    @Value("${kafka.topics.reservation-extended}")
    private String topicReservationExtended;


    private final KafkaTemplate<Long, String> kafkaTemplate;

    public void createReservation(Long orderId, String token){
        kafkaTemplate.send(topicReservationCreated,orderId , token);
    }

    public void confirmReservation(Long orderId){
        kafkaTemplate.send(topicReservationConfirmed, orderId, "");
    }

    public void cancelReservation(Long orderId){
        kafkaTemplate.send(topicReservationCanceled, orderId, "");
    }

    public void extendReservation(Long orderId){
        kafkaTemplate.send(topicReservationExtended, orderId, "");
    }


}
