package bdbe.bdbd.user;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {

    @Getter @Setter
    public static class FindById{
        private long id;
        private String username;
        private String email;

        public FindById(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }

    @Getter @Setter
    public static class LoginResponse {
        private String jwtToken;
        private String redirectUrl;

        public LoginResponse(String jwtToken, String redirectUrl) {
            this.jwtToken = jwtToken;
            this.redirectUrl = redirectUrl;
        }
    }

}
