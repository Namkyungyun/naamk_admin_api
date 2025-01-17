package kr.co.naamk.web.dto.admin.mapstruct;

import kr.co.naamk.domain.admin.TbCmmn;
import kr.co.naamk.domain.admin.TbCmmnGrp;
import kr.co.naamk.web.dto.admin.CmmnDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CmmnMapper {

    CmmnMapper INSTANCE = Mappers.getMapper(CmmnMapper.class);

    /** common group code */
    CmmnDto.CmmnGrpDetailResponse entityToCmmnGrpDetailResponseDTO(TbCmmnGrp entity);

    @Mapping(target = "cmmns", expression = "java(new ArrayList<>())")
    TbCmmnGrp cmmnGrpCreateRequestDtoToEntity(CmmnDto.CmmnGrpCreateRequest dto);

    CmmnDto.CmmnGrpListResponse entityToCmmnGrpListResponseDTO(TbCmmnGrp entity);
    List<CmmnDto.CmmnGrpListResponse> entitiesToCmmnGrpListResponseDTO(List<TbCmmnGrp> entities);


    /** common code */
    TbCmmn cmmnCreateRequestDtoToEntity(CmmnDto.CmmnCreateRequest dto);

    @Mapping(target = "cmmnGrpId", expression = "java(getCmmnGrpId(entity.getCmmnGrp()))")
    CmmnDto.CmmnListResponse entityToCmmnListResponseDTO(TbCmmn entity);
    List<CmmnDto.CmmnListResponse> entitiesToCmmnListResponseDTOs(List<TbCmmn> entities);

    @Mapping(target = "cmmnGrpId", expression = "java(getCmmnGrpId(entity.getCmmnGrp()))")
    CmmnDto.CmmnDetailResponse entityToCmmnDetailResponseDTO(TbCmmn entity);


    default Long getCmmnGrpId(TbCmmnGrp tbCmmnGrp) {
        return tbCmmnGrp.getId();
    }
}
