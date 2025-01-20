package kr.co.naamk.web.service.admin.impl;

import kr.co.naamk.domain.admin.TbRoles;
import kr.co.naamk.domain.admin.TbUserRole;
import kr.co.naamk.domain.admin.TbUsers;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.admin.UserDto;
import kr.co.naamk.web.dto.admin.mapstruct.UserMapper;
import kr.co.naamk.web.repository.admin.jpa.RoleRepository;
import kr.co.naamk.web.repository.admin.jpa.UserRepository;
import kr.co.naamk.web.repository.admin.queryDSL.UserQueryRepository;
import kr.co.naamk.web.service.admin.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto.ListResponse> getUsersBySearch(UserDto.SearchParam search, Pageable pageable) {
        return  userQueryRepository.findUsersWithRoles(search, pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDto.DetailResponse getUserDetailById(Long userId) {

        TbUsers user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ServiceMessageType.USER_NOT_FOUND));

        return UserMapper.INSTANCE.entityToDetailResponseDTO(user);
    }


    @Override
    @Transactional
    public void createUser(UserDto.CreateRequest userDto) {

        TbUsers user = UserMapper.INSTANCE.createRequestDtoToEntity(userDto);
        user.setUserRoles(makeUserRoles(user, userDto.getRoleIds()));

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        // 연관관계 삭제
        TbUsers user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ServiceMessageType.USER_NOT_FOUND));
        userRepository.delete(user);
    }


    @Override
    @Transactional
    public void updateUser(Long userId, UserDto.UpdateRequest userDto) {
        TbUsers user = userRepository.findById(userId).orElseThrow(()-> new ServiceException(ServiceMessageType.USER_NOT_FOUND));

        // user email
        updateUserEmail(user, userDto.getUserEmail());

        // login password
        updateLoginPwd(user, userDto.getLoginPwd());

        // user activated status
        updateUserActivated(user, userDto.isActivated());

        // user role
        updateUserRole(user, userDto.getRoleIds());

        // user update
        userRepository.save(user);
    }

    /* update private function */
    private void updateUserEmail(TbUsers user, String userEmail) {
        if(userEmail != null && !userEmail.isBlank() && !user.getUserEmail().equals(userEmail)) {
            user.setUserEmail(userEmail);
        }
    }

    private void updateLoginPwd(TbUsers user, String loginPwd) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(loginPwd != null && !loginPwd.isBlank() && !encoder.matches(loginPwd, user.getLoginPwd())) {
            user.setLoginPwd(encoder.encode(loginPwd));
            user.setPwdExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusDays(90)));
        }
    }

    private void updateUserActivated(TbUsers user, boolean activated) {
        if(user.isActivated() != activated) {
            user.setActivated(activated);
        }
    }

    private void updateUserRole(TbUsers user, List<Long> roleIds ) {
        List<Long> savedRoleIds = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getId()).toList();

        if(roleIds != null && !roleIds.isEmpty() && !roleIds.equals(savedRoleIds)) {
            // 연관 관계에서 삭제
            deleteUserRoleInUser(user);

            // 새로운 userRole
            user.getUserRoles().addAll(makeUserRoles(user, roleIds));

            // user 테이블 updated_at 강제 진행
            user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        }
    }

    /* delete private function */
    private void deleteUserRoleInUser(TbUsers user) {
        List<TbUserRole> userRoles = user.getUserRoles(); // ConcurrentModificationException 방지

        for(TbUserRole userRole : userRoles) {
            user.removeUserRole(userRole); // 연관 관계에서 제거 && orphanRemoval = true를 통해 자식 엔티티 자동 삭제를 활용.
        }
    }


    /* get & make new instance private function */
    private List<TbUserRole> makeUserRoles(TbUsers user, List<Long> roleIds) {
        List<TbUserRole> result = new ArrayList<>();

        for(Long roleId : roleIds) {
            TbRoles role = roleRepository.findById(roleId).orElseThrow(() -> new NullPointerException("no role"));

            TbUserRole userRole = new TbUserRole();
            userRole.setUser(user);
            userRole.setRole(role);

            result.add(userRole);
        }

        return result;
    }


}
