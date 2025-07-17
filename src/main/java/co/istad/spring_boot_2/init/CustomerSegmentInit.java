package co.istad.spring_boot_2.init;

import co.istad.spring_boot_2.domain.CustomerSegment;
import co.istad.spring_boot_2.repository.CustomerSegmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerSegmentInit {

    private final CustomerSegmentRepository customerSegmentRepository;

    @PostConstruct
    public void init(){
        if(customerSegmentRepository.count() == 0) {
            CustomerSegment regular = new CustomerSegment();
            regular.setSegmentName("REGULAR");
            regular.setDescription("Regular Customer");
            regular.setIsDeleted(false);

            CustomerSegment silver = new CustomerSegment();
            silver.setSegmentName("SILVER");
            silver.setDescription("Silver Customer");
            silver.setIsDeleted(false);

            CustomerSegment gold = new CustomerSegment();
            gold.setSegmentName("GOLD");
            gold.setDescription("Gold Customer");
            gold.setIsDeleted(false);

            customerSegmentRepository.saveAll(
                    List.of(regular, silver, gold)
            );
        }
    }
}
