package kr.co.naamkbank.api.dto.mapstruct;

import kr.co.naamkbank.api.dto.UserDto;
import kr.co.naamkbank.domain.TbUsers;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    TbUsers userDtoToEntity(UserDto userDto);

    UserDto.UserResponse entityToUserDto(TbUsers entity);
}
