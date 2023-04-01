package ai.openfabric.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.model.Container;


@Service
public class DockerService {
    
    @Autowired
    private DockerClient dockerClient;

    public List<Container> getAllContainers() {
        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();

        return containers;
    }

    public void startContainer(String containerId) {
        if (containerId.isEmpty()) {
            throw new IllegalArgumentException();
        }

        StartContainerCmd startContainerCmd = dockerClient.startContainerCmd(containerId);
        startContainerCmd.exec();
    }

    public void stopContainer(String containerId) {
        if (containerId.isEmpty()) {
            throw new IllegalArgumentException();
        }

        StopContainerCmd stopContainerCmd = dockerClient.stopContainerCmd(containerId);
        stopContainerCmd.exec();
    }

}
