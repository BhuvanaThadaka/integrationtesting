package com.rajlee.coursetesting.repository;

import com.rajlee.coursetesting.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Integer> {
}
