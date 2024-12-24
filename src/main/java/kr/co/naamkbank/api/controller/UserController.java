package kr.co.naamkbank.api.controller;

import kr.co.naamkbank.api.dto.UserDto;
import kr.co.naamkbank.api.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserDetail(@PathVariable Long userId) {
        UserDto.DetailResponse userDetail = userServiceImpl.getUserDetailById(userId);

        return ResponseEntity.ok(userDetail);
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestBody UserDto.CreateRequest userDto) {
        try{
            userServiceImpl.createUser(userDto);
            return ResponseEntity.ok("OK");
        } catch(Exception e) {
            throw e;
        }
    }
}
