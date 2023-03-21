package mx.com.wf.anticovid.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
//import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import mx.com.wf.anticovid.producer.entity.RegistroEntity;
import mx.com.wf.anticovid.producer.form.Registrar;
import mx.com.wf.anticovid.producer.producer.RegistroEntityProducer;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;
@Slf4j
@Controller
public class handlingforsubmission {

    @Autowired
    private RegistroEntityProducer producer;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //private static final Pattern patternCurp = Pattern.compile("^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$");
    private static final Pattern patternCurp = Pattern.compile("^[0-9]{8}$");
    private static final Pattern patternEmail = Pattern.compile("^(.+)@(.+)$");

    @GetMapping("/registrar")
    public String registrarForm(Model model) {
        model.addAttribute("registrar", new Registrar());
        return "registrar";
    }

    @PostMapping("/registrar")
    public String registrarSubmit(@ModelAttribute Registrar registrar, Model model) throws JsonProcessingException {
        model.addAttribute("registrar", registrar);
        boolean matcherCurp = patternCurp.matcher(registrar.getCurp()).matches();
        boolean matcherEmail = patternEmail.matcher(registrar.getEmail()).matches();
//        log.info("matcherCurp "+matcherCurp);
//        log.info("matcherEmail "+matcherEmail);
        if(matcherCurp && matcherEmail) {
            long curpl = parseLong(registrar.getCurp());
            String curps = String.valueOf(curpl);
            log.info("registrar "+curps);
            log.info("registrar2 "+registrar.getEmail());
            boolean isRegistred = stringRedisTemplate.opsForSet().isMember(curps, registrar.getEmail());
            log.info("isRegistred "+isRegistred);
            if(!isRegistred) {
                RegistroEntity registroEntity = new RegistroEntity(curps, registrar.getEmail());
                producer.sendMessage(registroEntity);
                long addResult = stringRedisTemplate.opsForSet().add(curps, registrar.getEmail());
                log.info("Redis Add "+addResult);
                return "registro";
            }
        }
        //no responder a los post invalidos
        log.info("Sin regreso ya existe");
        return null;
    }

//    public @PreDestroy void flushTestDb() {
//        factory.getConnection().flushDb();
//    }

}
