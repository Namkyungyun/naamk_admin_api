package kr.co.naamkbank.api.service;

import kr.co.naamkbank.api.dto.PermDto;
import kr.co.naamkbank.api.dto.RoleDto;
import kr.co.naamkbank.api.dto.UserDto;
import kr.co.naamkbank.api.dto.mapstruct.PermMapper;
import kr.co.naamkbank.api.dto.mapstruct.RoleMapper;
import kr.co.naamkbank.api.dto.mapstruct.UserMapper;
import kr.co.naamkbank.api.repository.jpa.RoleRepository;
import kr.co.naamkbank.api.repository.jpa.UserRepository;
import kr.co.naamkbank.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
/** TODO
 * 1. getUsersByRoleId
 * 2. getUsers [PAGE]
 * 3. updateUser
 * 4. update
 * */
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public UserDto.UserResponse getUserDetailById(Long userId) {

        TbUsers user = userRepository.findById(userId).orElse(null);
        List<TbUserRole> userRoles = Objects.requireNonNull(user).getUserRoles();

        List<RoleDto.RoleResponse> roles = userRoles.stream().map(userRole -> {
            // role 가져오기 & dto 변환
            TbRoles role = userRole.getRole();
            RoleDto.RoleResponse roleDto = RoleMapper.INSTANCE.entityToRoleResponse(userRole.getRole());

            // perms 가져오기 & dto 변환
            List<TbRolePerm> rolePerms = role.getRolePerms();
            List<TbPerms> perms = rolePerms.stream().map(TbRolePerm::getPerm).toList();
            List<PermDto.PermResponse> permDtos = perms.stream().map(PermMapper.INSTANCE::entityToResponseDto).toList();


            roleDto.setPerms(permDtos);

            return roleDto;
        }).toList();


        // user dto
        UserDto.UserResponse result = UserMapper.INSTANCE.entityToUserDto(user);
        result.setRoles(roles);

        return result;
    }

    @Transactional
    public void createUser(UserDto userDto) {
        TbUsers user = UserMapper.INSTANCE.userDtoToEntity(userDto);
        user.setUserRoles(new ArrayList<>());

        // make user-role entity list
        for(Long id : userDto.getRoles().getRoleIds()) {
            // get role entity
            TbRoles role = roleRepository.findById(id)
                    .orElseThrow(() -> new NullPointerException("no role"));

            TbUserRole userRole = new TbUserRole();
            userRole.setRole(role);
            userRole.setUser(user);

            user.getUserRoles().add(userRole);
        }

        userRepository.save(user);

    }

}
