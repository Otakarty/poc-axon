package poc.exposition.api;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/axon")
public class AxonController {
    @Autowired
    EventProcessingConfiguration eventProcessingConfiguration;

    // Options: "persons"
    @PostMapping("/replay/{aggregates}")
    // TODO: replace with enum
    public void replayAllEvents(@PathVariable final String aggregates) {
        this.eventProcessingConfiguration
            // .eventProcessors().entrySet().stream().forEach(set -> {
            // TrackingEventProcessor trackingEventProcessor = (TrackingEventProcessor) set.getValue();
            // trackingEventProcessor.shutDown();
            // trackingEventProcessor.resetTokens(); // (1)
            // trackingEventProcessor.start();
            // });
            .eventProcessorByProcessingGroup(aggregates + "Processor", TrackingEventProcessor.class)
            .ifPresent(trackingEventProcessor -> {
                trackingEventProcessor.shutDown();
                trackingEventProcessor.resetTokens(); // (1)
                trackingEventProcessor.start();
            });

    }
}
