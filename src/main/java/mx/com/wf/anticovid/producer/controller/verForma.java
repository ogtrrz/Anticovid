package mx.com.wf.anticovid.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/response")
public class verForma {
    @PostMapping("/postbody")
    public String postBody(@RequestBody String fullName) {
        log.info(fullName);
        return fullName;
    }
}
