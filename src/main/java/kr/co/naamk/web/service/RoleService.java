package kr.co.naamk.web.service;

import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.RoleDto;
import kr.co.naamk.web.dto.mapstruct.PermMapper;
import kr.co.naamk.web.dto.mapstruct.RoleMapper;
import kr.co.naamk.web.repository.jpa.PermRepository;
import kr.co.naamk.web.repository.jpa.RoleRepository;
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


        return RoleMapper.INSTANCE.entitiesToRoleResponseDtos(list);
    }

}
