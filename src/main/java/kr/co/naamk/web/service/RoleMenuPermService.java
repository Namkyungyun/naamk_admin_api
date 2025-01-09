package kr.co.naamk.web.service;

import kr.co.naamk.domain.TbMenus;
import kr.co.naamk.domain.TbRoles;
import kr.co.naamk.web.dto.MenuDto;

import java.util.List;

public interface RoleMenuPermService {
    // [생성] 역할에 대해서 현재의 메뉴들에 대한 권한 자체가 없는 경우 생성
    void createRoleMenuPerms();

    // [생성] 메뉴 단일 건 생성에 따른 role-menu-perm 추가하기
    void createRoleMenuPermsByMenu(TbMenus menu);

    // [생성] 역할 단일 건 생성에 따른 role-menu-perm 추가
    void createRoleMenuPermsByRole(TbRoles role);



    // [조회] 역할별 메뉴 권한 트리
    List<MenuDto.MenuTreeResponse> getPermissionTreeByRoleId(Long roleId);
    // [수정] 역할별 메뉴 트리 권한 수정
    // [생성] 역할 생성
}
