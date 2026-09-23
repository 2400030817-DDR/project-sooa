package com.library;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.careerlink.profile.controller.ProfileController;
import com.careerlink.profile.entity.Profile;
import com.careerlink.profile.repository.ProfileRepository;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceApplicationTests {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileController profileController;

    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profile.setId(3L);
        profile.setUserId(2L);
        profile.setName("Candidate User");
        profile.setEmail("candidate@gmail.com");
        profile.setPhone("9876543210");
        profile.setSkills("Java, Python, Spring Boot");
        profile.setExperience(2);
        profile.setResumeUrl("resume/candidate2.pdf");
    }

    @Test
    void getProfileByUserId() {

        when(profileRepository.findByUserId(2L))
                .thenReturn(Optional.of(profile));

        var response = profileController.getProfileByUserId(2L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getUserId());
        assertEquals("candidate@gmail.com", response.getBody().getEmail());

        verify(profileRepository).findByUserId(2L);
    }

    @Test
    void getProfileById() {

        when(profileRepository.findById(3L))
                .thenReturn(Optional.of(profile));

        var response = profileController.getProfileById(3L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(3L, response.getBody().getId());

        verify(profileRepository).findById(3L);
    }

    @Test
    void createProfile() {

        when(profileRepository.save(profile))
                .thenReturn(profile);

        Profile result = profileController.createProfile(profile);

        assertNotNull(result);
        assertEquals(2L, result.getUserId());
        assertEquals("candidate@gmail.com", result.getEmail());

        verify(profileRepository).save(profile);
    }

    @Test
    void updateProfile() {

        Profile updated = new Profile();
        updated.setUserId(2L);
        updated.setName("Updated Candidate");
        updated.setEmail("candidate@gmail.com");
        updated.setPhone("9999999999");
        updated.setSkills("Java, Python, React");
        updated.setExperience(3);
        updated.setResumeUrl("resume/updated.pdf");

        when(profileRepository.findById(3L))
                .thenReturn(Optional.of(profile));

        when(profileRepository.save(any(Profile.class)))
                .thenReturn(profile);

        var response = profileController.updateProfile(3L, updated);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());

        verify(profileRepository).findById(3L);
        verify(profileRepository).save(profile);
    }

    @Test
    void deleteProfile() {

        when(profileRepository.existsById(3L))
                .thenReturn(true);

        var response = profileController.deleteProfile(3L);

        assertTrue(response.getStatusCode().is2xxSuccessful());

        verify(profileRepository).existsById(3L);
        verify(profileRepository).deleteById(3L);
    }
}