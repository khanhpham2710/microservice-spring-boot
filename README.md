# Microservice Project Documentation

## Giới thiệu
Dự án này xây dựng một hệ thống microservice sử dụng các công nghệ phổ biến như Spring Cloud, Kafka, Redis, MySQL, MongoDB, PostgreSQL, Neo4j và các công cụ hỗ trợ khác để quản lý và vận hành các dịch vụ phân tán. Hệ thống này cung cấp các dịch vụ như quản lý người dùng, sản phẩm, đơn hàng, thanh toán, thông báo và chat.

## Các Dịch Vụ (Services)

### 1. **Config Server (Spring Cloud Config Server)**
Quản lý cấu hình tập trung cho tất cả các microservice. Giúp các dịch vụ dễ dàng truy cập và cập nhật cấu hình khi cần thiết.

### 2. **Discovery Service (Eureka Server)**
Dịch vụ phát hiện và đăng ký các microservice trong hệ thống. Giúp các dịch vụ có thể tìm thấy và giao tiếp với nhau một cách tự động.

### 3. **Gateway (Spring Cloud Gateway, Redis Rate Limiting, Resilience4j)**
- **Định tuyến API**: Cung cấp các tuyến đường API và xử lý các yêu cầu từ phía người dùng.
- **Giới hạn tần suất**: Sử dụng Redis Rate Limiting để kiểm soát số lượng yêu cầu từ người dùng.
- **Circuit Breaker**: Resilience4j được sử dụng để đảm bảo hệ thống có khả năng phục hồi khi gặp sự cố và có cơ chế dự phòng, thử lại.

### 4. **Common Service**
Quản lý các lớp, mô hình, và exception dùng chung giữa các dịch vụ trong hệ thống.

### 5. **Authentication Service (Keycloak)**
Cung cấp và quản lý xác thực và phân quyền người dùng trên toàn bộ hệ thống. Keycloak đảm bảo rằng tất cả các dịch vụ có thể xác thực và phân quyền người dùng một cách nhất quán.

### 6. **Profile Service (Neo4j với OpenFeign)**
- Quản lý và lưu trữ thông tin hồ sơ người dùng.
- Tích hợp với Authentication Service thông qua OpenFeign.

### 7. **Product Service (MySQL & Flyway)**
Quản lý dữ liệu sản phẩm, lưu trữ trong MySQL và sử dụng Flyway để quản lý phiên bản cơ sở dữ liệu.

### 8. **Order Service (OpenFeign, Kafka)**
Quản lý đơn hàng, lấy thông tin khách hàng từ Profile Service và dữ liệu sản phẩm từ Product Service. Sử dụng Kafka để gửi thông báo tới Notification Service khi có thay đổi trạng thái đơn hàng.

### 9. **Payment Service (MySQL, Kafka)**
Quản lý thanh toán và gửi thông báo trạng thái thanh toán cho Notification Service để gửi email xác thực thanh toán.

### 10. **Notification Service (Kafka, MongoDB, Java Mail)**
Xử lý và lưu trữ các thông báo từ Kafka để gửi email cho người dùng.

### 11. **Monitor Admin (Spring Boot Admin Server)**
Giám sát và quản lý tất cả các microservice. Hệ thống sẽ gửi email thông báo khi có dịch vụ nào bị offline.

### 12. **Tracing với Zipkin**
Sử dụng Zipkin để theo dõi các cuộc gọi giữa các dịch vụ. Các dịch vụ liên lạc qua OpenFeign sẽ được ghi lại trong cùng một trace, nhưng khi sử dụng Kafka, các trace sẽ tách biệt.

---

## Hạn Chế

### 1. **Kết nối Chat Service với các Microservices khác**
Chat Service hiện tại đã hoàn thành, nhưng chưa được tích hợp với các microservices khác trong hệ thống, như Profile Service để lấy thông tin người dùng hoặc Notification Service để gửi thông báo. Điều này khiến Chat Service hoạt động như phần riêng nằm ngoài hệ thống

### 2. **Swagger tại port Gateway không hiển thị khi chạy Docker**
Khi chạy dịch vụ trên Docker, Swagger tại port Gateway hiển thị các endpoint nhưng không thể gửi request được phải vào từng port riêng của từng service để gửi request


### 3. **Tracing giữa các dịch vụ qua Kafka không đồng bộ**
Khi các dịch vụ giao tiếp thông qua OpenFeign, tracing hoạt động tốt và các cuộc gọi được ghi lại trong cùng một trace. Tuy nhiên, khi các dịch vụ giao tiếp qua Kafka, các trace lại được tách biệt nên khó trace Notification Service.


### 5. **Chưa sử dụng các công cụ hỗ trợ mở rộng và cân bằng tải**
- Mặc dù hệ thống đã sử dụng các công nghệ như Spring Cloud Gateway và Redis để quản lý tần suất và routing, nhưng khả năng mở rộng và cân bằng tải giữa các dịch vụ chưa được tối ưu, đặc biệt là khi có lượng truy cập cao.
- Đang nghiên cứu các công cụ như Nginx hoặc Kubernetes.

### 6. **Chưa hoàn thiện tính năng Email Notification**
- Dã viết logic quản lý và thử lại gửi và nhận Kafka nhưng chưa thử nghiệm nhiều
- Cũng chưa thử nghiệm nhiều quản lý và thử lại khi Notification Service gửi email thất bại.
-  
### 7. **Cải thiện hiệu suất Redis Rate Limiting**
- Chưa thử nghiệm Redis Rate Limiting ở gateway nhiều. Sử dụng cấu hình tìm được trên mạng

### 8. **Chưa viết test**
- Chủ yếu test thông qua postman với swagger. Chưa viết Unit Test và Intergation Test.

### 9. **Chưa có Observability**
- Đang nghiên cứu Grafana Stack (Grafana, Loki, and Tempo)
