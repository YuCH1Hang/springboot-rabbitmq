package com.space.rabbitmq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqApplication {
        private static final Logger logger = LoggerFactory.getLogger(RabbitmqApplication.class);  
	public static void main(String[] args) {
		SpringApplication.run(RabbitmqApplication.class, args);
		logger.trace("trace...");
	        logger.debug("debug...");
		logger.info("infoÈ..");
		logger.warn("warnÈ..");
		logger.error("error...");
	}
}
