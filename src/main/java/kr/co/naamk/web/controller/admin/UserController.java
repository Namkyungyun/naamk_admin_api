package kr.co.naamk.web.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.admin.UserDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.admin.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/users")
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Object getUsersBySearch(@RequestBody UserDto.SearchParam searchDto, Pageable pageable,
                                   HttpServletRequest request) {

        Page<UserDto.ListResponse> users = userService.getUsersBySearch(searchDto, pageable);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(users)
                .build();

    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Object getUserDetail(@PathVariable Long userId, HttpServletRequest request) {

        UserDto.DetailResponse userDetail = userService.getUserDetailById(userId);

        return APIResponseEntityBuilder.create().service(request)
                        .resultMessage(ServiceMessageType.SUCCESS)
                        .entity(userDetail)
                        .build();
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public Object createUser(@RequestBody UserDto.CreateRequest userDto,
                                             HttpServletRequest request) {
        userService.createUser(userDto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    @RequestMapping(value="/{userId}", method = RequestMethod.PUT)
    public Object updateUser(@PathVariable Long userId,
                             @RequestBody UserDto.UpdateRequest userDto,
                             HttpServletRequest request) {

        userService.updateUser(userId, userDto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();

    }

    @RequestMapping(value="/{userId}", method = RequestMethod.DELETE)
    public Object deleteUser(@PathVariable Long userId, HttpServletRequest request) {

        userService.deleteUser(userId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }
}
