package kr.co.naamk.api.service;

import kr.co.naamk.api.dto.PermDto;
import kr.co.naamk.api.dto.RoleDto;
import kr.co.naamk.api.dto.mapstruct.PermMapper;
import kr.co.naamk.api.dto.mapstruct.RoleMapper;
import kr.co.naamk.api.repository.jpa.PermRepository;
import kr.co.naamk.api.repository.jpa.RoleRepository;
import kr.co.naamk.domain.*;
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

    @Transactional
    public void createRole(RoleDto.RoleRequest roleDto) {
        TbRoles role = RoleMapper.INSTANCE.roleRequestDtoToEntity(roleDto);
        role.setRolePerms(new ArrayList<>());

        for(Long permId : roleDto.getPermIds()) {
            TbPerms perm = permRepository.findById(permId).orElseThrow(()-> new NullPointerException("no perm"));

            TbRolePerm rolePerm = TbRolePerm.builder()
                    .perm(perm)
                    .role(role)
                    .build();

            role.getRolePerms().add(rolePerm);
        }

        roleRepository.save(role);

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
