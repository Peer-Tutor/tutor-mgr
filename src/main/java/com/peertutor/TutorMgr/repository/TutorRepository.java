package com.peertutor.TutorMgr.repository;

import com.peertutor.TutorMgr.model.Tutor;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    List<Tutor> findByLastName(String lastName);
    Tutor findById(long id);
}

