package com.lcwd.electro.store;

import com.lcwd.electro.store.entities.Role;
import com.lcwd.electro.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class ElectroStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectroStoreApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	@Value("${admin.role.id}")
	String role_admin_id;
	@Value("${normal.role.id}")
	String role_normal_id;


	@Override
	public void run(String... args) throws Exception {
//		System.out.println(passwordEncoder.encode("Ahmed"));
//		System.out.println(passwordEncoder.encode("Tameem"));

		try{

			Role roleAdmin = Role.builder().roleID(role_admin_id).roleName("ROLE_ADMIN").build();
			Role roleNormal = Role.builder().roleID(role_normal_id).roleName("ROLE_NORMAL").build();
			List<Role> roles = roleRepository.saveAll(Arrays.asList(roleAdmin, roleNormal));

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
