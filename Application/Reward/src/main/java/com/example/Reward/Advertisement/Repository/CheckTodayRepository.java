package com.example.Reward.Advertisement.Repository;

import com.example.Reward.Advertisement.Entity.CheckToday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckTodayRepository extends JpaRepository<CheckToday,Long> {
    List<CheckToday> findBymemberId(Long memId);

    @Query(nativeQuery = true,value= "SELECT * FROM check_today as c WHERE :enterpriseName =c.enterprise_name AND c.member_id =:memId")
    CheckToday findByEnterpriseNameANDMemberId(@Param("enterpriseName") String enterpriseName,@Param("memId") Long memId);
}
