package searchengine.routes;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ping controller to test connection
 * */
@RestController
@CrossOrigin()
public class PingController {

    @RequestMapping("/ping")
    public Pong Ping() {
        return new Pong();
    }
}
