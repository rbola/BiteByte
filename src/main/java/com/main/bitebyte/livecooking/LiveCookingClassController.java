package com.main.bitebyte.livecooking;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.bitebyte.livecooking.LiveCookingClass;
import com.main.bitebyte.livecooking.LiveCookingClassRepository;

@RestController
@RequestMapping("/api/live-cooking-classes")
public class LiveCookingClassController {

    @Autowired
    private LiveCookingClassRepository liveCookingClassRepository;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<LiveCookingClass>> getAllClasses() {
        return ResponseEntity.ok(liveCookingClassRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<LiveCookingClass> getClassById(@PathVariable Long id) {
        Optional<LiveCookingClass> liveCookingClass = liveCookingClassRepository.findById(id);
        return liveCookingClass.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LiveCookingClass createClass(@RequestBody LiveCookingClass liveCookingClass) {
        return liveCookingClassRepository.save(liveCookingClass);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LiveCookingClass> updateClass(@PathVariable Long id, @RequestBody LiveCookingClass liveCookingClassDetails) {
        Optional<LiveCookingClass> liveCookingClass = liveCookingClassRepository.findById(id);
        if (liveCookingClass.isPresent()) {
            LiveCookingClass updatedClass = liveCookingClass.get();
            updatedClass.setTitle(liveCookingClassDetails.getTitle());
            updatedClass.setDescription(liveCookingClassDetails.getDescription());
            updatedClass.setInstructor(liveCookingClassDetails.getInstructor());
            updatedClass.setDateTime(liveCookingClassDetails.getDateTime());
            updatedClass.setDuration(liveCookingClassDetails.getDuration());
            updatedClass.setCapacity(liveCookingClassDetails.getCapacity());
            return ResponseEntity.ok(liveCookingClassRepository.save(updatedClass));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        Optional<LiveCookingClass> liveCookingClass = liveCookingClassRepository.findById(id);
        if (liveCookingClass.isPresent()) {
            liveCookingClassRepository.delete(liveCookingClass.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/attend")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> attendClass(@PathVariable Long id) {
        Optional<LiveCookingClass> liveCookingClass = liveCookingClassRepository.findById(id);
        if (liveCookingClass.isPresent()) {
            LiveCookingClass classToAttend = liveCookingClass.get();
            if (classToAttend.getMaxParticipants() > classToAttend.getAttendeeCount()) {
                classToAttend.setAttendeeCount(classToAttend.getAttendeeCount() + 1);
                liveCookingClassRepository.save(classToAttend);
                return ResponseEntity.ok("Successfully registered for the class");
            } else {
                return ResponseEntity.badRequest().body("Class is full");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}