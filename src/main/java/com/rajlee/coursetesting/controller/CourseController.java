package com.rajlee.coursetesting.controller;

import com.rajlee.coursetesting.entity.Course;
import com.rajlee.coursetesting.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/courses")
@Slf4j
public class CourseController {

    private CourseService courseService;

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        log.info("CourseController::addCourse method executed");
        return courseService.addNewCourse(course);
    }

    @GetMapping
    public List<Course> viewAllCourses() {
        log.info("CourseController::viewAllCourses method executed");
        return courseService.getAllAvailableCourses();
    }
}
