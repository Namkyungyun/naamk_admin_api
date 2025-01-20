package kr.co.naamk.web.service;

import kr.co.naamk.domain.TbCmmn;
import kr.co.naamk.domain.TbCmmnGrp;
import kr.co.naamk.web.dto.CmmnDto;
import kr.co.naamk.web.repository.jpa.CmmnGrpRepository;
import kr.co.naamk.web.repository.jpa.CmmnRepository;
import kr.co.naamk.web.service.impl.CmmnServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CmmnServiceTest {

    @InjectMocks
    private CmmnServiceImpl cmmnService;

    @Mock
    private CmmnRepository cmmnRepository;

    @Mock
    private CmmnGrpRepository cmmnGrpRepository;


    private TbCmmnGrp getCmmnGrpEntity() {
        TbCmmnGrp entity = new TbCmmnGrp();

        entity.setId(1L);
        entity.setCmmnGrpNm("test name");
        entity.setCmmnGrpCd("TEST_CD");
        entity.setCmmnGrpDesc("test desc");
        entity.setCmmns(new ArrayList<>());
        entity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        entity.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        return entity;
    }

    private TbCmmn getCmmnEntity() {
        TbCmmn entity = new TbCmmn();

        entity.setId(1L);
        entity.setCmmnNm("test name");
        entity.setCmmnCd("TEST_CD");
        entity.setCmmnDesc("test desc");
        entity.setOrderNum(1);
        entity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        entity.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        return entity;
    }

    @Test
    void createCmmnGrp() {
        // given
        CmmnDto.CmmnGrpCreateRequest dto = new CmmnDto.CmmnGrpCreateRequest();
        dto.setCmmnGrpNm("testName");
        dto.setCmmnGrpCd("TEST_CD");
        dto.setCmmnGrpDesc("test desc");
        dto.setActivated(true);


        // stub 1
        when(cmmnGrpRepository.existsByCmmnGrpCd(any())).thenReturn(Boolean.FALSE);
        when(cmmnGrpRepository.save(any())).thenAnswer(invocation -> {
            TbCmmnGrp argument = invocation.getArgument(0);
            argument.setId(2L);
            argument.setCmmnGrpCd(dto.getCmmnGrpCd());
            argument.setCmmnGrpNm(dto.getCmmnGrpNm());
            argument.setCmmnGrpDesc(dto.getCmmnGrpDesc());
            argument.setActivated(dto.isActivated());

            return argument;
        });

        // when
        CmmnDto.CmmnGrpCreateResponse result = cmmnService.createCmmnGrp(dto);

        // then
        // then 호출 테스트
        verify(cmmnGrpRepository).existsByCmmnGrpCd(any());
        verify(cmmnGrpRepository).save(any());

        // 검증
        Assertions.assertThat(result.getId()).isEqualTo(2L);
    }


    @Test
    void updateCmmnGrp() {
        // given
        CmmnDto.CmmnGrpUpdateRequest dto = new CmmnDto.CmmnGrpUpdateRequest();
        dto.setCmmnGrpNm("test name updated");
        dto.setCmmnGrpDesc("test desc updated");
        dto.setActivated(Boolean.FALSE);

        TbCmmnGrp entity = getCmmnGrpEntity();

        // stub 1
        when(cmmnGrpRepository.findById(any())).thenReturn(Optional.of(entity));

        // stub 2
        when(cmmnGrpRepository.save(any())).thenAnswer(invocationOnMock -> {
            TbCmmnGrp argument = invocationOnMock.getArgument(0);

            argument.setCmmnGrpNm(dto.getCmmnGrpNm());
            argument.setCmmnGrpDesc(dto.getCmmnGrpDesc());
            argument.setActivated(dto.isActivated());

            return argument;
        });

        // when
        CmmnDto.CmmnGrpDetailResponse savedEntity = cmmnService.updateCmmnGrp(entity.getId(), dto);

        // then
        // then 호출
        verify(cmmnGrpRepository).findById(any());
        verify(cmmnGrpRepository).save(any());

        // then 검증
        Assertions.assertThat(savedEntity.getCmmnGrpNm()).isEqualTo(dto.getCmmnGrpNm());
        Assertions.assertThat(savedEntity.getCmmnGrpDesc()).isEqualTo(dto.getCmmnGrpDesc());
        Assertions.assertThat(savedEntity.getCmmnGrpNm()).isEqualTo(dto.getCmmnGrpNm());
    }

    @Test
    void deleteCmmnGrp() {
        // given
        TbCmmnGrp entity = getCmmnGrpEntity();
        Long id = getCmmnGrpEntity().getId();

        // stub
        when(cmmnGrpRepository.findById(id)).thenReturn(Optional.of(entity));

        // when
        cmmnService.deleteCmmnGrp(id);

        // then
        verify(cmmnGrpRepository).findById(id);
        verify(cmmnGrpRepository).delete(entity);
    }

    @Test
    void getAllCmmnGrps() {
        // given
        TbCmmnGrp entity1 = getCmmnGrpEntity();

        TbCmmnGrp entity2 = getCmmnGrpEntity();
        entity2.setId(2L);

        TbCmmnGrp entity3 = getCmmnGrpEntity();
        entity3.setId(3L);

        // stub
        when(cmmnGrpRepository.findAll()).thenReturn(List.of(entity1, entity2, entity3));

        // when
        List<CmmnDto.CmmnGrpListResponse> result = cmmnService.getAllCmmnGrps();

        // then
        verify(cmmnGrpRepository).findAll();
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void getDetailCmmnGrp() {
        // given
        TbCmmnGrp entity = getCmmnGrpEntity();
        Long id = getCmmnGrpEntity().getId();

        // stub
        when(cmmnGrpRepository.findById(any())).thenReturn(Optional.of(entity));

        // when
        CmmnDto.CmmnGrpDetailResponse result = cmmnService.getDetailCmmnGrp(id);

        // then
        verify(cmmnGrpRepository).findById(any());
        Assertions.assertThat(result.getCmmnGrpCd()).isEqualTo(entity.getCmmnGrpCd());
    }

    @Test
    void createCmmn() {
        // given
        CmmnDto.CmmnCreateRequest dto = new CmmnDto.CmmnCreateRequest();
        dto.setCmmnCd("EL_CD");
        dto.setCmmnNm("test el nm");
        dto.setCmmnDesc("test el desc");
        dto.setOrderNum(1);
        dto.setActivated(Boolean.TRUE);

        TbCmmnGrp grpEntity = getCmmnGrpEntity();

        // stub
        when(cmmnRepository.existsByCmmnCdAndCmmnGrpId(any(), any())).thenReturn(Boolean.FALSE);
        when(cmmnGrpRepository.findById(any())).thenReturn(Optional.of(grpEntity));
        when(cmmnRepository.save(any())).thenAnswer(invocationOnMock -> {
            TbCmmn entity = getCmmnEntity();

            entity.setCmmnGrp(grpEntity);
            entity.setCmmnCd(dto.getCmmnCd());
            entity.setCmmnNm(dto.getCmmnNm());
            entity.setCmmnDesc(dto.getCmmnDesc());
            entity.setOrderNum(dto.getOrderNum());
            entity.setActivated(dto.isActivated());

            return entity;
        });

        // when
        cmmnService.createCmmn(grpEntity.getId(), dto);

        // then
        verify(cmmnRepository).existsByCmmnCdAndCmmnGrpId(any(), any());
        verify(cmmnGrpRepository).findById(any());
        verify(cmmnRepository).save(any());

        ArgumentCaptor<TbCmmn> captor = ArgumentCaptor.forClass(TbCmmn.class);
        verify(cmmnRepository).save(captor.capture());
        TbCmmn savedEntity = captor.getValue();

        Assertions.assertThat(savedEntity.getCmmnCd()).isEqualTo(dto.getCmmnCd());
        Assertions.assertThat(savedEntity.getCmmnNm()).isEqualTo(dto.getCmmnNm());
        Assertions.assertThat(savedEntity.getCmmnDesc()).isEqualTo(dto.getCmmnDesc());
        Assertions.assertThat(savedEntity.isActivated()).isEqualTo(dto.isActivated());
    }

    @Test
    void updateCmmn() {
        // given
        CmmnDto.CmmnUpdateRequest dto = new CmmnDto.CmmnUpdateRequest();
        dto.setCmmnCd("EL_UPDATED");
        dto.setCmmnNm("test update nm");
        dto.setCmmnDesc("test update desc");
        dto.setOrderNum(2);
        dto.setActivated(Boolean.FALSE);

        TbCmmn entity = getCmmnEntity();

        // stub
        when(cmmnRepository.findById(any())).thenReturn(Optional.of(entity));
        when(cmmnRepository.save(any())).thenAnswer(invocationOnMock -> {
            entity.setCmmnCd(dto.getCmmnCd());
            entity.setCmmnNm(dto.getCmmnNm());
            entity.setCmmnDesc(dto.getCmmnDesc());
            entity.setOrderNum(dto.getOrderNum());
            entity.setActivated(dto.isActivated());

            return entity;
        });

        // when
        cmmnService.updateCmmn(entity.getId(), dto);

        // then
        verify(cmmnRepository).findById(any());
        verify(cmmnRepository).save(any());

        ArgumentCaptor<TbCmmn> captor = ArgumentCaptor.forClass(TbCmmn.class);
        verify(cmmnRepository).save(captor.capture());
        TbCmmn savedEntity = captor.getValue();

        Assertions.assertThat(savedEntity.getId()).isEqualTo(entity.getId());
        Assertions.assertThat(savedEntity.getCmmnNm()).isEqualTo(dto.getCmmnNm());
    }

    @Test
    void deleteCmmn() {
        // given
        TbCmmn entity = getCmmnEntity();
        entity.setCmmnGrp(getCmmnGrpEntity());
        Long id = entity.getId();

        // stub
        when(cmmnRepository.findById(any())).thenReturn(Optional.of(entity));

        // when
        cmmnService.deleteCmmn(id);

        // then
        verify(cmmnRepository).findById(any());
        verify(cmmnRepository).delete(any());
    }

    @Test
    void getAllCmmnsByGrpId() {
        // given
        TbCmmnGrp grpEntity = getCmmnGrpEntity();

        TbCmmn entity1 = getCmmnEntity();
        entity1.setCmmnGrp(grpEntity);

        TbCmmn entity2 = getCmmnEntity();
        entity2.setId(2L);
        entity2.setCmmnGrp(grpEntity);

        TbCmmn entity3 = getCmmnEntity();
        entity3.setId(2L);
        entity3.setCmmnGrp(grpEntity);

        grpEntity.getCmmns().addAll(List.of(entity1, entity2, entity3));

        // stub
        when(cmmnGrpRepository.findById(any())).thenReturn(Optional.of(grpEntity));

        // when
        List<CmmnDto.CmmnListResponse> result = cmmnService.getAllCmmnsByGrpId(grpEntity.getId());

        // then
        verify(cmmnGrpRepository).findById(any());

        Assertions.assertThat(result).hasSize(3);
        Assertions.assertThat(result.getFirst().getCmmnCd()).isEqualTo(entity1.getCmmnCd());

    }

    @Test
    void getDetailCmmnByCmmnGrpId() {
        // given
        TbCmmnGrp grpEntity = getCmmnGrpEntity();
        TbCmmn entity = getCmmnEntity();
        entity.setCmmnGrp(grpEntity);

        // stub
        when(cmmnRepository.findById(any())).thenReturn(Optional.of(entity));

        // when
        CmmnDto.CmmnDetailResponse result = cmmnService.getDetailCmmnByCmmnGrpId(grpEntity.getId(), entity.getId());

        // then
        verify(cmmnRepository).findById(any());

        Assertions.assertThat(result.getCmmnGrpId()).isEqualTo(grpEntity.getId());
        Assertions.assertThat(result.getId()).isEqualTo(entity.getId());

    }

    @Test
    void getDetailCmmn() {
        // given
        TbCmmn entity = getCmmnEntity();
        entity.setCmmnGrp(getCmmnGrpEntity());

        // stub
        when(cmmnRepository.findById(any())).thenReturn(Optional.of(entity));

        // when
        CmmnDto.CmmnDetailResponse result = cmmnService.getDetailCmmn(entity.getId());

        // then
        verify(cmmnRepository).findById(any());

        Assertions.assertThat(result.getId()).isEqualTo(entity.getId());
    }
}