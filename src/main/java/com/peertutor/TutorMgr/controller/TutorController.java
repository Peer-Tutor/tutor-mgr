package com.peertutor.TutorMgr.controller;

import com.peertutor.TutorMgr.model.viewmodel.request.TutorProfileReq;
import com.peertutor.TutorMgr.model.viewmodel.response.TutorProfileRes;
import com.peertutor.TutorMgr.repository.TutorRepository;
import com.peertutor.TutorMgr.service.AuthService;
import com.peertutor.TutorMgr.service.TutorService;
import com.peertutor.TutorMgr.service.dto.TutorCriteria;
import com.peertutor.TutorMgr.service.dto.TutorDTO;
import com.peertutor.TutorMgr.util.AppConfig;
import io.github.jhipster.web.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/tutor-mgr")
public class TutorController {
    @Autowired
    AppConfig appConfig;
    @Autowired
    private TutorRepository tutorRepository;// = new CustomerRepository();
    @Autowired
    private TutorService tutorService;
    @Autowired
    private AuthService authService;

    @GetMapping(path = "/health")
    public @ResponseBody String healthCheck() {
        return "Ok 2";
    }

    @PostMapping(path = "/tutor")
    public @ResponseBody ResponseEntity<TutorProfileRes> createTutorProfile(@RequestBody @Valid TutorProfileReq req) {
        boolean result = authService.getAuthentication(req.name, req.sessionToken);
        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        TutorDTO savedUser;

        savedUser = tutorService.createTutorProfile(req);

        if (savedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        com.peertutor.TutorMgr.model.viewmodel.response.TutorProfileRes res = new TutorProfileRes();
        res.displayName = savedUser.getDisplayName();
        res.introduction = savedUser.getIntroduction();
        res.subjects = savedUser.getSubjects();
        res.certificates = savedUser.getCertificates();
        res.id = savedUser.getId();

        return ResponseEntity.ok().body(res);
    }

    @GetMapping(path = "/tutor")
    public @ResponseBody ResponseEntity<List<TutorProfileRes>> getTutorProfile(@RequestBody @Valid TutorProfileReq req, Pageable pageable) {
        boolean result = authService.getAuthentication(req.name, req.sessionToken);
        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        TutorCriteria criteria = new TutorCriteria(req);
        Page<TutorDTO> page = tutorService.getTutorByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        List<TutorDTO> test = page.getContent();
        List<TutorProfileRes> filteredTutors = page.getContent().stream().map(tutorDTO -> {
            TutorProfileRes res = new TutorProfileRes();
            res.displayName = tutorDTO.getDisplayName();
            res.introduction = tutorDTO.getIntroduction();
            res.subjects = tutorDTO.getSubjects();
            res.certificates = tutorDTO.getCertificates();
            res.id = tutorDTO.getId();

            return res;
        }).collect(Collectors.toList());
        return ResponseEntity.ok().headers(headers).body(filteredTutors);
    }
}
