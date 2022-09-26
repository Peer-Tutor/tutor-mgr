package com.peertutor.TutorMgr.service;

import com.peertutor.TutorMgr.model.Tutor;
import com.peertutor.TutorMgr.model.viewmodel.request.TutorProfileReq;
import com.peertutor.TutorMgr.repository.TutorRepository;
import com.peertutor.TutorMgr.service.dto.TutorCriteria;
import com.peertutor.TutorMgr.service.dto.TutorDTO;
import com.peertutor.TutorMgr.service.mapper.TutorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TutorService {
    private static final Logger logger = LoggerFactory.getLogger(TutorService.class);
    @Autowired
    private final TutorMapper tutorMapper;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private TutorQueryService tutorQueryService;

    public TutorService(TutorRepository tutorRepository, TutorMapper tutorMapper) {
        this.tutorRepository = tutorRepository;
        this.tutorMapper = tutorMapper;
    }

    public TutorDTO getTutorProfileByAccountName(String accountName) {
        Tutor tutor = tutorRepository.findByAccountName(accountName);

        if (tutor == null) {
            return null;
        }
        TutorDTO result = tutorMapper.toDto(tutor);

        return result;
    }

    public TutorDTO getTutorProfileById(Long id) {
        Optional<Tutor> tutor = tutorRepository.findById(id);

        if (!tutor.isPresent()) {
            return null;
        }
        TutorDTO result = tutorMapper.toDto(tutor.get());

        return result;
    }

    public TutorDTO createTutorProfile(TutorProfileReq req) {
        Tutor tutor = tutorRepository.findByAccountName(req.name);

        if (tutor == null) {
            tutor = new Tutor();
            tutor.setAccountName(req.name);
        }

        if (req.displayName != null && !req.displayName.trim().isEmpty()) {
            tutor.setDisplayName(req.displayName);
        } else {
            tutor.setDisplayName(req.name);
        }

        tutor.setIntroduction(req.introduction);
        tutor.setSubjects(req.subjects);
        tutor.setCertificates(req.certificates);

        try {
            tutor = tutorRepository.save(tutor);
        } catch (Exception e) {
            logger.error("Tutor Profile Creation Failed: " + e.getMessage());
            return null;
        }

        TutorDTO result = tutorMapper.toDto(tutor);

        return result;
    }

    public Page<TutorDTO> getTutorByCriteria(TutorCriteria criteria, Pageable pageable) {
        Page<TutorDTO> page = tutorQueryService.findByCriteria(criteria, pageable);
        return page;
    }
}
