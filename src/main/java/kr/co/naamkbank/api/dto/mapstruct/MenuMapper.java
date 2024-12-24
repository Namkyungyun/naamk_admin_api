package kr.co.naamkbank.api.dto.mapstruct;

import kr.co.naamkbank.api.dto.MenuDto;
import kr.co.naamkbank.domain.TbMenus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    TbMenus requestDtoToEntity(MenuDto.MenuRequest dto);

    MenuDto.MenuTreeResponse entityToMenuTreeDto(TbMenus entity);

    MenuDto.MenuTreeDetailResponse entityToMenuTreeDetailDto(TbMenus entity);

}
