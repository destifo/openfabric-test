package ai.openfabric.api.model;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.model.Statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "worker_resource_usage")
@NoArgsConstructor
public class WorkerResourceUsage extends Datable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @Getter
    private String id;

    @Getter @Setter
    private Long memoryUsage;

    @Getter @Setter
    private Long cpuUsage;
    
    @Getter @Setter
    private Long diskUsage;

    @Getter @Setter
    private Long networkTransmitted;

    @Getter @Setter
    private Long networkReceived;

    @Getter @Setter
    private Long ioUsage;

    @Getter @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker; 


    public static WorkerResourceUsage create(Statistics stats, Worker worker) {
        WorkerResourceUsage workerUsage = new WorkerResourceUsage();

        workerUsage.setWorker(worker);
        if (stats.getCpuStats() != null && stats.getCpuStats().getCpuUsage() != null) {
            workerUsage.setCpuUsage(stats.getCpuStats().getCpuUsage().getTotalUsage());
        }
        
        if (stats.getMemoryStats() != null) {
            workerUsage.setMemoryUsage(stats.getMemoryStats().getUsage());
        }

        if (stats.getNetworks() != null) {
            workerUsage.setNetworkReceived(stats.getNetworks().get("eth0").getRxBytes());
            workerUsage.setNetworkTransmitted(stats.getNetworks().get("eth0").getTxBytes());
        }

        return workerUsage;
    }
}
