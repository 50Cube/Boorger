package pl.lodz.p.it.boorger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry
@SpringBootApplication
public class BoorgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoorgerApplication.class, args);
	}

}
