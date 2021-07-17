package ac.cn.saya.uniteauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UniteAuthApplication {

    /**
     * http://localhost:9001/oauth/authorize?client_id=client_2&response_type=code&redirect_uri=https://saya.ac.cn
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(UniteAuthApplication.class, args);
    }

}
