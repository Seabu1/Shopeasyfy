package com.shopeasyfy.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopeasyfy.entity.Role;
import com.shopeasyfy.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		
		Role roleAdmin = entityManager.find(Role.class, 1);

		User userSeabu = new User("seabu7177@gmail.com", "seabu123", "Seabu", "Dhakal");
		
		userSeabu.addRole(roleAdmin);
//		System.out.println(roleAdmin.getName());
		User savedUser = repo.save(userSeabu);
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userPriya = new User("priya@gmail.com", "priya123", "Priya", "Karn");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userPriya.addRole(roleEditor);
		userPriya.addRole(roleAssistant);
		
		User savedUser = repo.save(userPriya);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllUsers() {
		
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));	
	}
	
	@Test
	public void testGetUserById() {
		User userSeabu = repo.findById(1).get();
		System.out.println(userSeabu);
		assertThat(userSeabu).isNotNull();
		
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userSeabu = repo.findById(1).get();
		userSeabu.setEnabled(true);
		userSeabu.setEmail("seabu@gmail.com");
		
		repo.save(userSeabu);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userPriya = repo.findById(3).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		userPriya.getRoles().remove(roleEditor);
		userPriya.addRole(roleSalesperson);
		
		repo.save(userPriya);
	}
	
	@Test
	public void testDeleteUser() {
		
		Integer userId = 2;
		repo.deleteById(userId);
		
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "seabu7177@gmail.com";
		User user = repo.getUserByEmailUser(email);
		
		assertThat(user).isNotNull();
		
	}
	
	@Test
	public void testCountById() {
		
		Integer id = 5;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 6;
		repo.updateEnabledStatus(id, false);
		 
	}
	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
		 
	}
	
	@Test
	public void testListFirstPage() {
		
		int pageNumber = 0;
		int pageSize = 4;
		
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers =  page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
		
	}
}
