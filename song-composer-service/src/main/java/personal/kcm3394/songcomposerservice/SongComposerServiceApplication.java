package personal.kcm3394.songcomposerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SongComposerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SongComposerServiceApplication.class, args);
	}

}
