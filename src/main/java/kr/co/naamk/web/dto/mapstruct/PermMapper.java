package kr.co.naamk.web.dto.mapstruct;

import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.domain.TbPerms;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PermMapper {
    PermMapper INSTANCE = Mappers.getMapper(PermMapper.class);

    PermDto.PermResponse entityToResponseDto(TbPerms tbPerms);

    PermDto.PermStatusByRole entityToStatusByRoleDto(TbPerms tbPerms);
    List<PermDto.PermStatusByRole> entitiesToStatusByRoleDtos(List<TbPerms> tbPerms);

}
