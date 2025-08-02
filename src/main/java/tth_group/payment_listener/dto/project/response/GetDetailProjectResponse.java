package tth_group.payment_listener.dto.project.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDetailProjectResponse {
    private UUID id;

    private String name;

    private String globalName;

    private String path;

    private String pathDone;

    private String imageLink;

    private String customerKeyRs;

    private int imageCount;

    private double price;

    private double totalAmount;

    private boolean isTesting = false;

    private LocalDateTime deadline = LocalDateTime.now();

    private String statusForCustomer;
}
