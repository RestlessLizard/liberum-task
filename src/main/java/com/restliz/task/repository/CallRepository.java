package com.restliz.task.repository;

import com.restliz.task.entity.Call;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRepository extends org.springframework.data.jpa.repository.JpaRepository<Call, Long> {
//    @Query("SELECT c FROM Call c WHERE c.")
//    List<Call> findAllByTimestamp(long timestamp);
}
