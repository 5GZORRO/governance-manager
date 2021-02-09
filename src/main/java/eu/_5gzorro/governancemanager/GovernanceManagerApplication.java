package eu._5gzorro.governancemanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GovernanceManagerApplication {

	private static final Logger log = LogManager.getLogger(GovernanceManagerApplication.class);

	/* Init async logging */
	static {
		System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
	}

	public static void main(String[] args) {
		SpringApplication.run(GovernanceManagerApplication.class, args);
	}

}
