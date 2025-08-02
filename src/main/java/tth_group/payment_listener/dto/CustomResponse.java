package tth_group.payment_listener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponse<T> {
    private String message;
    private int status;
    private T result;
    private Object metadata;

    public CustomResponse(String message, int status, T result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    // Factory method for success response
    public static <T> CustomResponse<T> success(T result, String message) {
        return new CustomResponse<>(message, HttpStatus.OK.value(), result);
    }

    // Factory method for error response
    public static <T> CustomResponse<T> error(String message, int status) {
        return new CustomResponse<>(message, status, null);
    }

    @Override
    public String toString() {
        return "CustomResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", result=" + result +
                ", metadata=" + metadata +
                '}';
    }
}

