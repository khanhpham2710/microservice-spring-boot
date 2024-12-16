package microservice.authentication_service.response;

public record LoginResponse(String id, String accessToken,String refreshToken) {
}
