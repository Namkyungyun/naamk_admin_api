package kr.co.naamkbank.api.dto.mapstruct;

import kr.co.naamkbank.api.dto.RoleDto;
import kr.co.naamkbank.domain.TbRoles;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto.RoleResponse entityToRoleResponse(TbRoles entity);
}
