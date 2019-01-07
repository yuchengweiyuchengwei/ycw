package cn.tedu.store.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTestCase {
	@Autowired
	private IUserService userService;
	@Test
	public void reg(){
		try{
			User user=new User();
			user.setUsername("springMVC");
			user.setPassword("123");
			user.setGender(1);
			user.setPhone("18745623145");
			user.setEmail("dajd@dd");
			User result=userService.reg(user);
			System.err.println("result:"+result);
		} catch (ServiceException e){
			System.err.println("错误类型："+e.getClass().getName());
			System.err.println("错误描述："+e.getMessage());
		}
	}
	@Test
	public void reglogin(){
		try{
			String username="spring";
			String password="1234";
			User result=userService.login(username, password);
			System.out.println("result="+result);
		} catch (ServiceException e){
			System.err.println("错误类型："+e.getClass().getName());
			System.err.println("错误描述："+e.getMessage());
		}
	}
	@Test
	public void changePassword(){
		try {
			Integer id=1;
			String oldPassword="123";
			String newPassword="1234";
			userService.changePassword(id, oldPassword, newPassword);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println("错误类型："+e.getClass().getName());
			System.err.println("错误描述："+e.getMessage());
		}
	}
	@Test
	public void changeInfo(){
		try {
			User user=new User();
			user.setId(3);
			user.setGender(0);
			user.setPassword("18296874578");
			user.setEmail("dauh@hda");
			userService.changInfo(user);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println("错误类型："+e.getClass().getName());
			System.err.println("错误描述："+e.getMessage());
		}
	}
	@Test
	public void changeAvatar(){
		try {
			Integer id=1;
			String avatar="upload/1546830221302";
			userService.changeAvatar(id, avatar);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println("错误类型："+e.getClass().getName());
			System.err.println("错误描述："+e.getMessage());
		}
	}
}
