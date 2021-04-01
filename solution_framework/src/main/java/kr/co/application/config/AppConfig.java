package kr.co.application.config;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@ToString
public class AppConfig {
	
	static AppConfig instance;
	public String name;
	public String description;
	public String url;
	public int queueCnt;
	
	private AppConfig () {}
	
	public static AppConfig getInstance() {
		if(instance == null){ //최초 한번만 new 연산자를 통하여 메모리에 할당한다.
			instance = new AppConfig();
			init();
		}		
		return instance;
	}
	
	
	static void init() {
		try {
			
			ClassLoader loader = AppConfig.class.getClassLoader();
			
			File file = new File(loader.getResource("appconfig.properties").getFile());
			log.info("AppConfig Properties path : " + file.getAbsolutePath());
			
			Properties props = new Properties();
			props.load(new FileReader(file));
			
			instance.name = props.getProperty("app.name");
			instance.description = props.getProperty("app.description");
			instance.url = props.getProperty("app.url");
			instance.queueCnt = Integer.parseInt(props.get("app.queueCnt").toString()); 
			
			log.info("App Config loader complete");
			
		} catch(Exception e) {
			log.error("Config load Error", e);
		}
	}
	
	
	/*
	static {
		
		try {
			
			ClassLoader loader = AppConfig.class.getClassLoader();
			
			File file = new File(loader.getResource("appconfig.properties").getFile());
			log.info("AppConfig Properties path : " + file.getAbsolutePath());
			
			Properties props = new Properties();
			props.load(new FileReader(file));
			
			name = props.getProperty("app.name");
			description = props.getProperty("app.description");
			url = props.getProperty("app.url");
			queueCnt = Integer.parseInt(props.get("app.queueCnt").toString()); 
			
			log.info("App Config loader complete");
			
		} catch(Exception e) {
			log.error("Config load Error", e);
		}
	}
	*/
	
}