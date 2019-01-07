package cn.tedu.store.mapper;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTestCase {
	@Autowired
	private UserMapper userMapper;
	
	public void addnew(){
		User user=new User();
		Date now=new Date();
		user.setUsername("root");
		user.setPassword("123");
		user.setGender(1);
		user.setPhone("18745623145");
		user.setEmail("dajd@dd");
		user.setSalt("djh,MD5");
		user.setIsDelete(0);
		user.setCreatedUser("Admin");
		user.setModifiedUser("Admin");
		user.setCreateTime(now);
		user.setModifiedTime(now);
		Integer rows=userMapper.addnew(user);
		System.out.println("rows:"+rows);
	}
	@Test
	public void findByUsername(){
		String username="ww";
		User user=userMapper.findByUsername(username);
		System.out.println(user);
	}
	@Test
	public void updatePassword(){
		Integer id=2;
		String password="234";
		String modifiedUser="ww";
		Date modifiedTime=new Date();
		Integer rows=userMapper.updatePassword(id, password, modifiedUser, modifiedTime);
		System.out.println(rows);
	}
	
	@Test
	public void findById(){
		Integer id=1;
		User user=userMapper.findById(id);
		System.out.println(user);
	}
	@Test
	public void updateInfo(){
		User user=new User();
		user.setId(3);
		user.setGender(1);
		user.setPhone("13298764852");
		user.setEmail("dwoj@daj");
		user.setModifiedUser("yuche");
		user.setModifiedTime(new Date());
		Integer rows=userMapper.updateInfo(user);
		System.err.println(rows);
	}
	@Test
	public void updateAvatar(){
		Integer id=2;
		String avatar="upload/1546830221302";
		String modifiedUser="ww";
		Date modifiedTime=new Date();
		Integer rows=userMapper.updateAvatar(id,avatar, modifiedUser, modifiedTime);
		System.out.println(rows);
	}
	
}
