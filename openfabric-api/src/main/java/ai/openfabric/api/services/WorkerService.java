package ai.openfabric.api.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.model.Container;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerResourceUsage;
import ai.openfabric.api.repository.WorkerRepository;


@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepo;

    @Autowired
    private DockerService dockerService;

    @Autowired
    private WorkerResourceUsageService statService;

    @Scheduled(fixedDelay =  50000)
    private void registerNewWorkers() {
        List<Container> containers = dockerService.getAllContainers();

        for (Container container : containers) {
            Optional<Worker> result = workerRepo.findByContainerId(container.getId());
            Worker worker;
            if (result.isEmpty()) {
                worker = Worker.create(container);
            } else {
                worker = result.get();
                worker.setState(container.getState());
                worker.setStatus(container.getStatus());
            }
            workerRepo.save(worker);
        }
    }


    public Page<Worker> getWorkers(Pageable pageable) {
        registerNewWorkers();
        return (Page<Worker>) workerRepo.findAll(pageable);
    }

    public Worker getWorker(String id) {
        Optional<Worker> currWorker =  workerRepo.findById(id);
        if (currWorker.isPresent()) {
            return currWorker.get();
        }
        
        throw new NoSuchElementException();
    }


    public List<WorkerResourceUsage> getStats(String id) {
        if (!workerRepo.existsById(id)) {
            throw new NoSuchElementException();
        }
        Optional<Worker> data = workerRepo.findById(id);
        Worker worker = data.get();

        return statService.getWorkerStats(worker);
    }

    public void stopWorker(String workerId) {
        if (!workerRepo.existsById(workerId)) {
            throw new NoSuchElementException();
        }

        Optional<Worker> result = workerRepo.findById(workerId);
        Worker worker = result.get();
        
        dockerService.stopContainer(worker.getContainerId());
    }
    
    public void startWorker(String workerId) {
        if (!workerRepo.existsById(workerId)) {
            throw new NoSuchElementException();
        }

        Optional<Worker> result = workerRepo.findById(workerId);
        Worker worker = result.get();

        dockerService.startContainer(worker.getContainerId());
    }
}
