package kr.co.naamk.web.dto.mapstruct;

import kr.co.naamk.domain.TbMenuPerm;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.domain.TbMenus;
import kr.co.naamk.web.dto.PermDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    TbMenus requestDtoToEntity(MenuDto.CreateOrUpdateRequest dto);

    MenuDto.MenuDetailResponse entityToDetailDTO(TbMenus entity);

    @Mapping(target = "permCds", expression = "java(getPermCds(entity.getPermRoles()))")
    @Mapping(target = "children", expression = "java(new ArrayList<>())")
    MenuDto.DisplayTreeResponse entityToDisplayTreeDTO(TbMenus entity);
    List<MenuDto.DisplayTreeResponse> entitiesToDisplayTreeDTOs(List<TbMenus> entities);

    @Mapping(target = "children", expression = "java(new ArrayList<>())")
    MenuDto.ManagementTreeResponse entityToManagementTreeDTO(TbMenus entity);
    List<MenuDto.ManagementTreeResponse> entitiesToManagementTreeDTOs(List<TbMenus> entities);

    @Mapping(target = "perms", expression = "java(getPermsStatusByRole(entity.getPermRoles()))")
    MenuDto.PermissionTreeResponse entityToPermissionTreeDTO(TbMenus entity);
    List<MenuDto.PermissionTreeResponse> entitiesToPermissionTreeDTOs(List<TbMenus> entities);

    default List<String> getPermCds(List<TbMenuPerm> menuPerms) {
        return menuPerms.stream()
                .map(menuPerm -> menuPerm.getPerm().getPermCd())
                .toList();
    }

    default List<PermDto.PermResponse> getPerms(List<TbMenuPerm> menuPerms) {
        return menuPerms.stream().map(menuPerm
                -> PermMapper.INSTANCE.entityToResponseDto(menuPerm.getPerm())).toList();

    }

    default List<PermDto.PermStatusByRole> getPermsStatusByRole(List<TbMenuPerm> menuPerms) {
        return menuPerms.stream().map(menuPerm
                -> PermMapper.INSTANCE.entityToStatusByRoleDto(menuPerm.getPerm())).toList();

    }
}
