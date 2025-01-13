package kr.co.naamk.web.service.impl;

import kr.co.naamk.domain.TbRoles;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.RoleDto;
import kr.co.naamk.web.dto.mapstruct.RoleMapper;
import kr.co.naamk.web.repository.jpa.RoleRepository;
import kr.co.naamk.web.service.RoleMenuPermService;
import kr.co.naamk.web.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMenuPermService roleMenuPermService;

    @Override
    @Transactional(readOnly = true)
    public TbRoles getRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void createRole(RoleDto.RoleRequest roleDto) {
        TbRoles role = RoleMapper.INSTANCE.roleRequestDtoToEntity(roleDto);

        TbRoles savedRole = roleRepository.save(role);
        roleMenuPermService.createByRole(savedRole);

    }

    @Override
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


        return RoleMapper.INSTANCE.entitiesToRoleResponseDTOs(list);
    }
}
