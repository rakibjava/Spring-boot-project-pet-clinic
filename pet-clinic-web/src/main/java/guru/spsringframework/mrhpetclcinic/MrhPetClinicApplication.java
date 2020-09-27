package guru.spsringframework.mrhpetclcinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"guru.spsringframework.mrhpetclcinic","guru.springframework.mrhpetclcinic.service"})
public class MrhPetClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrhPetClinicApplication.class, args);
    }

}
