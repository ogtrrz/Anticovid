package mx.com.wf.anticovid.producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.com.wf.anticovid.producer.entity.RegistroEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegistroEntityProducer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(RegistroEntity registroEntity) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(registroEntity);
        kafkaTemplate.send("topic_input", json);
    }


}
