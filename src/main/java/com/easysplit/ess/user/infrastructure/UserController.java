package com.easysplit.ess.user.infrastructure;

import com.easysplit.ess.user.domain.contracts.UserService;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") String id) {
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NotFoundException e) {
            //TODO Add logs
            throw e;
        } catch (InternalServerErrorException e) {
            //TODO Add logs
            throw e;
        } catch (Exception e) {
            //TODO Add logs
            throw new InternalServerErrorException(); // TODO Add error title, error message and cause
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            //TODO Add logs
            throw e;
        } catch (InternalServerErrorException e) {
            //TODO Add logs
            throw e;
        } catch (Exception e) {
            //TODO Add logs
            throw new InternalServerErrorException(); // TODO Add error title, error message and cause
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam(name = "id") String userGuid) {
        try {
            userService.deleteUser(userGuid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            //TODO Add logs
            throw e;
        } catch (InternalServerErrorException e) {
            //TODO Add logs
            throw e;
        } catch (Exception e) {
            //TODO Add logs
            throw new InternalServerErrorException(); // TODO Add error title, error message and cause
        }
    }
}
