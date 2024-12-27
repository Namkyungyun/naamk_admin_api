package kr.co.naamkbank.api.service;

import kr.co.naamkbank.api.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    /* list */
    Page<UserDto.ListResponse> getUsersBySearch(UserDto.SearchParam search, Pageable pageable);

    /* single */
    UserDto.DetailResponse getUserDetailById(Long userId);

    /* update */
    void updateUser(Long userId, UserDto.UpdateRequest userDto);

    void createUser(UserDto.CreateRequest userDto);

    /* delete */
    void deleteUser(Long userId);
}
