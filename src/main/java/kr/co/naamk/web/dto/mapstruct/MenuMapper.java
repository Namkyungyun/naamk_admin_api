package kr.co.naamk.web.dto.mapstruct;

import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.domain.TbMenus;
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
