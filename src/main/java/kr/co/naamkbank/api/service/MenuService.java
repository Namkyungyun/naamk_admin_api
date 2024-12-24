package kr.co.naamkbank.api.service;

import kr.co.naamkbank.api.dto.MenuDto;
import kr.co.naamkbank.api.dto.PermDto;
import kr.co.naamkbank.api.dto.mapstruct.MenuMapper;
import kr.co.naamkbank.api.dto.mapstruct.PermMapper;
import kr.co.naamkbank.api.repository.MenuPermRepository;
import kr.co.naamkbank.api.repository.MenuRepository;
import kr.co.naamkbank.api.repository.PermRepository;
import kr.co.naamkbank.api.repository.UserRepository;
import kr.co.naamkbank.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final PermRepository permRepository;
    private final MenuPermRepository menuPermRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createMenu(MenuDto.MenuRequest menuDto) {
        TbMenus menu = MenuMapper.INSTANCE.requestDtoToEntity(menuDto);
        TbMenus savedEntity = menuRepository.save(menu);

        for(Long permId : menuDto.getPermIds()) {
            TbPerms permEntity = permRepository.findById(permId).orElseThrow(()-> new NullPointerException("no perm"));
            TbMenuPermIds compositeId = TbMenuPermIds.builder()
                    .menuId(savedEntity.getId())
                    .permId(permEntity.getId())
                    .build();

            TbMenuPerm menuPermEntity = TbMenuPerm.builder()
                    .id(compositeId)
                    .menu(savedEntity)
                    .perm(permEntity)
                    .build();

            menuPermRepository.save(menuPermEntity);
        }
    }

    @Transactional(readOnly = true)
    public List<MenuDto.MenuTreeDetailResponse> getMenusByMenuId(Long menuId) {
        List<TbMenus> list;
        if(menuId == null) {
            list = menuRepository.findAll();
        } else {
            list = menuRepository.findById(menuId).stream().toList();
        }

        List<MenuDto.MenuTreeDetailResponse> result = new ArrayList<>();
        for(TbMenus menu : list) {
            final MenuDto.MenuTreeDetailResponse response = MenuMapper.INSTANCE.entityToMenuTreeDetailDto(menu);

            List<PermDto.PermResponse> perms = Objects.requireNonNull(menu).getMenuPerms().stream().map(menuPerm -> {
                TbPerms entity = menuPerm.getPerm();
                return PermMapper.INSTANCE.entityToResponseDto(entity);
            }).toList();

            response.setPerms(perms);
            result.add(response);
        }
        return result;
    }

    // TODO !!!! In이 들어가는 부분 삭제
//    @Transactional(readOnly = true)
//    public List<MenuDto.MenuTreeDetailResponse> getAdminMenuTreeByUserId(Long userId, Long menuId) {
//        // 1. 유저 정보 가져오기
//        TbUsers user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("no User"));
//
//        // 2. 해당 유저의 role list에서 permId list 가져오기
//        List<TbUserRole> userRoles = user.getUserRoles();
//
//        // 3. 위의 권한이 부여된 최상위 메뉴 가져오기
//        List<TbMenus> rootMenus = menuRepository.nativeFindByParentIdNullAndPermIdIsIn(permIds);
//
//        List<MenuDto.MenuTreeDetailResponse> rootMenusResponse = rootMenus.stream().map(MenuMapper.INSTANCE::entityToMenuTreeDetailDto).toList();
//
//        // 4. 재귀적으로 자식 메뉴 가져오기
//        rootMenusResponse.forEach(rootMenu -> setChildrenMenuTree(rootMenu, permIds));
//
//        // dto로 변환
//        return rootMenusResponse;
//    }


    @Transactional(readOnly = true)
    public List<MenuDto.MenuTreeResponse> getDisplayMenuTreeByUserId(Long userId) {

        // 1. 유저 정보 가져오기
        TbUsers user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("no User"));

        // 2. 해당 유저의 role list에서 permId list 가져오기
        List<TbUserRole> userRoles = user.getUserRoles();
        List<Long> permIds = userRoles.stream()
                .flatMap(userRoleEntity -> userRoleEntity.getRole().getRolePerms().stream().map(TbRolePerm::getPerm))
                .map(TbPerms::getId)
                .toList();

        // 3. 위의 권한이 부여된 최상위 메뉴 가져오기
        List<TbMenus> rootMenus = menuRepository.nativeFindByParentIdNullAndPermIdIsIn(permIds);

        List<MenuDto.MenuTreeResponse> rootMenusResponse = rootMenus.stream().map(MenuMapper.INSTANCE::entityToMenuTreeDto).toList();

        // 4. 재귀적으로 자식 메뉴 가져오기
        rootMenusResponse.forEach(rootMenu -> setChildrenMenuTree(rootMenu, permIds));

        // dto로 변환
        return rootMenusResponse;
    }

    private void setChildrenMenuTree(MenuDto.MenuTreeResponse parentMenu, List<Long> permIds) {
        List<TbMenus> children = menuRepository.nativeFindByParentIdAndPermIdIsIn(parentMenu.getId(), permIds);
        List<MenuDto.MenuTreeResponse> childrenResponse = children.stream().map(MenuMapper.INSTANCE::entityToMenuTreeDto).toList();

        childrenResponse.forEach(el -> setChildrenMenuTree(el, permIds));
        parentMenu.setChildren(childrenResponse);
    }

    private void setChildrenMenuTreeDetail(MenuDto.MenuTreeDetailResponse parentMenu, List<Long> permIds) {
        List<TbMenus> children = menuRepository.nativeFindByParentIdAndPermIdIsIn(parentMenu.getId(), permIds);
        List<MenuDto.MenuTreeDetailResponse> childrenResponse = children.stream().map(MenuMapper.INSTANCE::entityToMenuTreeDetailDto).toList();

        childrenResponse.forEach(el -> setChildrenMenuTreeDetail(el, permIds));
        parentMenu.setChildren(childrenResponse);
    }

}
