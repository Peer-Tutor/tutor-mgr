package com.peertutor.TutorMgr.repository;

import com.peertutor.TutorMgr.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Tutor findByAccountName(String accountName);
}

