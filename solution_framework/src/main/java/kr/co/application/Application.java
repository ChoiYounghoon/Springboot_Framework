package kr.co.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import kr.co.application.config.AppConfig;
import kr.co.application.config.MybatisConfig;
import kr.co.application.task.test.TestProducer;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackages = "kr.co.application.*")
@Import({MybatisConfig.class})
@Slf4j
public class Application implements CommandLineRunner {
	
	private @Autowired TestProducer tp;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		AppConfig appconfig = AppConfig.getInstance();
		
		//log.info(AppConfig.toString());
		
		log.info(appconfig.toString());
		
		tp.start();
	}
}
