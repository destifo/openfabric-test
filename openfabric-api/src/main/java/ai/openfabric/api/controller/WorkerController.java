package ai.openfabric.api.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerResourceUsage;
import ai.openfabric.api.services.WorkerService;


@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {

    WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Autowired
    private final WorkerService workerService;

    @GetMapping()
    public @ResponseBody ResponseEntity<Page<Worker>> getWorkers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            return ResponseEntity.ok(workerService.getWorkers(pageable));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Worker> getWorker(@PathVariable String id) {
        try {
            return ResponseEntity.ok(workerService.getWorker(id));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(path = "/stats/{id}")
    public @ResponseBody ResponseEntity<List<WorkerResourceUsage>> getWorkerStats(@PathVariable String id) {
        try {
            return ResponseEntity.ok(workerService.getStats(id));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/start/{id}")
    public @ResponseBody ResponseEntity<Worker> startWorker(@PathVariable String id) {

        try {
            workerService.startWorker(id);
            Worker worker = workerService.getWorker(id);
            return ResponseEntity.ok(worker);
        } catch(Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/stop/{id}")
    public @ResponseBody ResponseEntity<Worker> stopWorker(@PathVariable String id) {

        try {
            workerService.stopWorker(id);
            Worker worker = workerService.getWorker(id);
            return ResponseEntity.ok(worker);
        } catch(Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

}