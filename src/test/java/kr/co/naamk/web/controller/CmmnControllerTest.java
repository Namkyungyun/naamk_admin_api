package kr.co.naamk.web.controller;

import kr.co.naamk.config.RestDocsSupport;
import kr.co.naamk.config.RestDocsConfiguration;
import kr.co.naamk.web.dto.CmmnDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Profile("local")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Slf4j
class CmmnControllerTest extends RestDocsSupport {

    final String COMMON_GROUP = "/cmmn-grps";
    final String COMMON = "/cmmns";

    @Test
    @DisplayName("공통 그룹 코드 생성")
    void createCmmnGrp() throws Exception{
        // given
        String urlPath = COMMON_GROUP;

        CmmnDto.CmmnGrpCreateRequest dto = new CmmnDto.CmmnGrpCreateRequest();
        dto.setCmmnGrpCd("TEST_GRP");
        dto.setCmmnGrpNm("테스트 그룹코드");
        dto.setCmmnGrpDesc("테스트용 코드입니다.");
        dto.setActivated(true);

        String responseBody = om.writeValueAsString(dto);

        // when & then
        this.mockMvc.perform(post(urlPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(responseBody)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                    pathParameters(),
                    requestHeaders(),
                    requestFields(
                            fieldWithPath("cmmnGrpCd").type(JsonFieldType.STRING).description("공통 그룹 코드"),
                            fieldWithPath("cmmnGrpNm").type(JsonFieldType.STRING).description("공통 그룹 명"),
                            fieldWithPath("cmmnGrpDesc").type(JsonFieldType.STRING).description("공통 그룹 설명"),
                            fieldWithPath("activated").type(JsonFieldType.BOOLEAN).description("사용 여부")
                    ),
                    responseFields(
                            fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                            fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                            fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                            fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                            fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                            fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                            fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                            fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문"),
                            fieldWithPath("body.entity").type(JsonFieldType.OBJECT).description("데이터부선언문"),
                            fieldWithPath("body.entity.id").type(JsonFieldType.NUMBER).description("SEQ")
                    )
            ))
            .andReturn();
    }

    @Test
    @DisplayName("공통 그룹 코드 수정")
    void updateCmmnGrp() throws Exception {
        // given
        String urlPath = COMMON_GROUP+"/{cmmnGrpId}";
        Long cmmnGrpId = 1L;

        CmmnDto.CmmnGrpUpdateRequest dto = new CmmnDto.CmmnGrpUpdateRequest();
        dto.setCmmnGrpNm("테스트 그룹코드1");
        dto.setCmmnGrpDesc("테스트용 코드입니다.");
        dto.setActivated(false);

        String responseBody = om.writeValueAsString(dto);

        // when & then
        this.mockMvc.perform(put(urlPath, cmmnGrpId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(responseBody)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        pathParameters(
                                parameterWithName("cmmnGrpId").description("공통 그룹 SEQ")
                        ),
                        requestHeaders(),
                        requestFields(
                                fieldWithPath("cmmnGrpNm").type(JsonFieldType.STRING).description("공통 그룹 명"),
                                fieldWithPath("cmmnGrpDesc").type(JsonFieldType.STRING).description("공통 그룹 설명"),
                                fieldWithPath("activated").type(JsonFieldType.BOOLEAN).description("사용 여부")
                        ),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문"),
                                fieldWithPath("body.entity").type(JsonFieldType.OBJECT).description("데이터부선언문"),
                                fieldWithPath("body.entity.id").type(JsonFieldType.NUMBER).description("공통 그룹 SEQ"),
                                fieldWithPath("body.entity.cmmnGrpCd").type(JsonFieldType.STRING).description("공통 그룹 코드"),
                                fieldWithPath("body.entity.cmmnGrpNm").type(JsonFieldType.STRING).description("공통 그룹 명"),
                                fieldWithPath("body.entity.cmmnGrpDesc").type(JsonFieldType.STRING).description("공통 그룹 명"),
                                fieldWithPath("body.entity.activated").type(JsonFieldType.STRING).description("공통 그룹 사용 여부"),
                                fieldWithPath("body.entity.createdAt").type(JsonFieldType.STRING).description("공통 그룹 생성일"),
                                fieldWithPath("body.entity.updatedAt").type(JsonFieldType.STRING).description("공통 그룹 수정일")
                        )
                ))
                .andReturn();
    }

    @Test
    @DisplayName("공통 그룹 코드 삭제")
    void deleteCmmnGrp() throws Exception{
        // given
        String urlPath = COMMON_GROUP+"/{cmmnGrpId}";
        Long cmmnGrpId = 1L;


        // when & then
        this.mockMvc.perform(delete(urlPath, cmmnGrpId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        pathParameters(
                                parameterWithName("cmmnGrpId").description("공통 그룹 SEQ")
                        ),
                        requestHeaders(),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문")
                        )
                ))
                .andReturn();
    }

    @Test
    @DisplayName("공통 그룹 코드 전체 조회")
    void getAllCmmnGrps() throws Exception{
        // given
        String urlPath = COMMON_GROUP;

        // when & then
        this.mockMvc.perform(get(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        requestHeaders(),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문"),
                                fieldWithPath("body.entity").type(JsonFieldType.ARRAY).description("데이터부선언문"),
                                fieldWithPath("body.entity[].id").type(JsonFieldType.NUMBER).description("공통 그룹 SEQ"),
                                fieldWithPath("body.entity[].cmmnGrpCd").type(JsonFieldType.STRING).description("공통 그룹 코드"),
                                fieldWithPath("body.entity[].cmmnGrpNm").type(JsonFieldType.STRING).description("공통 그룹 명"),
                                fieldWithPath("body.entity[].activated").type(JsonFieldType.STRING).description("공통 그룹 사용 여부"),
                                fieldWithPath("body.entity[].createdAt").type(JsonFieldType.STRING).description("공통 그룹 생성일"),
                                fieldWithPath("body.entity[].updatedAt").type(JsonFieldType.STRING).description("공통 그룹 수정일")
                        )
                ))
                .andReturn();
    }

    @Test
    @DisplayName("공통 그룹 코드 상세 조회")
    void getDetailCmmnGrp() throws Exception {
        // given
        String urlPath = COMMON_GROUP +"/{cmmnGrpId}";
        Long cmmnGrpId = 1L;

        // when & then
        this.mockMvc.perform(get(urlPath, cmmnGrpId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        requestHeaders(),
                        pathParameters(
                                parameterWithName("cmmnGrpId").description("공통 그룹 SEQ")
                        ),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문"),
                                fieldWithPath("body.entity").type(JsonFieldType.OBJECT).description("데이터부선언문"),
                                fieldWithPath("body.entity.id").type(JsonFieldType.NUMBER).description("공통 그룹 SEQ"),
                                fieldWithPath("body.entity.cmmnGrpCd").type(JsonFieldType.STRING).description("공통 그룹 코드"),
                                fieldWithPath("body.entity.cmmnGrpNm").type(JsonFieldType.STRING).description("공통 그룹 명"),
                                fieldWithPath("body.entity.cmmnGrpDesc").type(JsonFieldType.STRING).description("공통 그룹 명"),
                                fieldWithPath("body.entity.activated").type(JsonFieldType.STRING).description("공통 그룹 사용 여부"),
                                fieldWithPath("body.entity.createdAt").type(JsonFieldType.STRING).description("공통 그룹 생성일"),
                                fieldWithPath("body.entity.updatedAt").type(JsonFieldType.STRING).description("공통 그룹 수정일")
                        )
                ))
                .andReturn();
    }

    @Test
    void createCmmn() throws Exception{
        // given
        String urlPath = COMMON_GROUP +"/{grpId}" +COMMON;
        Long grpId = 1L;

        CmmnDto.CmmnCreateRequest dto = new CmmnDto.CmmnCreateRequest();
        dto.setCmmnCd("TEST COMMON CODE1");
        dto.setCmmnNm("테스트 공통 코드1");
        dto.setCmmnDesc("그룹 내에 속할 테스트 공통코드1");
        dto.setOrderNum(1);
        dto.setActivated(true);

        // when & then
        this.mockMvc.perform(post(urlPath, grpId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        requestHeaders(),
                        pathParameters(
                                parameterWithName("grpId").description("공통 그룹 SEQ")
                        ),
                        requestFields(
                                fieldWithPath("cmmnCd").type(JsonFieldType.STRING).description("공통 코드"),
                                fieldWithPath("cmmnNm").type(JsonFieldType.STRING).description("공통 명"),
                                fieldWithPath("cmmnDesc").type(JsonFieldType.STRING).description("공통 설명"),
                                fieldWithPath("orderNum").type(JsonFieldType.NUMBER).description("정렬 번호"),
                                fieldWithPath("activated").type(JsonFieldType.BOOLEAN).description("사용 여부")
                        ),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문")
                        )
                ))
                .andReturn();
    }

    @Test
    void updateCmmn() throws Exception{
        // given
        String urlPath = COMMON +"/{id}";
        Long id = 1L;

        CmmnDto.CmmnUpdateRequest dto = new CmmnDto.CmmnUpdateRequest();
        dto.setCmmnCd("TEST COMMON CODE1");
        dto.setCmmnNm("테스트 공통 코드1");
        dto.setCmmnDesc("그룹 내에 속할 테스트 공통코드1");
        dto.setOrderNum(2);
        dto.setActivated(false);

        // when & then
        this.mockMvc.perform(put(urlPath, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        requestHeaders(),
                        pathParameters(
                                parameterWithName("id").description("공통 SEQ")
                        ),
                        requestFields(
                                fieldWithPath("cmmnCd").type(JsonFieldType.STRING).description("공통 코드"),
                                fieldWithPath("cmmnNm").type(JsonFieldType.STRING).description("공통 명"),
                                fieldWithPath("cmmnDesc").type(JsonFieldType.STRING).description("공통 설명"),
                                fieldWithPath("orderNum").type(JsonFieldType.NUMBER).description("정렬 번호"),
                                fieldWithPath("activated").type(JsonFieldType.BOOLEAN).description("사용 여부")
                        ),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문")
                        )
                ))
                .andReturn();
    }

    @Test
    void deleteCmmn() throws Exception{
        // given
        String urlPath = COMMON +"/{id}";
        Long id = 1L;

        // when & then
        this.mockMvc.perform(delete(urlPath, id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        requestHeaders(),
                        pathParameters(
                                parameterWithName("id").description("공통 SEQ")
                        ),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문")
                        )
                ))
                .andReturn();
    }

    @Test
    void getDetailCmmn() throws Exception{
        // given
        String urlPath = COMMON +"/{id}";
        Long id = 1L;

        // when & then
        this.mockMvc.perform(get(urlPath, id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        requestHeaders(),
                        pathParameters(
                                parameterWithName("id").description("공통 SEQ")
                        ),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문"),
                                fieldWithPath("body.entity").type(JsonFieldType.OBJECT).description("데이터부선언문"),
//                                subsectionWithPath("body.entity").ignored()
                                fieldWithPath("body.entity.id").type(JsonFieldType.NUMBER).description("공통 코드 SEQ"),
                                fieldWithPath("body.entity.cmmnGrpId").type(JsonFieldType.NUMBER).description("공통 코드 그룹 SEQ"),
                                fieldWithPath("body.entity.cmmnCd").type(JsonFieldType.STRING).description("공통 코드"),
                                fieldWithPath("body.entity.cmmnNm").type(JsonFieldType.STRING).description("공통 명"),
                                fieldWithPath("body.entity.cmmnDesc").type(JsonFieldType.STRING).description("공통 설명"),
                                fieldWithPath("body.entity.orderNum").type(JsonFieldType.NUMBER).description("정렬 번호"),
                                fieldWithPath("body.entity.activated").type(JsonFieldType.BOOLEAN).description("사용 여부"),
                                fieldWithPath("body.entity.createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("body.entity.updatedAt").type(JsonFieldType.STRING).description("수정일")
                        )
                ))
                .andReturn();
    }

    @Test
    void getAllCmmByCmmnGrpId() throws Exception{
        // given
        String urlPath = COMMON_GROUP + "/{grpId}" + COMMON;
        Long grpId = 1L;

        // when & then
        this.mockMvc.perform(get(urlPath, grpId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        requestHeaders(),
                        pathParameters(
                                parameterWithName("grpId").description("공통 그룹 SEQ")
                        ),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문"),
                                fieldWithPath("body.entity").type(JsonFieldType.ARRAY).description("데이터부선언문"),
//                                subsectionWithPath("body.entity").ignored()
                                fieldWithPath("body.entity[].id").type(JsonFieldType.NUMBER).description("공통 코드 SEQ"),
                                fieldWithPath("body.entity[].cmmnGrpId").type(JsonFieldType.NUMBER).description("공통 코드 그룹 SEQ"),
                                fieldWithPath("body.entity[].cmmnCd").type(JsonFieldType.STRING).description("공통 코드"),
                                fieldWithPath("body.entity[].cmmnNm").type(JsonFieldType.STRING).description("공통 명"),
                                fieldWithPath("body.entity[].orderNum").type(JsonFieldType.NUMBER).description("정렬 번호"),
                                fieldWithPath("body.entity[].activated").type(JsonFieldType.BOOLEAN).description("사용 여부")
                        )
                ))
                .andReturn();
    }

    @Test
    void getDetailCmmnInCmmnGrp() throws Exception{
        // given
        String urlPath = COMMON_GROUP + "/{grpId}" + COMMON + "/{id}";
        Long grpId = 1L;
        Long id = 1L;

        // when & then
        this.mockMvc.perform(get(urlPath, grpId, id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDoc.document(
                        requestHeaders(),
                        pathParameters(
                                parameterWithName("grpId").description("공통 그룹 SEQ"),
                                parameterWithName("id").description("공통 SEQ")
                        ),
                        responseFields(
                                fieldWithPath("header").type(JsonFieldType.OBJECT).description("헤더선언문"),
                                fieldWithPath("header.responseTime").type(JsonFieldType.STRING).description("결과받은시간"),
                                fieldWithPath("header.actionMethod").type(JsonFieldType.STRING).description("호출 Http 메서드"),
                                fieldWithPath("header.actionUrl").type(JsonFieldType.STRING).description("호출 url"),
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("header.resultMessage").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("header.detailMessage").type(JsonFieldType.STRING).description("결과상세메세지"),
                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("바디선언문"),
                                fieldWithPath("body.entity").type(JsonFieldType.OBJECT).description("데이터부선언문"),
//                                subsectionWithPath("body.entity").ignored()
                                fieldWithPath("body.entity.id").type(JsonFieldType.NUMBER).description("공통 코드 SEQ"),
                                fieldWithPath("body.entity.cmmnGrpId").type(JsonFieldType.NUMBER).description("공통 코드 그룹 SEQ"),
                                fieldWithPath("body.entity.cmmnCd").type(JsonFieldType.STRING).description("공통 코드"),
                                fieldWithPath("body.entity.cmmnNm").type(JsonFieldType.STRING).description("공통 명"),
                                fieldWithPath("body.entity.cmmnDesc").type(JsonFieldType.STRING).description("공통 설명"),
                                fieldWithPath("body.entity.orderNum").type(JsonFieldType.NUMBER).description("정렬 번호"),
                                fieldWithPath("body.entity.activated").type(JsonFieldType.BOOLEAN).description("사용 여부"),
                                fieldWithPath("body.entity.createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("body.entity.updatedAt").type(JsonFieldType.STRING).description("수정일")
                        )
                ))
                .andReturn();
    }
}