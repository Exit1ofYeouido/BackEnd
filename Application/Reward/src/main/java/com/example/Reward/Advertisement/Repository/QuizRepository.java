package com.example.Reward.Advertisement.Repository;

import com.example.Reward.Advertisement.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    Quiz findByMediaLinkId(Long mediaId);
}
