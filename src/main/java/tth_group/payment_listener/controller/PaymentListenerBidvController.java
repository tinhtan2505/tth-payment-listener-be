package tth_group.payment_listener.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tth_group.payment_listener.dto.request.VnpayCallbackRequest;
import tth_group.payment_listener.dto.response.VnpayCallbackResponse;
import tth_group.payment_listener.utils.ThanhToanOnlineUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("tthgroup/api")
@Tag(name = "Payment Listener Bidv")
public class PaymentListenerBidvController {
    @Value("${vnpay.secretKey}")
    private String secretKey;

    @PostMapping(
            value = "thanhtoanqrcode",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VnpayCallbackResponse> onPaymentResult(
            @RequestBody VnpayCallbackRequest req) {
        // 1) Tính lại checksum
        String raw = String.join("|",
                req.getCode(), req.getMsgType(), req.getTxnId(), req.getQrTrace(),
                req.getBankCode(), req.getMobile(), req.getAccountNo(),
                req.getAmount(), req.getPayDate(),
                req.getMerchantCode(), secretKey);
        String expected = ThanhToanOnlineUtils.encodeMD5LowCase(raw);
        if (!expected.equalsIgnoreCase(req.getChecksum())) {
            // checksum sai -> báo lỗi
            String respChk = ThanhToanOnlineUtils.encodeMD5LowCase("06" + secretKey);
            return ResponseEntity
                    .badRequest()
                    .body(new VnpayCallbackResponse("06", "sai thông tin xác thực", respChk));
        }

        // 2) Xử lý nghiệp vụ: cập nhật trạng thái thanh toán
        if ("00".equals(req.getCode())) {
//            hospitalFeeService.markAsPaid(req.getTxnId());
        } else {
//            hospitalFeeService.markAsFailed(req.getTxnId(), req.getCode());
        }

        // 3) Trả về BIDV: báo nhận thành công
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("txnId", req.getTxnId());
        String respChecksum = ThanhToanOnlineUtils.generateSecureCode("00" + secretKey);
        VnpayCallbackResponse resp = new VnpayCallbackResponse("00",
                "đặt hàng thành công", dataMap, respChecksum);
        return ResponseEntity.ok(resp);
    }
}
