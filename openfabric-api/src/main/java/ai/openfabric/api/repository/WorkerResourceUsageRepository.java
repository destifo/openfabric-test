package ai.openfabric.api.repository;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerResourceUsage;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface WorkerResourceUsageRepository extends CrudRepository<WorkerResourceUsage, String> {
    List<WorkerResourceUsage> findAllByWorker(Worker worker);
}
