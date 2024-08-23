package com.example.Reward.Advertisement.Repository;

import com.example.Reward.Advertisement.Entity.CheckToday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckTodayRepository extends JpaRepository<CheckToday,Long> {
    List<CheckToday> findBymemberId(Long memId);
}
