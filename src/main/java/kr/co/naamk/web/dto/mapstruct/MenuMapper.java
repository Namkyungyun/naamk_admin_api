package kr.co.naamk.web.dto.mapstruct;

import kr.co.naamk.domain.TbPerms;
import kr.co.naamk.domain.TbRoleMenuPerm;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.domain.TbMenus;
import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.RoleMenuPermDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    TbMenus requestDtoToEntity(MenuDto.MenuCreateRequest dto);

    MenuDto.MenuDetailResponse entityToDetailDTO(TbMenus entity);


    @Mapping(target = "rootId", expression = "java((Long) row.get(\"root_id\"))")
    @Mapping(target = "parentId", expression = "java((Long) row.get(\"parent_id\"))")
    @Mapping(target = "id", expression = "java((Long) row.get(\"id\"))")
    @Mapping(target = "level", expression = "java((Integer) row.get(\"level\"))")
    @Mapping(target = "orderNum", expression = "java((Integer) row.get(\"order_num\"))")
    @Mapping(target = "path", expression = "java((String) row.get(\"path\"))")
    MenuDto.MenuTree objToMenuTreeDTO(Map<String, Object> row);
    List<MenuDto.MenuTree> objsToMenuTreeDTOS(List<Map<String, Object>> rows);


    @Mapping(target = "perms", expression = "java(new ArrayList<>())")
    @Mapping(target = "children", expression = "java(new ArrayList<>())")
    MenuDto.MenuTreeResponse entityToMenuTreeResponseDTO(TbMenus entity);



    /** 메뉴 트리 생성
     * (1) root, child 모두 맵에 담기
     * (2) menuTrees instance가 rootMenus, childMenus에 없는 값이라면
     * 최종 menuTree Object에 없음.
     * */
    default List<MenuDto.MenuTreeResponse> generateMenuTree(Map<Long, MenuDto.MenuTreeResponse> rootMenuMap,
                                                            Map<Long, MenuDto.MenuTreeResponse> allMenuMap,
                                                            List<MenuDto.MenuTree> menuTrees) {
        // 메뉴 트리 instance 담을 map (초기값으로 순서가 정렬된 rootMenuMap 넣기)
        Map<Long, MenuDto.MenuTreeResponse> menuTreeMap = new ConcurrentHashMap<>(rootMenuMap);

        for(MenuDto.MenuTree dto : menuTrees ) {
            if(dto.getParentId() != null) {
                Long id = dto.getId();
                Long rootId = dto.getRootId();
                Long parentId = dto.getParentId();

                // 전체 메뉴 트리 이므로, rootMenus와 childMenus에 포함 여부 체크
                if(!allMenuMap.containsKey(parentId)) continue;
                if(!allMenuMap.containsKey(id)) continue;

                // 필터링 맵의 current obj 확인
                MenuDto.MenuTreeResponse currentMenu = allMenuMap.get(id);

                // 필터링 맵의 parent obj 확인
                MenuDto.MenuTreeResponse parentMenu = allMenuMap.get(parentId);
                parentMenu.getChildren().add(currentMenu);

                // 필터링 맵의 root obj 확인
                if(Objects.equals(parentId, rootId)) {
                    menuTreeMap.put(rootId, parentMenu);
                }
            }
        }

        return menuTreeMap.values().stream().toList();
    }

    // 반복 map converting 함수화
    default Map<Long, MenuDto.MenuTreeResponse> entityToMap(List<TbMenus> entities) {
        Map<Long, MenuDto.MenuTreeResponse> map = new ConcurrentHashMap<>();

        for(TbMenus entity : entities) {
            Long key = entity.getId();
            MenuDto.MenuTreeResponse value = entityToMenuTreeResponseDTO(entity);

            List<PermDto.PermResponse> perms = getPerms(entity.getRoleMenuPerms());
            value.getPerms().addAll(perms);

            map.put(key, value);
        }

        return map;
    }

    default Map<Long, MenuDto.MenuTreeResponse> entityToMap(List<TbMenus> entities, Long roleId) {
        Map<Long, MenuDto.MenuTreeResponse> map = new ConcurrentHashMap<>();

        for(TbMenus entity : entities) {
            Long key = entity.getId();
            MenuDto.MenuTreeResponse value = entityToMenuTreeResponseDTO(entity);

            List<RoleMenuPermDto.MenuPermResponse> perms = getMenuPermsByRoleId(entity.getRoleMenuPerms(), roleId);
            value.getPerms().addAll(perms);

            map.put(key, value);
        }

        return map;
    }

    default Map<Long, MenuDto.MenuTreeResponse> entityToMap(List<TbMenus> entities, List<Long> roleIds) {
        Map<Long, MenuDto.MenuTreeResponse> map = new ConcurrentHashMap<>();

        for(TbMenus entity : entities) {
            Long key = entity.getId();
            MenuDto.MenuTreeResponse value = entityToMenuTreeResponseDTO(entity);

            List<RoleMenuPermDto.MenuPermResponse> perms = getMenuPermsByRoleIds(entity.getRoleMenuPerms(), roleIds);
            value.getPerms().addAll(perms);

            map.put(key, value);
        }

        return map;
    }



    default List<PermDto.PermResponse> getPerms(List<TbRoleMenuPerm> roleMenuPerms) {
        List<TbPerms> perms = roleMenuPerms.stream()
                .map(TbRoleMenuPerm::getPerm)
                .toList();

        List<TbPerms> distinctPerms =   new ArrayList<>(perms.stream()
                .collect(Collectors.toMap(TbPerms::getId, perm -> perm, (existing, replacement) -> existing))
                .values());

        return  distinctPerms.stream()
                .map(PermMapper.INSTANCE::entityToResponseDto)
                .toList();
    }

    default List<RoleMenuPermDto.MenuPermResponse> getMenuPermsByRoleId(List<TbRoleMenuPerm> roleMenuPerms, Long roleId) {
        return roleMenuPerms.stream()
                .filter(el -> el.getRole().getId().equals(roleId))
                .map(el -> {
                    RoleMenuPermDto.MenuPermResponse menuPermResponse = new RoleMenuPermDto.MenuPermResponse();
                    menuPermResponse.setPermId(el.getPerm().getId());
                    menuPermResponse.setActivated(el.isActivated());

                    return menuPermResponse;
                })
                .sorted(Comparator.comparing(RoleMenuPermDto.MenuPermResponse::getPermId))
                .distinct()
                .toList();
    }

    default List<RoleMenuPermDto.MenuPermResponse> getMenuPermsByRoleIds(List<TbRoleMenuPerm> roleMenuPerms, List<Long> roleIds) {
        return roleMenuPerms.stream()
                .filter(el -> roleIds.contains(el.getRole().getId()))
                .map(el -> {
                    RoleMenuPermDto.MenuPermResponse menuPermResponse = new RoleMenuPermDto.MenuPermResponse();
                    menuPermResponse.setPermId(el.getPerm().getId());
                    menuPermResponse.setActivated(el.isActivated());

                    return menuPermResponse;
                })
                .sorted(Comparator.comparing(RoleMenuPermDto.MenuPermResponse::getPermId))
                .distinct()
                .toList();
    }

}
