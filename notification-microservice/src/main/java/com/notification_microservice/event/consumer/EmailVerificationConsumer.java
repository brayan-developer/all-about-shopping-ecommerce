package com.notification_microservice.event.consumer;
import com.notification_microservice.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailVerificationConsumer {

    private final INotificationService notificationService;

    @KafkaListener(topics = "${kafka.topics.email-verification}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumerEvent(ConsumerRecord<String, String> record) {
        String email = record.key();
        String tokenJwt = record.value();
        notificationService.sendRegistrationVerificationEmail(email, tokenJwt);
    }


}
