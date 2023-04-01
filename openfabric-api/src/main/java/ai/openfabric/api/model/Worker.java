package ai.openfabric.api.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.model.Container;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity()
@Table(name="worker")
@NoArgsConstructor
public class Worker extends Datable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @Getter @Setter
    private String id;

    @Getter @Setter
    @Column(length = 255)
    private String containerId;

    @Getter @Setter
    @Column(length = 255)
    private String name;

    @Getter @Setter
    @Column(length = 255)
    private String imageId;

    @Getter @Setter
    @Column(length = 255)
    private String image;

    @Getter @Setter
    @Column(length = 255)
    private String status;

    @Getter @Setter
    @Column(length = 255)
    private String state;

    @Getter @Setter
    @Column(length = 255)
    private String command;

    @Getter @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private List<WorkerResourceUsage> resourceUsages = new ArrayList<>();

    public static Worker create(Container container) {
        Worker worker = new Worker();
        worker.setContainerId(container.getId());
        worker.setImage(container.getImage());
        worker.setImageId(container.getImageId());
        worker.setState(container.getState());
        worker.setStatus(container.getStatus());
        worker.setCommand(container.getCommand());
        worker.setName(container.getImage());

        return worker;
    } 

}
