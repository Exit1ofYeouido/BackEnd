package com.example.Reward.Advertisement.Repository;

import com.example.Reward.Advertisement.Entity.MediaHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaHistoryRepository extends JpaRepository<MediaHistory,Long> {
    List<MediaHistory> findBymemberId(Long memId);
}
