package com.peertutor.TutorMgr.controller;

import com.peertutor.TutorMgr.model.Tutor;
import com.peertutor.TutorMgr.repository.TutorRepository;
import com.peertutor.TutorMgr.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path="/tutor-mgr")
public class TutorController {
    @Autowired
    AppConfig appConfig;
    @Autowired
    private TutorRepository tutorRepository;// = new CustomerRepository();
    @GetMapping(path="/")
    public @ResponseBody String defaultResponse(){

        System.out.println("appConfig="+ appConfig.toString());
        System.out.println("ver"+ SpringVersion.getVersion());
        return "Hello world Spring Ver = " + SpringVersion.getVersion() + "From Tutor mgr";

    }
    @GetMapping(path="/public-api")
    public @ResponseBody String callPublicApi() {
        String endpoint = "https://api.publicapis.org/entries"; //url+":"+port;
        System.out.println("endpoint" + endpoint);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);

        return response.toString();
    }

    @GetMapping(path="/call-app-bookmark-mgr")
    public @ResponseBody String callAppTwo() {
        String url = appConfig.getBookmarkMgr().get("url");
        String port = appConfig.getBookmarkMgr().get("port");

        String endpoint = url; //+":"+port + "/";
        System.out.println("endpoint" + endpoint);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);

        return response.toString();
    }
    @GetMapping(path="/health")
    public @ResponseBody String healthCheck(){
        return "Ok";
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewCustomer(@RequestBody Map<String, String> customerDTO) {

        // <validation logic here>
        // todo: generalise validation logic

        // <retrieve data from request body>
        System.out.println("customerMap= " +customerDTO);
        String firstName = customerDTO.get("firstName");
        String lastName = customerDTO.get("lastName");
        // create DTO
        Tutor customer = new Tutor(firstName, lastName);

        // dao layer: save object to db
        tutorRepository.save(customer);

        // todo: better logging
        // todo: generalise response message
        return "Saved";
    }
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Tutor> getAllCustomers (){

        return tutorRepository.findAll();
    }


}
