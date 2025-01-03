package kr.co.naamk.web.service.impl;

import jakarta.annotation.Nullable;
import kr.co.naamk.domain.*;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.mapstruct.MenuMapper;
import kr.co.naamk.web.dto.mapstruct.PermMapper;
import kr.co.naamk.web.repository.jpa.MenuPermsRepository;
import kr.co.naamk.web.repository.jpa.MenuRepository;
import kr.co.naamk.web.repository.jpa.PermRepository;
import kr.co.naamk.web.repository.jpa.RoleRepository;
import kr.co.naamk.web.repository.queryDSL.MenuQueryRepository;
import kr.co.naamk.web.service.RoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl implements RoleMenuService {

    private final MenuQueryRepository menuQueryRepository;
    private final MenuPermsRepository menuPermsRepository;
    private final PermRepository permRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    /** 역할별로 메뉴에 대한 권한을 생성 **/
    @Override
    @Transactional
    public void saveMenuPermission(List<MenuDto.MenuPermissionRequest> dtos) {
        List<TbMenuPerm> listOfSave = new ArrayList<>();
        List<TbPerms> allPerms = getAllPerms();
        Set<Long> allPermIds = allPerms.stream()
                .map(TbPerms::getId).collect(Collectors.toSet());

        for(MenuDto.MenuPermissionRequest dto : dtos) {

            // 잘못된 요청 값이 있는지 check
            TbRoles role = getRoleById(dto.getRoleId());
            TbMenus menu = getMenuById(dto.getMenuId());

            List<Long> permIds = dto.getPermIds();
            List<Long> invalidPermIds =  permIds.stream()
                    .filter(o -> !allPermIds.contains(o)).toList();

            if(!invalidPermIds.isEmpty()) {
                throw new ServiceException(ServiceMessageType.PERMISSION_NOT_FOUND, "invalid ids : " +invalidPermIds);
            }

            listOfSave.addAll(allPerms.stream()
                    .filter(o -> permIds.contains(o.getId()))
                    .map(perm -> TbMenuPerm.builder()
                            .role(role)
                            .menu(menu)
                            .perm(perm)
                            .build()
                    )
                    .toList());

            // delete
            deleteMenuPermission(menu.getId(), role.getId(), allPerms);
        }

        menuPermsRepository.saveAll(listOfSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDto.PermissionTreeResponse> getPermissionTreeByRoleId(Long roleId) {
        // 역할 확인
        TbRoles tbRole = getRoleById(roleId);

        // MenuDto.MenuPermissionSearch 생성
        MenuDto.MenuPermissionSearch search = MenuDto.MenuPermissionSearch.builder()
                .roleId(tbRole.getId())
                .build();

        // 1. parent 메뉴 조회
        List<TbMenus> menuWithPerms = menuQueryRepository.findMenuWithPermsBySearch(search);

        // 2. parent DTOs 변환
        List<MenuDto.PermissionTreeResponse> menuWithPermsDTOs
                = MenuMapper.INSTANCE.entitiesToPermissionTreeDTOs(menuWithPerms);

        // 2-1. 전체 permission 가져오기
        List<PermDto.PermStatusByRole> allPermDTOs = PermMapper.INSTANCE.entitiesToStatusByRoleDtos(getAllPerms());

        // 3. 자식 메뉴 재귀
        menuWithPermsDTOs.forEach(rootMenu -> {
            rootMenu.setPerms(makePermissionCheckBoxData(rootMenu.getPerms(), allPermDTOs));
            setChildrenMenuTree(rootMenu, rootMenu.getId(), search, allPermDTOs);
        });

        return menuWithPermsDTOs;
    }


    private void deleteMenuPermission(Long menuId, Long roleId ,List<TbPerms> perms) {
        List<TbMenuPermIds> listOfDelete = new ArrayList<>();

        for(TbPerms perm : perms) {
            TbMenuPermIds compositeIds = new TbMenuPermIds();

            Long id = perm.getId();

            compositeIds.setPerm(id);
            compositeIds.setMenu(menuId);
            compositeIds.setRole(roleId);

            listOfDelete.add(compositeIds);
        }

        menuPermsRepository.deleteAllById(listOfDelete);
    }

    private void setChildrenMenuTree(Object parentDTO, Long parentId,
                                     MenuDto.MenuPermissionSearch parentSearch,
                                     List<PermDto.PermStatusByRole> allPerms) {
        parentSearch.setParentId(parentId);

        List<TbMenus> children = menuQueryRepository.findMenuWithPermsBySearch(parentSearch);
        List<MenuDto.PermissionTreeResponse> childrenDTOs = MenuMapper.INSTANCE.entitiesToPermissionTreeDTOs(children);

        // recursive
        List<MenuDto.PermissionTreeResponse> list = childrenDTOs.stream().peek(dto -> {
            // make PermDto.PermStatusByRole's activated value
            if(allPerms != null) dto.setPerms(makePermissionCheckBoxData(dto.getPerms(), allPerms));

            setChildrenMenuTree(dto, dto.getId(), parentSearch, allPerms);
        }).toList();

        // set dto value
        ((MenuDto.PermissionTreeResponse) parentDTO).setChildren(list);
    }

    /** 메뉴트리 체크 박스에 대한 activated 만들기 */
    private List<PermDto.PermStatusByRole> makePermissionCheckBoxData(List<PermDto.PermStatusByRole> perms,
                                                                      List<PermDto.PermStatusByRole> allPerms) {
        // allPerms 에 없는 값 추출
        Set<Long> permsIds = perms.stream()
                .map(PermDto.PermStatusByRole::getId)
                .collect(Collectors.toSet());

        // allPerms [1, 2, 3, 4] , perms [1, 2, 3] -> extract [4]
        List<PermDto.PermStatusByRole> missingPerms = allPerms.stream()
                .filter(dto -> !permsIds.contains(dto.getId()))
                .peek(dto -> dto.setActivated(false))
                .toList();

        // perms 에 없는 값 추가 후 리턴
        return Stream.concat(perms.stream(), missingPerms.stream())
                .sorted(Comparator.comparing(PermDto.PermStatusByRole::getId)) // id로 정렬
                .toList();
    }

    private List<TbPerms> getAllPerms() {
        return permRepository.findAll();
    }

    private TbRoles getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.USER_NOT_FOUND));
    }

    private TbMenus getMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.MENU_NOT_FOUND));
    }

}
