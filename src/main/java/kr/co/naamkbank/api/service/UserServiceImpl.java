package kr.co.naamkbank.api.service;

import kr.co.naamkbank.api.dto.PermDto;
import kr.co.naamkbank.api.dto.RoleDto;
import kr.co.naamkbank.api.dto.UserDto;
import kr.co.naamkbank.api.dto.mapstruct.PermMapper;
import kr.co.naamkbank.api.dto.mapstruct.RoleMapper;
import kr.co.naamkbank.api.dto.mapstruct.UserMapper;
import kr.co.naamkbank.api.repository.jpa.RoleRepository;
import kr.co.naamkbank.api.repository.jpa.UserRepository;
import kr.co.naamkbank.api.repository.queryDSL.UserQueryRepository;
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
public class UserServiceImpl implements UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<UserDto.ListResponse> getUsers() {
        List<TbUsers> entities = userRepository.findAll();

        return  entities.stream().map(entity -> {
            UserDto.ListResponse dto = UserMapper.INSTANCE.entityToListResponseDto(entity);
            dto.setRoleNames(getRoleNames(entity));

            return dto;
        }).toList();
    }

    @Override
    public List<UserDto.ListResponse> getUsersByRoleId(Long roleId) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto.DetailResponse getUserDetailById(Long userId) {

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
        UserDto.DetailResponse result = UserMapper.INSTANCE.entityToDetailResponseDto(user);
        result.setRoles(roles);

        return result;
    }


    @Override
    @Transactional
    public void createUser(UserDto.CreateRequest userDto) {
        TbUsers user = UserMapper.INSTANCE.createRequestDtoToEntity(userDto);
        user.setUserRoles(new ArrayList<>());

        // make user-role entity list
        for(Long id : userDto.getRoleIds()) {
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


    @Override
    public void updateUserActivated(Long userId, boolean activated) {

    }

    @Override
    public void updateUserPassword(Long userId, String newPassword) {

    }

    @Override
    public void updateUserRole(Long userId, List<Long> roleIds) {

    }

    private List<String> getRoleNames(TbUsers entity) {
        return  entity.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getRoleNm())
                .toList();
    }

}
