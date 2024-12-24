package kr.co.naamkbank.api.controller;

import kr.co.naamkbank.api.dto.UserDto;
import kr.co.naamkbank.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/user")
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserDetail(@PathVariable Long userId) {
        UserDto.UserResponse userDetail = userService.getUserDetailById(userId);

        return ResponseEntity.ok(userDetail);
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestBody UserDto user) {
        try{
            userService.createUser(user);
            return ResponseEntity.ok("OK");
        } catch(Exception e) {
            throw e;
        }
    }
}
