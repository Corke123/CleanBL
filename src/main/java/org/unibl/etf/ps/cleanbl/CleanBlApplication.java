package org.unibl.etf.ps.cleanbl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CleanBlApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanBlApplication.class, args);
	}


}
