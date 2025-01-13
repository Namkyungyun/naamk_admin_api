package kr.co.naamk.web.service.impl;

import kr.co.naamk.domain.TbPerms;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.mapstruct.PermMapper;
import kr.co.naamk.web.repository.jpa.PermRepository;
import kr.co.naamk.web.service.PermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermServiceImpl implements PermService {

    private final PermRepository permRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TbPerms> getPermissions() {
        return permRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TbPerms getPermissionById(Long id) {
        return permRepository.findById(id)
                .orElseThrow(()-> new ServiceException(ServiceMessageType.PERMISSION_NOT_FOUND));
    }


    @Transactional
    public void createPermission(PermDto.PermCreateRequest dto) {
        TbPerms perm = PermMapper.INSTANCE.createRequestDtoToEntity(dto);
        permRepository.save(perm);
    }

    @Override
    @Transactional
    public void deletePermissionById(Long permId) {
        TbPerms perm = getPermissionById(permId);
        permRepository.delete(perm);
    }

    @Override
    @Transactional
    public void updatePermission(Long id, PermDto.PermUpdateRequest dto) {
        TbPerms perm = getPermissionById(id);

        perm.setPermNm(dto.getPermNm());
        perm.setPermDesc(dto.getPermDesc());
        
        permRepository.save(perm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermDto.PermListResponse> getAll() {
        List<TbPerms> list = getPermissions();

        return PermMapper.INSTANCE.entitiesToListResponseDTOs(list);
    }

    @Override
    @Transactional(readOnly = true)
    public PermDto.PermDetailResponse getDetailById(Long permId) {
        TbPerms perm = getPermissionById(permId);

        return PermMapper.INSTANCE.entityToDetailResponseDTO(perm);
    }
}
