package kr.co.naamk.web.dto.admin.mapstruct;

import kr.co.naamk.web.dto.admin.RoleDto;
import kr.co.naamk.web.dto.admin.UserDto;
import kr.co.naamk.domain.admin.TbUserRole;
import kr.co.naamk.domain.admin.TbUsers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    TbUsers createRequestDtoToEntity(UserDto.CreateRequest dto);

    @Mapping(target = "roles", expression = "java(getRoleResponseDTOs(entity.getUserRoles()))")
    UserDto.ListResponse entityToListResponseDTO(TbUsers entity);
    List<UserDto.ListResponse> entitiesToListResponseDTOs(List<TbUsers> entities);

    @Mapping(target = "roles", expression = "java(getRoleResponseDTOs(entity.getUserRoles()))")
    UserDto.DetailResponse entityToDetailResponseDTO(TbUsers entity);


    default List<RoleDto.RoleListResponse> getRoleResponseDTOs(List<TbUserRole> userRoles) {
        return userRoles.stream()
                .map(userRole -> RoleMapper.INSTANCE.entityToListResponseDTO(userRole.getRole()))
                .toList();
    }
}
