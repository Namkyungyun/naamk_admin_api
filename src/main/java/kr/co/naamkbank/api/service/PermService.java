package kr.co.naamkbank.api.service;

import kr.co.naamkbank.api.dto.PermDto;
import kr.co.naamkbank.api.dto.mapstruct.PermMapper;
import kr.co.naamkbank.api.repository.PermRepository;
import kr.co.naamkbank.domain.TbPerms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermService {

    private final PermRepository permRepository;

    @Transactional
    public void createPermission(PermDto dto) {
        TbPerms perm = TbPerms.builder()
                .permNm(dto.getPermNm())
                .permCd(dto.getPermCd())
                .build();

        permRepository.save(perm);
    }

    @Transactional(readOnly = true)
    public List<PermDto.PermResponse> getPermissions(Long permId) {
        List<PermDto.PermResponse> result;
        List<TbPerms> list;

        if(permId == null) {
            list = permRepository.findAll().stream().toList();
        } else {
            list = permRepository.findById(permId).stream().toList();
        }

        result = list.stream().map(PermMapper.INSTANCE::entityToResponseDto).toList();

        return result;
    }

}
