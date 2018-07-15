package nian.shop;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
/**
 * 
 * @author Niantianlei
 * @date 2018年5月17日 下午3:43:40
 */
@SpringBootApplication
@EnableTransactionManagement
public class App {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }
    
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
    	return builder.build();
    }
}