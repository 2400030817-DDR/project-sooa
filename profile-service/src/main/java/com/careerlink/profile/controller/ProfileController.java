package com.careerlink.profile.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.careerlink.profile.entity.Profile;
import com.careerlink.profile.repository.ProfileRepository;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileRepository profileRepository;

    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileRepository.save(profile);
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(
            @PathVariable Long id) {

        return profileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Profile> getProfileByUserId(
            @PathVariable Long userId) {

        return profileRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Long id,
            @RequestBody Profile updatedProfile) {

        return profileRepository.findById(id)
                .map(profile -> {

                    profile.setUserId(updatedProfile.getUserId());
                    profile.setName(updatedProfile.getName());
                    profile.setEmail(updatedProfile.getEmail());
                    profile.setPhone(updatedProfile.getPhone());
                    profile.setSkills(updatedProfile.getSkills());
                    profile.setExperience(updatedProfile.getExperience());
                    profile.setResumeUrl(updatedProfile.getResumeUrl());

                    return ResponseEntity.ok(
                            profileRepository.save(profile));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Long id) {

        if (!profileRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        profileRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}