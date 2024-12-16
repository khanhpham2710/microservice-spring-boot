package microservice.authentication_service.controllers;

import lombok.RequiredArgsConstructor;
import microservice.authentication_service.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PutMapping("/assign")
    public ResponseEntity<?> assignGroup(@RequestParam String userId, @RequestParam String groupId) {
        groupService.assignGroup(userId, groupId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> unAssignGroup(@RequestParam String userId, @RequestParam String groupId) {
        groupService.deleteGroupFromUser(userId, groupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}


