package kr.co.naamk.web.service.admin;

import kr.co.naamk.domain.admin.TbMenus;
import kr.co.naamk.domain.admin.TbRoles;
import kr.co.naamk.web.dto.admin.MenuDto;
import kr.co.naamk.web.dto.admin.RoleMenuPermDto;

import java.util.List;

public interface RoleMenuPermService {
    // [조회] 역할별 메뉴 권한 트리
    List<MenuDto.MenuTreeResponse> getPermissionTreeByRoleId(Long roleId);

    // [생성] 메뉴 단일 건 생성에 따른 role-menu-perm 추가하기
    void createByMenu(TbMenus menu);

    // [생성] 역할 단일 건 생성에 따른 role-menu-perm 추가
    void createByRole(TbRoles role);

    // [수정] 역할별 메뉴 트리 권한 수정
    void updateActivated(Long roleId, List<RoleMenuPermDto.MenuPermRequest> roleMenuPerms);

    // [수정] 메뉴의 root, el 변화에 따른 role-menu-perm 수정
    void updatePermByMenu(TbMenus menu);
}
