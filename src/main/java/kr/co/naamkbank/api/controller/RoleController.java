package kr.co.naamkbank.api.controller;

import kr.co.naamkbank.api.dto.RoleDto;
import kr.co.naamkbank.api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/role")
public class RoleController {

    private final RoleService roleService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> saveRole(@RequestBody RoleDto.RoleRequest roleDto) {
        try {
            roleService.createRole(roleDto);
            return ResponseEntity.ok("OK");

        } catch(Exception e) {
            throw e;
        }
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<Object> getRoles(@RequestParam(required = false) Long roleId) {
        try {
            List<RoleDto.RoleResponse> list = roleService.getRoles(roleId);

            Object result = (roleId != null) ? list.getFirst() : list;

            return ResponseEntity.ok(result);
        } catch(Exception e) {
            throw e;
        }
    }
}
