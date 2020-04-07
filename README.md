# Todo List - Single Page Application

# 개발 환경

## Backend
	JAVA 8
	Spring Boot 2.2
	Spring Data JPA - DB: MySQL
	Gradle
	Docker
	
## Frontend
	JavaScript (ES6, ES7)
	Vue.js
	Vuex
	Vuetify CSS Framework
	Vue CLI
	
## IDE
	IntelliJ (Backend)
	VS Code	(Frontend)
	

# 실행 방법 (for development)
	Backend
		1. Gradle Build 후 Run TodoApplication.java
	
	
	Frontend
		1. to-do-list 폴더 이동 후 터미널에서 npm install 명령어 실행
		2. npm install 명령어 종료 후 터미널에서 npm run serve 명령어 실행
		
		
# 실행 방법 (for production)
	 Frontend
		1. to-do-list 폴더 이동 후 터미널에서 npm run build 명령어 실행
		2. Backend\todo\src\main\resources\static 폴더로 파일이 배포 되었는지 확인
	 	
	 Backend
	 	1번째 방법
			1. Backend\todo 이동 후 터미널에서 gradlew build 명령어 실행
			2. Backend\todo\build\libs 이동 후 터미널에서 java -jar [파일명].war 명령어 실행
			3. localhost:33001 접속
			 
		2번째 방법 (Docker)
		-> Docker 설치 필요.
		-> windows - https://hub.docker.com/editions/community/docker-ce-desktop-windows/
		-> mac - https://hub.docker.com/editions/community/docker-ce-desktop-mac/
		-> linux(ubuntu) - curl -fsSL get.docker.com -o get-docker.sh 
			1. Backend\todo 이동 후 터미널에서 gradlew build 명령어 실행
		    	2. docker build -t [태그명] . 명령어 입력 - ex) docker build -t todo-docker . (docker image 생성)
			3. docker run -p 33001:33001 [태그명] 명령어 입력 - ex) docker run -p 33001:33001 todo-docker (docker container 실행)
			4. localhost:33001 접속
			


# 비고

 application.properties 설정 관련
  - spring.datasource.url
   - 개발로 테스트시 url이 localhost여도 문제 없으나 docker로 실행시 실제 local pc ip 주소를 입력해야 DB 연결이 가능



 Gradle build 할때 lombok 에러 발생할 경우 (IntelliJ)
  - build.gradle에 compile "org.projectlombok:lombok:1.18.8" 추가
  - annotationProcessor("org.projectlombok:lombok:1.18.8")
  - 설정 - Enable annotation processing 체크
		

	
	

	
