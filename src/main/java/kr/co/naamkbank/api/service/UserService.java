package kr.co.naamkbank.api.service;

import kr.co.naamkbank.api.dto.UserDto;

import java.util.List;

public interface UserService {
    /* list */
    List<UserDto.ListResponse> getUsers();
    List<UserDto.ListResponse> getUsersByRoleId(Long roleId);
    // + TODO Pagination 적용된 getUsersByPagintaion();

    /* single */
    UserDto.DetailResponse getUserDetailById(Long userId);

    /* update */
    void updateUserActivated(Long userId, boolean activated);
    void updateUserPassword(Long userId, String newPassword);
    void updateUserRole(Long userId, List<Long> roleIds);

    void createUser(UserDto.CreateRequest userDto);
}
