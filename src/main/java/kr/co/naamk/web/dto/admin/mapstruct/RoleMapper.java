package kr.co.naamk.web.dto.admin.mapstruct;

import kr.co.naamk.domain.admin.TbRoles;
import kr.co.naamk.web.dto.admin.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    TbRoles createRequestDTOToEntity(RoleDto.RoleCreateRequest dto);

    RoleDto.RoleDetailResponse entityToDetailResponseDTO(TbRoles role);

    RoleDto.RoleListResponse entityToListResponseDTO(TbRoles entity);

    List<RoleDto.RoleListResponse> entitiesToListResponseDTOs(List<TbRoles> entities);

}
