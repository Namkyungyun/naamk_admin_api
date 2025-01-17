package kr.co.naamk.web.service.admin.impl;

import kr.co.naamk.domain.admin.TbRoles;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.admin.RoleDto;
import kr.co.naamk.web.dto.admin.mapstruct.RoleMapper;
import kr.co.naamk.web.repository.admin.jpa.RoleRepository;
import kr.co.naamk.web.service.admin.RoleMenuPermService;
import kr.co.naamk.web.service.admin.RoleService;
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
    public List<RoleDto.RoleListResponse> getAll() {
        List<TbRoles> roles = getRoles();
        return RoleMapper.INSTANCE.entitiesToListResponseDTOs(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto.RoleDetailResponse getDetailById(Long id) {
        TbRoles role = getRoleById(id);

        return RoleMapper.INSTANCE.entityToDetailResponseDTO(role);
    }

    @Override
    @Transactional
    public void createRole(RoleDto.RoleCreateRequest dto) {
        boolean isExisting = roleRepository.existsByRoleCd(dto.getRoleCd());
        if(isExisting) {
            throw new ServiceException(ServiceMessageType.ALREADY_EXIST);
        }

        TbRoles role = RoleMapper.INSTANCE.createRequestDTOToEntity(dto);
        TbRoles savedRole = roleRepository.save(role);

        roleMenuPermService.createByRole(savedRole);
    }

    @Override
    @Transactional
    public void updateRole(Long id, RoleDto.RoleUpdateRequest dto) {
        TbRoles role = getRoleById(id);

        role.setRoleNm(dto.getRoleNm());
        role.setRoleDesc(dto.getRoleDesc());

        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRoleById(Long id) {
        TbRoles role = getRoleById(id);
        roleRepository.delete(role);
    }

    @Override
    @Transactional(readOnly = true)
    public TbRoles getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.ROLE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TbRoles> getRoles() {
        return roleRepository.findAll();
    }

}
