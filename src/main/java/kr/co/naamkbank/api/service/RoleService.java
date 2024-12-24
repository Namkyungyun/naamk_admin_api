package kr.co.naamkbank.api.service;

import kr.co.naamkbank.api.dto.PermDto;
import kr.co.naamkbank.api.dto.RoleDto;
import kr.co.naamkbank.api.dto.mapstruct.PermMapper;
import kr.co.naamkbank.api.dto.mapstruct.RoleMapper;
import kr.co.naamkbank.api.repository.*;
import kr.co.naamkbank.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermRepository permRepository;
    private final RolePermRepository rolePermRepository;

    @Transactional
    public TbRoles createRole(RoleDto role) {
        return roleRepository.save(TbRoles.builder()
                        .roleCd(role.getRoleCd())
                        .roleNm(role.getRoleNm())
                .build());
    }

    @Transactional
    public void createRolePermission(Long roleId, RoleDto.RolePermRequest rolePermRequest) {

        // get role entity
        TbRoles role= roleRepository.findById(roleId)
                .orElseThrow(() -> new NullPointerException("no role"));

        // deleteAll existing role-perms
        List<TbRolePerm> existingRolePerms = role.getRolePerms();
        if(!existingRolePerms.isEmpty()) {
            rolePermRepository.deleteAll(existingRolePerms);
        }

        // make role-perm entity list
        List<TbRolePerm> list = new ArrayList<>();
        for(Long permId : rolePermRequest.getPermIds()) {
            // get perm entity
            final TbPerms perm = permRepository.findById(permId).orElseThrow(()-> new NullPointerException("no perm"));

            final TbRolePermIds compositeId = TbRolePermIds.builder()
                    .roleId(role.getId())
                    .permId(perm.getId())
                    .build();

            final TbRolePerm rolePerm = TbRolePerm.builder()
                    .id(compositeId)
                    .perm(perm)
                    .role(role)
                    .build();

            list.add(rolePerm);
        }

        // bulk insert
        rolePermRepository.saveAll(list);
    }

    @Transactional(readOnly = true)
    public List<RoleDto.RoleResponse> getRoles(Long roleId) {
        List<TbRoles> list;

        if(roleId == null) {
            // 전체 데이터 내리기
           list =  roleRepository.findAll();
        } else {
            // roleId 데이터 내리기
            list = roleRepository.findById(roleId).stream().toList();
        }

        List<RoleDto.RoleResponse> result = new ArrayList<>();
        for(TbRoles role : list) {
            final RoleDto.RoleResponse response = RoleMapper.INSTANCE.entityToRoleResponse(role);

            List<PermDto.PermResponse> perms = Objects.requireNonNull(role).getRolePerms().stream().map(rolePerm -> {
                TbPerms entity = rolePerm.getPerm();
                return PermMapper.INSTANCE.entityToResponseDto(entity);
            }).toList();

            response.setPerms(perms);
            result.add(response);
        }

        return result;
    }

}
