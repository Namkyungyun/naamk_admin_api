package kr.co.naamk.web.service.impl;

import kr.co.naamk.domain.TbCmmn;
import kr.co.naamk.domain.TbCmmnGrp;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.CmmnDto.*;
import kr.co.naamk.web.dto.mapstruct.CmmnMapper;
import kr.co.naamk.web.repository.jpa.CmmnGrpRepository;
import kr.co.naamk.web.repository.jpa.CmmnRepository;
import kr.co.naamk.web.service.CmmnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CmmnServiceImpl implements CmmnService {

    private final CmmnRepository cmmnRepository;
    private final CmmnGrpRepository cmmnGrpRepository;

    /******** 공통 그룹 코드 ********/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CmmnGrpCreateResponse createCmmnGrp(CmmnGrpCreateRequest dto) {
        // 이미 조재하는 그룹 코드인지 확인
        boolean isExisting = cmmnGrpRepository.existsByCmmnGrpCd(dto.getCmmnGrpCd());
        if(isExisting) {
            throw new ServiceException(ServiceMessageType.ALREADY_EXIST);
        }

        // cmmn grp dto to entity & save
        TbCmmnGrp cmmnGrp = CmmnMapper.INSTANCE.cmmnGrpCreateRequestDtoToEntity(dto);
        TbCmmnGrp savedEntity = cmmnGrpRepository.save(cmmnGrp);

        return CmmnGrpCreateResponse.builder()
                .id(savedEntity.getId())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCmmnGrp(Long cmmnGrpId, CmmnGrpUpdateRequest dto) {
        TbCmmnGrp cmmnGrp = getCmmnGrpById(cmmnGrpId);

        cmmnGrp.setCmmnGrpNm(dto.getCmmnGrpNm());
        cmmnGrp.setCmmnGrpDesc(dto.getCmmnGrpDesc());
        cmmnGrp.setActivated(dto.isActivated());

        cmmnGrpRepository.save(cmmnGrp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCmmnGrp(Long cmmnGrpId) {
        TbCmmnGrp cmmnGrp = getCmmnGrpById(cmmnGrpId);
        cmmnGrpRepository.delete(cmmnGrp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CmmnGrpListResponse> getAllCmmnGrps() {
        List<TbCmmnGrp> cmmnGrps = cmmnGrpRepository.findAll();

        return CmmnMapper.INSTANCE.entitiesToCmmnGrpListResponseDTO(cmmnGrps);
    }

    @Override
    @Transactional(readOnly = true)
    public CmmnGrpDetailResponse getDetailCmmnGrp(Long cmmnGrpId) {
        // 그룹코드 조회
        TbCmmnGrp cmmnGrp = getCmmnGrpById(cmmnGrpId);

        return CmmnMapper.INSTANCE.entityToCmmnGrpDetailResponseDTO(cmmnGrp);
    }


    private TbCmmnGrp getCmmnGrpById(Long cmmnGrpId) {
        return cmmnGrpRepository.findById(cmmnGrpId)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.COMMON_CODE_NOT_FOUND, "common group code not found"));
    }

    /******** 공통 코드 ********/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCmmn(Long cmmnGrpId, CmmnCreateRequest dto) {
        boolean isExisting = cmmnRepository.existsByCmmnCdAndCmmnGrpId(dto.getCmmnCd(), cmmnGrpId);
        if(isExisting) {
            throw new ServiceException(ServiceMessageType.ALREADY_EXIST);
        }

        TbCmmn cmmn = CmmnMapper.INSTANCE.cmmnCreateRequestDtoToEntity(dto);
        cmmnRepository.save(cmmn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCmmn(Long cmmnId, CmmnUpdateRequest dto) {
        TbCmmn cmmn = getCmmnById(cmmnId);

        cmmn.setCmmnCd(dto.getCmmnCd());
        cmmn.setCmmnNm(dto.getCmmnNm());
        cmmn.setCmmnDesc(dto.getCmmnDesc());
        cmmn.setOrderNum(dto.getOrderNum());
        cmmn.setActivated(dto.isActivated());

        cmmnRepository.save(cmmn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCmmn(Long cmmnId) {
        TbCmmn cmmn = getCmmnById(cmmnId);
        cmmnRepository.delete(cmmn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CmmnListResponse> getAllCmmnsByGrpId(Long cmmnGrpId) {
        // 그룹코드 조회
        TbCmmnGrp cmmnGrp = getCmmnGrpById(cmmnGrpId);

        List<TbCmmn> cmmns = cmmnGrp.getCmmns().stream()
                .sorted(Comparator.comparing(TbCmmn::getOrderNum))
                .toList();

        return CmmnMapper.INSTANCE.entitiesToCmmnListResponseDTOs(cmmns);
    }

    @Override
    @Transactional(readOnly = true)
    public CmmnDetailResponse getDetailCmmnByCmmnGrpId(Long cmmnGrpId, Long cmmnId) {
        TbCmmn cmmn = getCmmnById(cmmnId);

        if(!cmmn.getCmmnGrp().getId().equals(cmmnGrpId)) {
            throw new ServiceException(ServiceMessageType.COMMON_CODE_NOT_FOUND, "The requested group is not same with origin ");
        }

        return CmmnMapper.INSTANCE.entityToCmmnDetailResponseDTO(cmmn);
    }


    @Override
    @Transactional(readOnly = true)
    public CmmnDetailResponse getDetailCmmn(Long cmmnId) {
        TbCmmn cmmn = getCmmnById(cmmnId);

        return CmmnMapper.INSTANCE.entityToCmmnDetailResponseDTO(cmmn);
    }

    private TbCmmn getCmmnById(Long cmmnId) {
        return cmmnRepository.findById(cmmnId)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.COMMON_CODE_NOT_FOUND));
    }
}
