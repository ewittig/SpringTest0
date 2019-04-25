package hello;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@RestController
public class GreetingController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @RequestMapping("/ServiceC")
    public String serviceC() throws InterruptedException {
        // Hashmap containing Header key/val
        Map<String, String> map = new HashMap<>();
        //build HttpHeader
        HttpHeaders header = new HttpHeaders();
        header.setAll(map);
        //Sleep
        Thread.sleep(250L);
        logger.info("In Service C ***************");
        //Post to downstream service
        String rs = restTemplate.postForEntity("http://localhost:9393/ServiceD", new HttpEntity(header), String.class).getBody();
        return rs;
    }


    @RequestMapping("/ServiceD")
    public String serviceD() throws InterruptedException {

        Enumeration<String> e = request.getHeaderNames();
        Map<String, String> spanMap = new HashMap<>();

        while (e.hasMoreElements()) {
            // add the names of the request headers into the spanMap
            String key = e.nextElement();
            String value = request.getHeader(key);
            spanMap.put(key, value);
        }

        Thread.sleep(230L);
        logger.info("In Service D ***************");

        return "Service D\n";
    }


}
