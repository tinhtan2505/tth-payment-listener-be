package tth_group.payment_listener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tth_group.payment_listener.entity.TblVnpayCallback;

import java.util.UUID;

public interface TblVnpayCallbackRepository extends JpaRepository<TblVnpayCallback, UUID> {
    @Modifying
    @Query(value = """
      UPDATE tbl_vnpay_callback
      SET ket_qua = CAST(:json AS jsonb)
      WHERE id = :id
      """, nativeQuery = true)
    void updateKetQuaJson(@Param("id") UUID id, @Param("json") String json);
}