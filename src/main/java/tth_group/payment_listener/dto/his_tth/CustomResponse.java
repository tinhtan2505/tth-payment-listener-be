package tth_group.payment_listener.dto.his_tth;

import lombok.Getter;
import lombok.Setter;

@Setter
public class CustomResponse<T> {
  @Getter
  private String message;
  @Getter
  private int status;
  @Getter
  private T data;
  private Object metadata;

  public CustomResponse(String message, int status, T data) {
    this.message = message;
    this.status = status;
    this.data = data;
  }

}
