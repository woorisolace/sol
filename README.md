# Project : shopping mall

### 🖥️ 프로젝트 소개
AI를 활용하여 공간인식 + 공간안내 및 청소꿀팀, 청소용품 추천 쇼핑몰


### 🎥 프로젝트 기획발표 영상
<http://woori-prn.iptime.org:8080/plan/230627.html/>

---

### 🕰 프로젝트 기간
23.11.06 ~ 23.12.15 (총 40일)


### 👨‍👨‍👧‍👧 프로젝트 참여인원/역할 (4명)
1. 문혜선 (팀장)
   * 업무 총괄 및 발표
   * 관리자-상품 CRUD
   * 회원-상품 진열 및 상세페이지
   * 구매-양식 조회, 완료페이지, 구매목록
   * alert창
   * 전체 페이지 레이아웃
   * RDS, S3 서버 구축
  
2. 박근예
   * 회원-회원가입, 로그인, 마이페이지(회원정보, 구매내역)
   * 관리자-회원목록, 주문내역
   * AI 이미지 학습, 이미지 인식 및 결과 출력
   * HTML 및 CSS 디자인 작업
   * 톰캣, 플라스크 구축
  
4. 이상빈
    * AI 이미지 수집, 편집 및 증강
    * 세부 기능 개발 (검색 기능, 상품진열, 페이지네이션 등)
    * 발표 자료 수집
  
5. 조인준
    * 관리자 공지사항 - 회원 공지사항 관리
    * S3 구축
    * HTML 및 디자인 작업
    * GitHub 관리
    * MVC 보조 작업
---

### ⚙ 개발환경
1. Backend
   * Java, SpringBoot, Jpa
   
2. Frontend
     * Bootstrap, HTML, CSS, Java Script

3. DataBase
     * MariaDB, redis, MySQL
   
4. Cloud (AWS)
     * EC2 - Tomcat, Flask
     * RDS
     * S3
       
5. OS
   * Linux

6. AI
    * Python, Colab, conda, LabeImg, Spyder
  
7. Tool
    * IntelliJ, Junit5
   
   


### 📌 주요기능
1. 관리자
     * 회원 : 회원목록 및 구매내역
     * 상품 : CRUD (enum활용하여 카테고리, 판매상태 분류 / 대표, 서브, 상세 이미지 분류하여 등록)
     * 공지사항 : CRUD(enum 활용하여 공지 카테고리 분류)
   
2. 회원
     * 로그인 : 로그인페이지, 로그인 실패 alert창
     * 회원가입 : 홈페이지 회원가입 및 카카오 회원가입
     * 마이페이지 : 회원정보 및 구매내역 확인
     * 상품 목록 조회 : 카테고리 별로 상품 진열 (ALL카테고리에는 모든 상품 진열, SELL 상태의 제품만 진열)
     * 상품 상세 조회 : 구매 수량 선택, 자동 합산 기능, 연관상품 추천
     * 상품구매 : 배송지 입력, 결제 방법 선택
     * 공지사항 : 관리자에서 작성한 공지사항 조회가능(읽기전용)

3. AI
     * 이미지 인식 : 파일첨부 시 Flask에 Yolov5 연동하여 이미지 분석
     * 이미지 결과 : 이미지에 해당하는 공간 출력, 청소꿀팁 소개, 연관 상품 추천

