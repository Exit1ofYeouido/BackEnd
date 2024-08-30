package com.example.Reward.Common.Repository;

import com.example.Reward.Advertisement.Entity.MediaLink;
import com.example.Reward.Common.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    Event findByEnterpriseNameContaining(String enterprisename);

    @Query(nativeQuery = true, value = "SELECT event_id, enterprise_name FROM event WHERE reward_amount > 1 AND content_id = 2")
    List<Event> findEventIdAndEnterpriseNameByRewardAmount();

    @Query(nativeQuery = true, value = "SELECT code FROM event WHERE enterprise_name = ? AND content_id = 2")
    String findCodeByEnterpriseName(@Param("enterpriseName") String enterpriseName);

    @Query(nativeQuery = true, value = "SELECT event_id, code, enterprise_name, reward_amount FROM event WHERE enterprise_name = ? AND content_id = 2")
    Event findByEnterpriseName(@Param("enterpriseName") String enterpriseName);

}
