package poc.infrastructure;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandRepository {
    @Autowired
    CommandJpaRepository commandJpaRepository;

    public final void finishCommand(final String commandId) {
        Optional<CommandEntry> cOpt = this.commandJpaRepository.findById(commandId);
        if (!cOpt.isPresent()) {
            throw new RuntimeException("No command found for id: " + commandId);
        } else {
            CommandEntry c = cOpt.get();
            c.setStatus("FINISHED");
            this.commandJpaRepository.save(c);
        }

    }
}
