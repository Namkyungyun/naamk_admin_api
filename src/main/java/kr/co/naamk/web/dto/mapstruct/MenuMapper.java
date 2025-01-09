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
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    TbMenus requestDtoToEntity(MenuDto.CreateRequest dto);

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
    List<MenuDto.MenuTreeResponse> entitiesToMenuTreeResponseDTOs(List<TbMenus> entities);

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

    default List<RoleMenuPermDto.MenuPerm> getMenuPerms(List<TbRoleMenuPerm> roleMenuPerms) {
        return roleMenuPerms.stream()
                .map(el -> {
                    RoleMenuPermDto.MenuPerm menuPerm = new RoleMenuPermDto.MenuPerm();
                    menuPerm.setPermId(el.getPerm().getId());
                    menuPerm.setActivated(el.isActivated());

                    return menuPerm;
                })
                .sorted(Comparator.comparing(RoleMenuPermDto.MenuPerm::getPermId))
                .distinct()
                .toList();
    }

    /** 메뉴 트리 생성
     * (1) root, child 모두 맵에 담기
     * (2) menuTrees instance가 rootMenus, childMenus에 없는 값이라면
     * 최종 menuTree Object에 없음.
     * */
    default<T> List<MenuDto.MenuTreeResponse> generateMenuTree(List<TbMenus> rootMenus,
                                                           List<TbMenus> childMenus,
                                                           List<MenuDto.MenuTree> menuTrees,
                                                               Class<T> permClazz) {
        // root map 으로 변경
        Map<Long, MenuDto.MenuTreeResponse> rootMenuMap = convertToMenuTreeResponseMap(rootMenus, permClazz);

        // child map 으로 변경
        List<TbMenus> allMenus = Stream.concat(rootMenus.stream(), childMenus.stream()).toList();
        Map<Long, MenuDto.MenuTreeResponse> allMenuMap = convertToMenuTreeResponseMap(allMenus, permClazz);

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
    default <T> Map<Long, MenuDto.MenuTreeResponse> convertToMenuTreeResponseMap(List<TbMenus> entities, Class<T> clazz) {
        Map<Long, MenuDto.MenuTreeResponse> map = new ConcurrentHashMap<>();
        for(TbMenus entity : entities) {
            Long key = entity.getId();
            MenuDto.MenuTreeResponse value = MenuMapper.INSTANCE.entityToMenuTreeResponseDTO(entity);

            if(clazz.equals(PermDto.PermResponse.class)) {
                List<PermDto.PermResponse> perms = MenuMapper.INSTANCE.getPerms(entity.getRoleMenuPerms());
                value.getPerms().addAll(perms);

            } else if (clazz.equals(RoleMenuPermDto.MenuPerm.class)) {
                List<RoleMenuPermDto.MenuPerm> perms = MenuMapper.INSTANCE.getMenuPerms(entity.getRoleMenuPerms());
                value.getPerms().addAll(perms);
            }

            map.put(key, value);
        }

        return map;
    }

}
