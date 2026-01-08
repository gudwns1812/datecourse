package com.datecourse.controller;

import com.datecourse.service.DateCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/datecourse")
@RequiredArgsConstructor
public class DateCourseController {

    private final DateCourseService service;

    @GetMapping("/random")
    public String getRandomPlace(Model model) {
        String randomPlace = service.getRandomPlaceInLine2();
        model.addAttribute("randomPlace", randomPlace);

        return "random/place";
    }
}
