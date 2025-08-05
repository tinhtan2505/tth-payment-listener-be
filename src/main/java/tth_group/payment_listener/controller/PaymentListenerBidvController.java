package tth_group.payment_listener.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tth_group.payment_listener.dto.request.VnpayCallbackRequest;
import tth_group.payment_listener.dto.response.VnpayCallbackResponse;
import tth_group.payment_listener.utils.ThanhToanOnlineUtils;

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
                req.getBankCode(), // thêm mobile, accountNo nếu có
                req.getAmount(), req.getPayDate(),
                req.getMerchantCode(), secretKey);
        String expected = ThanhToanOnlineUtils.generateSecureCode(raw);
        if (!expected.equalsIgnoreCase(req.getChecksum())) {
            // checksum sai -> báo lỗi
            String respChk = ThanhToanOnlineUtils.generateSecureCode("96" + secretKey);
            return ResponseEntity
                    .badRequest()
                    .body(new VnpayCallbackResponse("96", "Checksum không hợp lệ", respChk));
        }

        // 2) Xử lý nghiệp vụ: cập nhật trạng thái thanh toán
        //    Ví dụ: code="00" -> paid; khác -> failed
        if ("00".equals(req.getCode())) {
//            hospitalFeeService.markAsPaid(req.getTxnId());
        } else {
//            hospitalFeeService.markAsFailed(req.getTxnId(), req.getCode());
        }

        // 3) Trả về BIDV: báo nhận thành công
        String respChecksum = ThanhToanOnlineUtils.generateSecureCode("00" + secretKey);
        VnpayCallbackResponse resp = new VnpayCallbackResponse("00",
                "Đã nhận callback", respChecksum);
        return ResponseEntity.ok(resp);
    }
}
