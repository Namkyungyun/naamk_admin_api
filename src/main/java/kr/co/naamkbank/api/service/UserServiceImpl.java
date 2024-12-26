package kr.co.naamkbank.api.service;

import kr.co.naamkbank.api.dto.UserDto;
import kr.co.naamkbank.api.dto.mapstruct.UserMapper;
import kr.co.naamkbank.api.repository.jpa.RoleRepository;
import kr.co.naamkbank.api.repository.jpa.UserRepository;
import kr.co.naamkbank.api.repository.jpa.UserRoleRepository;
import kr.co.naamkbank.api.repository.queryDSL.UserQueryRepository;
import kr.co.naamkbank.domain.*;
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
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto.ListResponse> getUsersBySearch(UserDto.SearchParam search, Pageable pageable) {
        return  userQueryRepository.findUsersWithRoles(search, pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDto.DetailResponse getUserDetailById(Long userId) {

        TbUsers user = userRepository.findById(userId).orElse(null);

        return UserMapper.INSTANCE.entityToDetailResponseDto(user);
    }


    @Override
    @Transactional
    public void createUser(UserDto.CreateRequest userDto) {

        TbUsers user = UserMapper.INSTANCE.createRequestDtoToEntity(userDto);
        user.setUserRoles(getUserRoles(user, userDto.getRoleIds()));

        userRepository.save(user);
    }


    @Override
    @Transactional
    public void updateUser(Long userId, UserDto.UpdateRequest userDto) {
        TbUsers user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("no user"));

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
            removeUserRoleInUser(user);

            // 새로운 userRole
            user.getUserRoles().addAll(getUserRoles(user, roleIds));

            // user 테이블 updated_at 강제 진행
            user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        }
    }

    private void removeUserRoleInUser(TbUsers user) {
        List<TbUserRole> userRoles = new ArrayList<>(user.getUserRoles()); // ConcurrentModificationException 방지

        for(TbUserRole userRole : userRoles) {
            user.removeUserRole(userRole); // 연관 관계에서 제거 && orphanRemoval = true를 통해 자식 엔티티 자동 삭제를 활용.
        }
    }


    private List<TbUserRole> getUserRoles(TbUsers user, List<Long> roleIds) {
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
