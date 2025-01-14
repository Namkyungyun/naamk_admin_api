# Getting Started

### INIT DATA API 호출
#### 권한 일반
```
 curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "C",
    "permNm": "생성"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "R",
    "permNm": "조회"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "D",
    "permNm": "삭제"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "U",
    "permNm": "수정"
}'
 
```


#### 권한 그룹
```
curl -X POST 'localhost:28080/perms/grp' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_ROOT",
    "permGrpNm": "루트메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 할당되는 권한의 그룹."
}'; \
curl -X PUT --request PUT 'localhost:28080/perms/grp/1' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_ROOT",
    "permGrpNm": "루트메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 할당되는 권한의 그룹.",
    "childPerms": [
        {
            "permId": 2,
            "activated": true
        }
    ]
}'; \

curl -X POST 'localhost:28080/perms/grp' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_BASE",
    "permGrpNm": "일반메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 하위로 들어가야하는 일반 메뉴 그룹."
}'; \
curl -X PUT --request PUT 'localhost:28080/perms/grp/2' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_BASE",
    "permGrpNm": "일반메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 하위로 들어가야하는 일반 메뉴 그룹.",
    "childPerms": [
        {
            "permId": 1,
            "activated": true
        },
         {
            "permId": 2,
            "activated": true
        },
         {
            "permId": 3,
            "activated": true
        },
         {
            "permId": 4,
            "activated": true
        }
    ]
}'
```

#### 역할
```
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "슈퍼어드민",
    "roleCd": "SUPERADMIN"
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "어드민",
    "roleCd": "ADMIN"
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "뷰어",
    "roleCd": "VIEWER"
}'
```

#### 메뉴
```
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT1",
    "menuNm": "루트 메뉴1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1",
    "menuNm": "메뉴1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 1,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1-1",
    "menuNm": "메뉴1-1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 2,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1-2",
    "menuNm": "메뉴1-2",
    "menuDesc": "test",
    "orderNum": 2,
    "parentId": 2,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT2",
    "menuNm": "루트 메뉴2",
    "menuDesc": "test",
    "orderNum": 2,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU2",
    "menuNm": "메뉴2",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 5,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU2-1",
    "menuNm": "메뉴2-1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 6,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT3",
    "menuNm": "루트3",
    "menuDesc": "test",
    "orderNum": 3,
    "parentId": null,
    "pathUrl": "/"
}'
```


#### 사용자 
```
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터1",
    "userEmail": "test@test.com",
    "loginId": "test1",
    "loginPwd": "test",
    "roleIds": [1]
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터2",
    "userEmail": "test@test.com",
    "loginId": "test2",
    "loginPwd": "test",
    "roleIds": [2]
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터3",
    "userEmail": "test@test.com",
    "loginId": "test3",
    "loginPwd": "test",
    "roleIds": [3]
}'
```

### 전체 CMD
```
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "C",
    "permNm": "생성"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "R",
    "permNm": "조회"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "D",
    "permNm": "삭제"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "U",
    "permNm": "수정"
}'; \
curl -X POST 'localhost:28080/perms/grp' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_ROOT",
    "permGrpNm": "루트메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 할당되는 권한의 그룹."
}'; \
curl -X PUT --request PUT 'localhost:28080/perms/grp/1' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_ROOT",
    "permGrpNm": "루트메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 할당되는 권한의 그룹.",
    "childPerms": [
        {
            "permId": 2,
            "activated": true
        }
    ]
}'; \
curl -X POST 'localhost:28080/perms/grp' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_BASE",
    "permGrpNm": "일반메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 하위로 들어가야하는 일반 메뉴 그룹."
}'; \
curl -X PUT --request PUT 'localhost:28080/perms/grp/2' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_BASE",
    "permGrpNm": "일반메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 하위로 들어가야하는 일반 메뉴 그룹.",
    "childPerms": [
        {
            "permId": 1,
            "activated": true
        },
         {
            "permId": 2,
            "activated": true
        },
         {
            "permId": 3,
            "activated": true
        },
         {
            "permId": 4,
            "activated": true
        }
    ]
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "슈퍼어드민",
    "roleCd": "SUPERADMIN"
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "어드민",
    "roleCd": "ADMIN"
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "뷰어",
    "roleCd": "VIEWER"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT1",
    "menuNm": "루트 메뉴1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1",
    "menuNm": "메뉴1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 1,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1-1",
    "menuNm": "메뉴1-1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 2,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1-2",
    "menuNm": "메뉴1-2",
    "menuDesc": "test",
    "orderNum": 2,
    "parentId": 2,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT2",
    "menuNm": "루트 메뉴2",
    "menuDesc": "test",
    "orderNum": 2,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU2",
    "menuNm": "메뉴2",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 5,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU2-1",
    "menuNm": "메뉴2-1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 6,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT3",
    "menuNm": "루트3",
    "menuDesc": "test",
    "orderNum": 3,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터1",
    "userEmail": "test@test.com",
    "loginId": "test1",
    "loginPwd": "test",
    "roleIds": [1]
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터2",
    "userEmail": "test@test.com",
    "loginId": "test2",
    "loginPwd": "test",
    "roleIds": [2]
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터3",
    "userEmail": "test@test.com",
    "loginId": "test3",
    "loginPwd": "test",
    "roleIds": [3]
}'
```