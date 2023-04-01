package ai.openfabric.api.repository;

import ai.openfabric.api.model.Worker;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface WorkerRepository extends CrudRepository<Worker, String> {
    Page<Worker> findAll(Pageable pageable);
    Optional<Worker> findByContainerId(String containerId);
    boolean existsByContainerId(String containerId);
}
