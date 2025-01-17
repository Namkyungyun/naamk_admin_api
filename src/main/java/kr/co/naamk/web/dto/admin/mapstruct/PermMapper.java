package kr.co.naamk.web.dto.admin.mapstruct;

import kr.co.naamk.web.dto.admin.PermDto;
import kr.co.naamk.domain.admin.TbPerms;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PermMapper {
    PermMapper INSTANCE = Mappers.getMapper(PermMapper.class);

    TbPerms createRequestDtoToEntity(PermDto.PermCreateRequest dto);

    PermDto.PermListResponse entityToListResponseDTO(TbPerms tbPerms);
    List<PermDto.PermListResponse> entitiesToListResponseDTOs(List<TbPerms> tbPerms);

    PermDto.PermDetailResponse entityToDetailResponseDTO(TbPerms tbPerms);

}
