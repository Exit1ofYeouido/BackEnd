package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.PopupCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PopupCheckRepository extends JpaRepository<PopupCheck,Long> {

    @Query(nativeQuery = true,value="SELECT * FROM popup_check as p WHERE :type=p.popup_type AND :memId=p.member_id")
    PopupCheck findByTypeAndMemId(@Param("type") String type,@Param("memId") Long memId);
}
