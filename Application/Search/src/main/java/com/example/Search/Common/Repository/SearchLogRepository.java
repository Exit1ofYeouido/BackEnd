package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchLogRepository extends JpaRepository<SearchLog,Long> {
}
