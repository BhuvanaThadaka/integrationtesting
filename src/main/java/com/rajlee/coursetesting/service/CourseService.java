package com.rajlee.coursetesting.service;

import com.rajlee.coursetesting.entity.Course;
import com.rajlee.coursetesting.repository.CourseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course addNewCourse(Course course) {
        log.info("CourseService::addNewCourse method executed");
        return courseRepository.save(course);
    }

    public List<Course> getAllAvailableCourses() {
        log.info("CourseService::getAllAvailableCourses method executed");
        return courseRepository.findAll();
    }
}
