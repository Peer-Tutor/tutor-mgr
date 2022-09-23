package com.peertutor.TutorMgr.controller;

import com.peertutor.TutorMgr.model.viewmodel.request.TutorProfileReq;
import com.peertutor.TutorMgr.model.viewmodel.response.TutorProfileRes;
import com.peertutor.TutorMgr.repository.TutorRepository;
import com.peertutor.TutorMgr.service.AuthService;
import com.peertutor.TutorMgr.service.TutorService;
import com.peertutor.TutorMgr.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

        com.peertutor.TutorMgr.service.dto.TutorDTO savedUser;

        savedUser = tutorService.createTutorProfile(req);

        if (savedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        com.peertutor.TutorMgr.model.viewmodel.response.TutorProfileRes res = new TutorProfileRes();
        res.displayName = savedUser.getDisplayName();
        res.introduction = savedUser.getIntroduction();
        res.subjects = savedUser.getSubjects();
        res.certificates = savedUser.getCertificates();

        return ResponseEntity.ok().body(res);
    }

    @GetMapping(path = "/tutor")
    public @ResponseBody ResponseEntity<TutorProfileRes> getTutorProfile(@RequestBody @Valid TutorProfileReq req) {
        boolean result = authService.getAuthentication(req.name, req.sessionToken);
        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        com.peertutor.TutorMgr.service.dto.TutorDTO tutorRetrieved;
        tutorRetrieved = tutorService.getTutorProfile(req.accountName);

        if (tutorRetrieved == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        com.peertutor.TutorMgr.model.viewmodel.response.TutorProfileRes res = new TutorProfileRes();
        res.displayName = tutorRetrieved.getDisplayName();
        res.introduction = tutorRetrieved.getIntroduction();
        res.subjects = tutorRetrieved.getSubjects();
        res.certificates = tutorRetrieved.getCertificates();

        return ResponseEntity.ok().body(res);
    }


}
