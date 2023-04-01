package ai.openfabric.api.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.InvocationBuilder.AsyncResultCallback;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerResourceUsage;
import ai.openfabric.api.repository.WorkerRepository;
import ai.openfabric.api.repository.WorkerResourceUsageRepository;

@Service
public class WorkerResourceUsageService {

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private WorkerResourceUsageRepository statRepo;

    @Autowired
    private WorkerRepository workerRepo;


    @Scheduled(fixedDelay = 90000)
    private void updateWorkerStatistics() {

        List<Worker> workers = (List<Worker>) workerRepo.findAll();

        for (Worker worker: workers) {
            String containerId = worker.getContainerId();
            
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            AsyncResultCallback<Statistics> callback = new AsyncResultCallback<>();
            statsCmd.exec(callback);
            Statistics stats;

            try {
                stats = callback.awaitResult();
                if (stats == null) {
                    continue;
                }
            } catch (RuntimeException e) {
                continue;
            }
            try {
                callback.close();
            } catch (IOException ex) {}
            WorkerResourceUsage workerUsage = WorkerResourceUsage.create(stats, worker);

            statRepo.save(workerUsage);
        }

    }

    public List<WorkerResourceUsage> getWorkerStats(Worker worker) {
        return statRepo.findAllByWorker(worker);
    }
    
}
