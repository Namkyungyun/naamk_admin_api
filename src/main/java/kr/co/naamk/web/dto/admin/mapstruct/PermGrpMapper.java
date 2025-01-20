package kr.co.naamk.web.dto.admin.mapstruct;

import kr.co.naamk.domain.admin.TbPermGrpPerm;
import kr.co.naamk.domain.admin.TbPermGrps;
import kr.co.naamk.web.dto.admin.PermGrpDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PermGrpMapper {
    PermGrpMapper INSTANCE = Mappers.getMapper(PermGrpMapper.class);

    @Mapping(target = "permGrpPerm", expression = "java(new ArrayList<>())")
    TbPermGrps createRequestDtoToEntity(PermGrpDto.PermGrpCreateRequest dto);

    PermGrpDto.PermGrpListResponse entityToListResponseDTO(TbPermGrps entity);
    List<PermGrpDto.PermGrpListResponse> entitiesToListResponseDTOs(List<TbPermGrps> entities);

    @Mapping(target = "childPerms", expression = "java(getChildPerms(entity.getPermGrpPerm()))")
    PermGrpDto.PermGrpDetailResponse entityToDetailResponseDTO(TbPermGrps entity);


    default List<PermGrpDto.ChildPermStatus> getChildPerms(List<TbPermGrpPerm> child) {
        return child.stream()
                .map(el -> new PermGrpDto.ChildPermStatus(el.getPerm().getId(), el.isActivated()))
                .toList();
    }

}
