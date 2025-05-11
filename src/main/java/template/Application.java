package template;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Items API"))
public class Application {

    public static void main(String[] args) {
        run(Application.class, args);
    }

}
