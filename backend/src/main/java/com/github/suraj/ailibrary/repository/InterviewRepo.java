package com.github.suraj.ailibrary.repository;

import com.github.suraj.ailibrary.model.GptAPI.Analysis.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepo extends JpaRepository<Interview,Long> {

}
