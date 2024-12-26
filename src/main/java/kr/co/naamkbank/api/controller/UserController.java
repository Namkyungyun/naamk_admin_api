package kr.co.naamkbank.api.controller;

import kr.co.naamkbank.api.dto.UserDto;
import kr.co.naamkbank.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/users")
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<Object> getUsersBySearch(@RequestBody UserDto.SearchParam searchDto,
                                                   Pageable pageable) {
        Page<UserDto.ListResponse> users = userService.getUsersBySearch(searchDto, pageable);

        return ResponseEntity.ok(users);

    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserDetail(@PathVariable Long userId) {
        UserDto.DetailResponse userDetail = userService.getUserDetailById(userId);

        return ResponseEntity.ok(userDetail);
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestBody UserDto.CreateRequest userDto) {
        try{
            userService.createUser(userDto);
            return ResponseEntity.ok("OK");
        } catch(Exception e) {
            throw e;
        }
    }

    @RequestMapping(value="/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @RequestBody UserDto.UpdateRequest userDto) {
        try {
            userService.updateUser(userId, userDto);
            return ResponseEntity.ok("OK");

        } catch(Exception e) {
            throw e;
        }
    }
}
