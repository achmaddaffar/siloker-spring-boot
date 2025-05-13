package com.oliver.siloker.service;

import com.oliver.siloker.component.JwtUtils;
import com.oliver.siloker.model.entity.user.*;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.repository.*;
import com.oliver.siloker.model.request.RegisterEmployerRequest;
import com.oliver.siloker.model.request.RegisterJobSeekerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final JwtUtils jwtUtils;

    public User getUser() throws ResourceNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String phoneNumber = userDetails.getUsername();

        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public JobSeeker registerJobSeeker(
            RegisterJobSeekerRequest request
    ) throws ResourceNotFoundException {
        User user = userRepository.findByPhoneNumber(jwtUtils.getUserDetails().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        JobSeeker jobSeeker = jobSeekerRepository.findByUserId(user.getId())
                .orElse(new JobSeeker());
        jobSeeker.setUser(user);
        jobSeeker.setResumeUrl(request.getResumeUrl());
        jobSeeker.setCreatedAt(LocalDateTime.now().toString());

        skillRepository.deleteSkillsByUserId(user.getId());
        List<Skill> skills = request.getSkills().stream().map((skillName) -> {
            Skill skill = new Skill();
            skill.setUserId(user.getId());
            skill.setName(skillName);
            skill.setCreatedAt(LocalDateTime.now().toString());
            return skill;
        }).toList();
        skillRepository.saveAll(skills);

        experienceRepository.deleteExperiencesByUserId(user.getId());
        List<Experience> experiences = request.getExperiences().stream().map((experienceName) -> {
            Experience experience = new Experience();
            experience.setUserId(user.getId());
            experience.setName(experienceName);
            experience.setCreatedAt(LocalDateTime.now().toString());
            return experience;
        }).toList();
        experienceRepository.saveAll(experiences);
        user.setJobSeeker(jobSeeker);

        return jobSeekerRepository.save(jobSeeker);
    }

    public Employer registerEmployer(
            RegisterEmployerRequest request
    ) throws ResourceNotFoundException {
        User user = userRepository.findByPhoneNumber(jwtUtils.getUserDetails().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Employer employer = employerRepository.findByUserId(user.getId())
                .orElse(new Employer());
        employer.setUser(user);
        employer.setCompanyName(request.getCompanyName());
        employer.setPosition(request.getPosition());
        employer.setCompanyWebsite(request.getCompanyWebsite());
        employer.setCreatedAt(LocalDateTime.now().toString());

        return employerRepository.save(employer);
    }
}
