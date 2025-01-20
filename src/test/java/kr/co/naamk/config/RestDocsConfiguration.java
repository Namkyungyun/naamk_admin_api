package kr.co.naamk.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

@TestConfiguration
public class RestDocsConfiguration {

    // RestDocumentationResultHandler -> REST Docs의 주요 구성 중 하나로, 테스트 실행 중 요청과 응답을 캡처하고 이를 문서화하는 역할.
    // 이 핸들러는 테스트 메서드에서 자동으로 적용되어 REST Docs 문서를 생성
    @Bean
    public RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}", // 문서 저장 경로 형식
                        Preprocessors.preprocessRequest( // 요청 데이터 문서화하기 전의 전처리 작업 수행
                            Preprocessors.modifyUris()
                                    .scheme("http")
                                    .host("localhost"),
            //                            .removePort(), // host가 도메인인 경우에는 상관없음 (개발 환경에서는 port 필요)
                            Preprocessors.prettyPrint() // JSON 또는 XML 응답 데이터를 사람이 읽기 쉬운 형태로 변환
                        ),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );
    }
}
