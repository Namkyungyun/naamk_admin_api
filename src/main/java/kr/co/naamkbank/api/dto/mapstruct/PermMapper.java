package kr.co.naamkbank.api.dto.mapstruct;

import kr.co.naamkbank.api.dto.PermDto;
import kr.co.naamkbank.domain.TbPerms;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PermMapper {
    PermMapper INSTANCE = Mappers.getMapper(PermMapper.class);

    PermDto.PermResponse entityToResponseDto(TbPerms tbPerms);


}
