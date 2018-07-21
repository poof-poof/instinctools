package com.instinctools.textapp.controllers;

import com.instinctools.textapp.services.TextService;
import org.springframework.web.bind.annotation.*;

@RestController
public class TextController {
    private final TextService service;

    public TextController(TextService service) {
        this.service = service;
    }

    @RequestMapping(value = "/words",method = RequestMethod.POST)
    public String topWords(@RequestBody String fileName){
        return service.topWords(fileName);
    }

    @RequestMapping(value = "/brackets",method = RequestMethod.POST)
    public String checkBrackets(@RequestBody String fileName){
        return service.checkBrackets(fileName);
    }
}
