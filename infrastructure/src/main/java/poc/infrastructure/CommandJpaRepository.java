package poc.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CommandJpaRepository extends JpaRepository<CommandEntry, String> {
    List<CommandEntry> findAllByOrderId(String orderId);
}
