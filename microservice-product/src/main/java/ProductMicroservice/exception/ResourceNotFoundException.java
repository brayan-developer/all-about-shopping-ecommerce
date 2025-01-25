package ProductMicroservice.exception;

import static ProductMicroservice.constants.ErrorMessageConstants.NOT_RESULTS_FOUND_MESSAGE;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String entityName, String fieldName, Object fieldValue) {
        super(String.format(NOT_RESULTS_FOUND_MESSAGE, entityName, fieldName, fieldValue));
    }
}