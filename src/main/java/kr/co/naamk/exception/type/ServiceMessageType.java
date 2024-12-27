package kr.co.naamk.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceMessageType {
    SUCCESS(200, "SUCCESS"),
    NOT_DEFINED_ERROR( 9999, "정의 되지 않은 ERROR 입니다." ),

    ERROR_SQL( 906, " SQL Error" ),
    ERROR_IO( 907, "Server IO Error" ),
    ERROR_DATA_CONVERT(908, "Data Convert Error"),

    REQUEST_PARAM_ERROR( 1303, "요청 parameter 정보가 잘못되어있습니다." ),
    REQUEST_ENTITY_TYPE_ERROR( 1304, "요청 Entity Type이 맞지 않습니다." ),


    // authorization
    SC_UNAUTHORIZED(4001, "클라이언트가 인증되지 않았기 때문에 요청을 정상적으로 처리할 수 없습니다."),
    SC_FORBIDDEN(4003, "클라이언트가 해당 요청에 대한 권한이 없습니다."),


    // entity not found
    USER_NOT_FOUND(3001, "USER_NOT_FOUND"),
    ROLE_NOT_FOUND(3002, "ROLE_NOT_FOUND"),
    PERMISSION_NOT_FOUND(3003, "PERMISSION_NOT_FOUND"),
    MENU_NOT_FOUND(3004, "MENU_NOT_FOUND"),

    ;


    private final Integer code;
    private final String serviceMessage;


    public static ServiceMessageType valueOfString(String messageString) {
        if (messageString == null) {
            return null;
        }
        for (ServiceMessageType type : ServiceMessageType.values()) {
            if (type.name().equalsIgnoreCase(messageString.trim())) {
                return type;
            }
        }
        return null; // 존재하지 않을 경우 null 반환
    }
}
