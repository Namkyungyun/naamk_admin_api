package kr.co.naamk.web.service.admin;

import kr.co.naamk.web.dto.admin.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
