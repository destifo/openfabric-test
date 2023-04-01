package ai.openfabric.api.model;

import lombok.Getter;
import lombok.Setter;

public class ErrorResponse {

    @Getter @Setter
    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
