package com.example.Reward.Common.Repository;

import com.example.Reward.Common.Entity.Event;
import com.example.Reward.Receipt.Dto.out.EventDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    Event findByEnterpriseNameContaining(String enterprisename);

    List<Event> findByGreaterThanAndContentId(Long rewardAmount, Long contentId);

    @Query(nativeQuery = true, value = "SELECT code FROM event WHERE enterprise_name = ? AND content_id = 2")
    String findCodeByEnterpriseName(@Param("enterpriseName") String enterpriseName);

    Event findByEnterpriseName(String enterpriseName);

}
