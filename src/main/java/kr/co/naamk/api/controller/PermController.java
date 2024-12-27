package kr.co.naamk.api.controller;

import jakarta.annotation.Nullable;
import kr.co.naamk.api.dto.PermDto;
import kr.co.naamk.api.service.PermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/permission")
public class PermController {

    private final PermService permService;

    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<Object> createPermission(@RequestBody PermDto permDto) {
        try {
            permService.createPermission(permDto);
            return ResponseEntity.ok("OK");

        } catch(Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> getPermissions(@RequestParam @Nullable Long permId) {
        List<PermDto.PermResponse> permissions = permService.getPermissions(permId);

        return ResponseEntity.ok(permissions);
    }


}
