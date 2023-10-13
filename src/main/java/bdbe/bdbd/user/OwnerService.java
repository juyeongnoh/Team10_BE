package bdbe.bdbd.user;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.exception.InternalServerError;
import bdbe.bdbd._core.errors.exception.UnAuthorizedError;
import bdbe.bdbd._core.errors.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service

public class OwnerService {
         private final PasswordEncoder passwordEncoder;
         private final UserJPARepository userJPARepository;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        try {
            userJPARepository.save(requestDTO.toEntity(encodedPassword));
        } catch (Exception e) {
            throw new InternalServerError("unknown server error");
        }
    }


    public UserResponse.LoginResponse login(UserRequest.LoginDTO requestDTO) {
        User userPS = userJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new BadRequestError("email not found : "+requestDTO.getEmail())
        );

        if(!passwordEncoder.matches(requestDTO.getPassword(), userPS.getPassword())) {
            throw new BadRequestError("wrong password");
        }

        // 여기서 사용자의 권한을 확인합니다.
        String userRole = String.valueOf(userPS.getRole());
        if (!"ROLE_OWNER".equals(userRole) && !"ROLE_ADMIN".equals(userRole)) {
            throw new UnAuthorizedError("can't access this page");
        }

        String jwt = JWTProvider.create(userPS);
        String redirectUrl = "/owner/home";

        return new UserResponse.LoginResponse(jwt, redirectUrl);
    }



    public void sameCheckEmail(String email) {
        Optional<User> userOP = userJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new BadRequestError("duplicate email exist : " + email);
        }
    }
}
