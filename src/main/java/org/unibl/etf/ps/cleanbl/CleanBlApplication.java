package org.unibl.etf.ps.cleanbl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.unibl.etf.ps.cleanbl.config.SwaggerConfiguration;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class CleanBlApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanBlApplication.class, args);
	}


}
