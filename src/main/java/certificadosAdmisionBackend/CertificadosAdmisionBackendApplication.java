package certificadosAdmisionBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "certificadosAdmisionBackend.repository")
public class CertificadosAdmisionBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertificadosAdmisionBackendApplication.class, args);
    }

}
