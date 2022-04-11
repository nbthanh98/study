import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/demo")
public class DemoController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("heelo", HttpStatus.OK);
    }

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public ResponseEntity<String> hello2() {
        return new ResponseEntity<>("heelo", HttpStatus.OK);
    }

}
