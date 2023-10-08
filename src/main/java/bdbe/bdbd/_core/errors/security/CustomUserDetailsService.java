package bdbe.bdbd._core.errors.security;


import bdbe.bdbd.user.User;
import bdbe.bdbd.user.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// FIXME: 로그인은 직접 서비스에서 처리할 예정, 아래 메서드는 통합 테스트시 사용예정
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJPARepository userJPARepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOP = userJPARepository.findByEmail(email);

        if (userOP.isEmpty()) {
            return null;
        } else {
            User userPS = userOP.get();
            return new CustomUserDetails(userPS); //가입된 유저이면 세션 만듬
        }
    }
}
