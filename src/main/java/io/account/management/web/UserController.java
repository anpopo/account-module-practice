package io.account.management.web;

import io.account.management.domain.user.Users;
import io.account.management.domain.user.UsersRepository;
import io.account.management.exception.ResourceNotFoundException;
import io.account.management.security.CurrentUser;
import io.account.management.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UsersRepository usersRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public Users getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return usersRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
