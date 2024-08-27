package com.example.Reward.Receipt.Repository;

import com.example.Reward.Receipt.Entity.PopupCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PopupRepository extends JpaRepository<PopupCheck, Long> {

    @Query(nativeQuery = true, value="SELECT COUNT(*) > 0 FROM popup_check WHERE popup_type = :popupType AND member_id = :memberId")
    Long exists(@Param("popupType") String popupType, @Param("memberId") Long memberId);
}
