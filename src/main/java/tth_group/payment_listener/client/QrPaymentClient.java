package tth_group.payment_listener.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tth_group.payment_listener.dto.his_tth.CustomResponse;
import tth_group.payment_listener.dto.response.VnpayCallbackResponse;

import java.util.Map;

@Component
public class QrPaymentClient {

    private final WebClient be1WebClient;

    public QrPaymentClient(WebClient be1WebClient) {
        this.be1WebClient = be1WebClient;
    }

    public CustomResponse<Map<String, Object>> getByBillNumber(String billNumber) {
        try {
            return be1WebClient.get()
                    .uri("/qr-payment/{billNumber}", billNumber)
                    .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkZXZ0ZXJpYS5jb20iLCJzdWIiOiJ0aW5obnEiLCJleHAiOjE3ODYwODgwMjksImN1c3RvbUNsYWltIjoiY3VzdG9tIiwidXNlcklkIjoiNDYyMmRlZjgtOThlYy00NTE0LTllMGYtNTY4ZGVjYzRiMTU0IiwiaWF0IjoxNzU0NTUyMDI5fQ.AfEkSAwRMPcNJ16E9DRzQEiB91BR8AVGKq3Px0Sv6U2At-5COQqTnu8rMRfLlgrb0Fm9E8Ovg_UolRAWla6IXA") // nếu cần
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<CustomResponse<Map<String, Object>>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            return new CustomResponse<>(ex.getResponseBodyAsString(), ex.getRawStatusCode(), null);
        } catch (Exception e) {
            return new CustomResponse<>("Internal error", 500, null);
        }
    }

    public CustomResponse<Map<String, Object>> sendPaymentResult(VnpayCallbackResponse rep) {
        try {
            return be1WebClient.post()
                    .uri("/thanh-toan-online/payment-result")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkZXZ0ZXJpYS5jb20iLCJzdWIiOiJ0aW5obnEiLCJleHAiOjE3ODYwODgwMjksImN1c3RvbUNsYWltIjoiY3VzdG9tIiwidXNlcklkIjoiNDYyMmRlZjgtOThlYy00NTE0LTllMGYtNTY4ZGVjYzRiMTU0IiwiaWF0IjoxNzU0NTUyMDI5fQ.AfEkSAwRMPcNJ16E9DRzQEiB91BR8AVGKq3Px0Sv6U2At-5COQqTnu8rMRfLlgrb0Fm9E8Ovg_UolRAWla6IXA") // nếu cần
                    .bodyValue(rep)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<CustomResponse<Map<String, Object>>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            // BE2 trả 4xx/5xx -> lấy body/text + status
            return new CustomResponse<>(ex.getResponseBodyAsString(), ex.getRawStatusCode(), null);
        } catch (Exception e) {
            return new CustomResponse<>("Internal error", 500, null);
        }
    }

}