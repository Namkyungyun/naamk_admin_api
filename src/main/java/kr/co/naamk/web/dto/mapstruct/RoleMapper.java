package kr.co.naamk.web.dto.mapstruct;

import kr.co.naamk.domain.TbRoles;
import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    TbRoles roleRequestDtoToEntity(RoleDto.RoleRequest dto);

//    @Mapping(target = "perms", expression = "java(getPermResponseDtos(entity.getRolePerms()))")
    RoleDto.RoleResponse entityToRoleResponse(TbRoles entity);

    List<RoleDto.RoleResponse> entitiesToRoleResponseDtos(List<TbRoles> entities);


//    default List<PermDto.PermResponse> getPermResponseDtos(List<TbRolePerm> rolePerms) {
//        return rolePerms.stream()
//                .map(rolePerm -> PermMapper.INSTANCE.entityToResponseDto(rolePerm.getPerm()))
//                .toList();
//    }
}
