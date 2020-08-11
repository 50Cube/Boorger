package pl.lodz.p.it.boorger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BoorgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoorgerApplication.class, args);
	}

}
