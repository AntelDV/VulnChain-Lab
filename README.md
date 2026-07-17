# 🔗 VulnChain-Lab

Web app lab mô phỏng trang bán thiết bị IT (Spring Boot + Tomcat), được xây dựng với nhiều lỗ hổng để luyện tập khai thác. Mục tiêu: bắt đầu từ tài khoản thường, leo quyền và đạt RCE trên server.

---

## 🛠️ Công nghệ sử dụng

- **Backend:** Java Spring Boot 4.x + Tomcat 11 (WAR deployment)
- **Database:** MySQL 8
- **Frontend:** HTML + CSS + JavaScript
- **Deployment:** Docker + Docker Compose

---

## 🎯 Các lỗ hổng

### 🌐 Insecure Default Initialization (CWE-1188)
Spring Boot Actuator để công khai không cần xác thực do không được cấu hình hạn chế truy cập. Endpoint `/actuator/env` tiết lộ tên các biến môi trường nhạy cảm như `JWT_SECRET_KEY`; tuy giá trị bị mask thành `******` nhưng cho biết biến này có tồn tại.

### 🔓 Insecure Direct Object Reference — IDOR (CWE-639)
Endpoint `/users/{id}` không kiểm tra người gửi request có quyền truy cập hay không. Từ `/users/me` biết được id của mình, dò và suy ra id của admin rồi gọi thẳng `/users/1` để đọc thông tin tài khoản admin, chuẩn bị cho việc forge JWT.

### 📂 Path Traversal — Read (CWE-22)
Server kiểm tra chuỗi `../` trước khi decode URL. Dùng double URL encoding %252e%252e%252f để lọt qua filter, sau 2 lần decode mới trở lại `../` thì traversal mới thật sự xảy ra. Lợi dụng điểm này đọc `/proc/self/environ` của process Tomcat, lấy giá trị thật của `JWT_SECRET_KEY`.

### 🔑 JWT Signature Bypass (CWE-347)
Secret key lấy được từ bước Path Traversal cho phép tự ký JWT hợp lệ với bất kỳ nội dung nào, khiến toàn bộ cơ chế xác thực mất tác dụng vì server không thể phân biệt token tự tạo với token thật. Tạo token với role: ADMIN để nâng quyền từ user thường lên admin.

### 📤 File Upload Bypass (CWE-434 + CWE-602)
Chức năng upload chỉ validate loại file qua header `Content-Type` do client gửi. Vì header này hoàn toàn do client kiểm soát nên có thể thay đổi giá trị của `Content-Type` thành `image/jpeg` khi upload một JSP webshell là qua được filter. Tuy nhiên file upload lên nằm trong thư mục `/uploads`, nơi Tomcat không execute.

### 📝 Path Traversal — Write (CWE-22)
Server kiểm tra chuỗi `../` trước khi decode URL. Đặt tên file %2e%2e%2fshell.jsp để lọt qua filter; sau khi decode thành ../shell.jsp, file thoát khỏi thư mục `/uploads` và ghi thẳng vào Tomcat webapps root. Khi truy cập `/shell.jsp`, Tomcat tự compile và execute file JSP, dẫn đến việc RCE với quyền root do container chạy với user root.

---

## 🧰 Công cụ sử dụng

Burp Suite, ffuf.

---

## 🚀 Cài đặt

**Yêu cầu:** Docker, Docker Compose, Java 21, Maven

```bash
git clone https://github.com/AntelDV/VulnChain-Lab.git
cd VulnChain-Lab
./mvnw clean package -DskipTests
docker compose up -d --build
```

Truy cập tại: `http://localhost:8090`

**Dọn dẹp:**

```bash
docker compose down -v
```

---

> ⚠️ **Cảnh báo:** Project chứa các lỗ hổng bảo mật. Chỉ dùng trong môi trường lab, không deploy production.
