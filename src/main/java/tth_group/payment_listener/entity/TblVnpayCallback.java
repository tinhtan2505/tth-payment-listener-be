package tth_group.payment_listener.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tth_group.payment_listener.dto.QrCodeItemPayment;
import tth_group.payment_listener.dto.request.VnpayCallbackRequest;
import tth_group.payment_listener.dto.response.VnpayCallbackResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "tbl_vnpay_callback",
        indexes = {
                @Index(name = "idx_vnpay_code", columnList = "code"),
                @Index(name = "idx_vnpay_created_at", columnList = "ngay_tao")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TblVnpayCallback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @NotBlank
    @Size(max = 10)
    @Column(name = "code", length = 10, nullable = false)
    String code;

    @NotBlank
    @Size(max = 100)
    @Column(name = "message", length = 100, nullable = false)
    String message;

    @NotBlank
    @Size(max = 10)
    @Column(name = "msg_type", length = 10, nullable = false)
    String msgType;

    @NotBlank
    @Size(max = 20)
    @Column(name = "txn_id", length = 20, nullable = false)
    String txnId;

    @NotBlank
    @Size(max = 10)
    @Column(name = "qr_trace", length = 10, nullable = false)
    String qrTrace;

    @NotBlank
    @Size(max = 10)
    @Column(name = "bank_code", length = 10, nullable = false)
    String bankCode;

    @Size(max = 20)
    @Column(name = "mobile", length = 20)
    String mobile;

    @Size(max = 30)
    @Column(name = "account_no", length = 30)
    String accountNo;

    @NotBlank
    @Size(max = 13)
    @Pattern(regexp = "\\d{1,13}", message = "amount must be numeric up to 13 digits")
    @Column(name = "amount_raw", length = 13, nullable = false)
    String amountRaw;

    @Column(name = "amount_numeric", precision = 18, scale = 2)
    BigDecimal amountNumeric;

    @NotBlank
    @Size(max = 14)
    @Pattern(regexp = "\\d{14}", message = "payDateRaw must be 14 digits (yyyyMMddHHmmss)")
    @Column(name = "pay_date_raw", length = 14, nullable = false)
    String payDateRaw;

    @Column(name = "pay_date")
    LocalDateTime payDate;

    @NotBlank
    @Size(max = 20)
    @Column(name = "merchant_code", length = 20, nullable = false)
    String merchantCode;

    @NotBlank
    @Size(max = 8)
    @Column(name = "terminal_id", length = 8, nullable = false)
    String terminalId;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    String name;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    String phone;

    @Size(max = 14)
    @Column(name = "province_id", length = 14)
    String provinceId;

    @Size(max = 14)
    @Column(name = "district_id", length = 14)
    String districtId;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    String address;

    @Email
    @Size(max = 100)
    @Column(name = "email", length = 100)
    String email;

    @Size(max = 10)
    @Column(name = "ccy", length = 10)
    String ccy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "add_data", columnDefinition = "jsonb")
    List<QrCodeItemPayment> addData;

    @NotBlank
    @Size(max = 64)
    @Column(name = "checksum", length = 64, nullable = false)
    String checksum;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_json", columnDefinition = "jsonb", nullable = false)
    JsonNode rawJson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ket_qua", columnDefinition = "jsonb")
    VnpayCallbackResponse ketQua;


    public static TblVnpayCallback fromRequest(
            VnpayCallbackRequest req,
            JsonNode rawJson,
            BigDecimal amountNumeric,
            LocalDateTime parsedPayDate
    ) {
        return TblVnpayCallback.builder()
                .code(req.getCode())
                .message(req.getMessage())
                .msgType(req.getMsgType())
                .txnId(req.getTxnId())
                .qrTrace(req.getQrTrace())
                .bankCode(req.getBankCode())
                .mobile(req.getMobile())
                .accountNo(req.getAccountNo())
                .amountRaw(req.getAmount())
                .amountNumeric(amountNumeric)
                .payDateRaw(req.getPayDate())
                .payDate(parsedPayDate)
                .merchantCode(req.getMerchantCode())
                .terminalId(req.getTerminalId())
                .name(req.getName())
                .phone(req.getPhone())
                .provinceId(req.getProvinceId())
                .districtId(req.getDistrictId())
                .address(req.getAddress())
                .email(req.getEmail())
                .ccy(req.getCcy())
                .addData(req.getAddData())
                .checksum(req.getChecksum())
                .rawJson(rawJson)
                .build();
    }
}
