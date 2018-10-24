package com.bnpparibas.axon.exposition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
    public AggregateFactory<Sale> saleAggregateFactory() {
        SpringPrototypeAggregateFactory<Sale> aggregateFactory = new SpringPrototypeAggregateFactory<>();
        aggregateFactory.setPrototypeBeanName("sale");

        return aggregateFactory;
    }

    @Bean
    public Cache cache(){
        return new WeakReferenceCache();
    }

    @Bean
    public SpringAggregateSnapshotter snapshotter(ParameterResolverFactory parameterResolverFactory, EventStore eventStore, TransactionManager transactionManager) {
        Executor executor = Executors.newSingleThreadExecutor(); //Or any other executor of course
        return new SpringAggregateSnapshotter(eventStore, parameterResolverFactory, executor, transactionManager);
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(Snapshotter snapshotter) throws Exception {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 3);
    }

    @Bean
    public Repository<Sale> saleRepository(EventStore eventStore, SnapshotTriggerDefinition snapshotTriggerDefinition, Cache cache) {
        return new CachingEventSourcingRepository<>(new GenericAggregateFactory<>(Sale.class), eventStore, cache, snapshotTriggerDefinition);
    }

}