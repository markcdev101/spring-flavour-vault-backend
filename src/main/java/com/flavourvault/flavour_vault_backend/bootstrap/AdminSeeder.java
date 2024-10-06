package com.flavourvault.flavour_vault_backend.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.flavourvault.flavour_vault_backend.dto.RegisterUserDto;
import com.flavourvault.flavour_vault_backend.model.Role;
import com.flavourvault.flavour_vault_backend.model.RoleEnum;
import com.flavourvault.flavour_vault_backend.model.User;
import com.flavourvault.flavour_vault_backend.repository.RoleRepository;
import com.flavourvault.flavour_vault_backend.repository.UserRepository;

import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;



	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		this.createSuperAdministrator();
	}

	private void createSuperAdministrator() {
		RegisterUserDto userDto = new RegisterUserDto();
		userDto.setFullName("Super Admin");
		userDto.setEmail("super.admin@email.com");
		userDto.setPassword("123456");

		Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
		Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

		if (optionalRole.isEmpty() || optionalUser.isPresent()) {
			return;
		}

		User user = new User();
		user.setFullName(userDto.getFullName());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setRole(optionalRole.get());

		userRepository.save(user);
	}
}