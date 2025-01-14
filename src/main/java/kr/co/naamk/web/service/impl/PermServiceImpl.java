package kr.co.naamk.web.service.impl;

import kr.co.naamk.domain.TbPermGrpPerm;
import kr.co.naamk.domain.TbPermGrps;
import kr.co.naamk.domain.TbPerms;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.PermGrpDto;
import kr.co.naamk.web.dto.mapstruct.PermGrpMapper;
import kr.co.naamk.web.dto.mapstruct.PermMapper;
import kr.co.naamk.web.repository.jpa.PermGrpPermRepository;
import kr.co.naamk.web.repository.jpa.PermGrpRepository;
import kr.co.naamk.web.repository.jpa.PermRepository;
import kr.co.naamk.web.service.PermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermServiceImpl implements PermService {

    private final PermRepository permRepository;
    private final PermGrpRepository permGrpRepository;
    private final PermGrpPermRepository permGrpPermRepository;


    /************ perm group CRUD ************/
    @Override
    @Transactional(readOnly = true)
    public List<PermGrpDto.PermGrpListResponse> getAllPermGrp() {
        List<TbPermGrps> permGrps = getPermGrps();
        return PermGrpMapper.INSTANCE.entitiesToListResponseDTOs(permGrps);
    }

    @Override
    @Transactional(readOnly = true)
    public PermGrpDto.PermGrpDetailResponse getDetailPermGrp(Long permGrpId) {
        TbPermGrps permGrp = getPermGrpById(permGrpId);
        // TODO 내부 dto의 일부값 mapper에서 mapping 어노테이션을 사용해 조정 필요.
        return PermGrpMapper.INSTANCE.entityToDetailResponseDTO(permGrp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPermGrp(PermGrpDto.PermGrpCreateRequest dto) {
        boolean isExisting = permGrpRepository.existsByPermGrpCd(dto.getPermGrpCd());
        if (isExisting) {
            throw new ServiceException(ServiceMessageType.ALREADY_EXIST);
        }

        TbPermGrps permGrp = PermGrpMapper.INSTANCE.createRequestDtoToEntity(dto);

        TbPermGrps savedPermGrp = permGrpRepository.save(permGrp);
        List<TbPerms> permissions = getPerms();
        createMappingDataByPermGrp(savedPermGrp, permissions);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermGrp(Long id,PermGrpDto.PermGrpUpdateRequest dto) {
        TbPermGrps permGrp = getPermGrpById(id);

        permGrp.setPermGrpNm(dto.getPermGrpNm());
        permGrp.setPermGrpDesc(dto.getPermGrpDesc());

        List<PermGrpDto.ChildPermStatus> childPerms = dto.getChildPerms();
        updateMappingDataActivated(permGrp, childPerms);

        permGrpRepository.save(permGrp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermGrpById(Long permGrpId) {
        TbPermGrps permGrp = getPermGrpById(permGrpId);
        permGrpRepository.delete(permGrp);
    }

    @Override
    @Transactional(readOnly = true)
    public TbPermGrps getPermGrpById(Long permGrpId) {
        return permGrpRepository.findById(permGrpId)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.PERMISSION_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TbPermGrps> getPermGrps() {
        return permGrpRepository.findAll();
    }




    /************  perm CRUD ************/
    @Override
    @Transactional(readOnly = true)
    public List<PermDto.PermListResponse> getAllPerm() {
        List<TbPerms> list = getPerms();

        return PermMapper.INSTANCE.entitiesToListResponseDTOs(list);
    }

    @Override
    @Transactional(readOnly = true)
    public PermDto.PermDetailResponse getDetailPermById(Long permId) {
        TbPerms perm = getPermById(permId);

        return PermMapper.INSTANCE.entityToDetailResponseDTO(perm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPermission(PermDto.PermCreateRequest dto) {
        boolean isExisting = permRepository.existsByPermCd(dto.getPermCd());
        if(isExisting) {
            throw new ServiceException(ServiceMessageType.ALREADY_EXIST);
        }

        TbPerms perm = PermMapper.INSTANCE.createRequestDtoToEntity(dto);
        TbPerms savedPerm = permRepository.save(perm);

        List<TbPermGrps> permGrps = getPermGrps();
        createMappingDataByPerm(savedPerm, permGrps);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermissionById(Long permId) {
        TbPerms perm = getPermById(permId);
        permRepository.delete(perm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Long id, PermDto.PermUpdateRequest dto) {
        TbPerms perm = getPermById(id);

        perm.setPermNm(dto.getPermNm());
        perm.setPermDesc(dto.getPermDesc());
        
        permRepository.save(perm);
    }


    @Override
    @Transactional(readOnly = true)
    public List<TbPerms> getPerms() {
        return permRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TbPerms getPermById(Long id) {
        return permRepository.findById(id)
                .orElseThrow(()-> new ServiceException(ServiceMessageType.PERMISSION_NOT_FOUND));
    }



    /************ perm group, perm mapping CRUD ************/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMappingDataByPerm(TbPerms perm, List<TbPermGrps> permGrps) {
        List<TbPermGrpPerm> list = new ArrayList<>();

        for(TbPermGrps permGrp : permGrps) {
            TbPermGrpPerm mapping = new TbPermGrpPerm();
            mapping.setPermGrp(permGrp);
            mapping.setPerm(perm);

            list.add(mapping);
        }

        permGrpPermRepository.saveAll(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMappingDataByPermGrp(TbPermGrps permGrp, List<TbPerms> perms) {
        List<TbPermGrpPerm> list = new ArrayList<>();

        for(TbPerms perm : perms) {
            TbPermGrpPerm mapping = new TbPermGrpPerm();
            mapping.setPermGrp(permGrp);
            mapping.setPerm(perm);

            list.add(mapping);
        }

        permGrpPermRepository.saveAll(list);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMappingDataActivated(TbPermGrps permGrp, List<PermGrpDto.ChildPermStatus> childPermStatuses) {
        List<TbPermGrpPerm> permGrpPerm = permGrp.getPermGrpPerm();

        for(TbPermGrpPerm perm : permGrpPerm) {
            Long id = perm.getPerm().getId();
            childPermStatuses.stream()
                    .filter(el -> el.getPermId().equals(id))
                    .findFirst()
                    .ifPresent(el -> {
                        perm.setActivated(el.isActivated());
                    });
        }
    }
}
