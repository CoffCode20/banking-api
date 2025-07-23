package co.istad.spring_boot_2.security;

import co.istad.spring_boot_2.domain.User;
import co.istad.spring_boot_2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));

        // set user to CustomUserDetail
        CustomerUserDetail userDetails = new CustomerUserDetail();
        userDetails.setUser(user);

        return userDetails;
    }
}
