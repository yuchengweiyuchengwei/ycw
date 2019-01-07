package cn.tedu.store.service.imp;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.tedu.store.entity.User;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.exception.DuplicateKeyException;
import cn.tedu.store.service.exception.InsertException;
import cn.tedu.store.service.exception.PasswordNotMatchException;
import cn.tedu.store.service.exception.UpdateException;
import cn.tedu.store.service.exception.UserNotFoundException;
/**
 * 处理用户数据业务层的实现类
 * @author UID-ECD
 *
 */
@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	private UserMapper userMapper;
	/**
	 * 处理用户注册业务
	 */
	@Override
	public User reg(User user) throws DuplicateKeyException, InsertException {
		//根据尝试注册的用户名查询用户数据
		User data=findByUsername(user.getUsername());
		//判断查询到的数据是否为null
		if(data==null){
			//是：用户名不存在，允许注册，则处理密码加密
			//补充非用户提交的数据
			user.setIsDelete(0);//是否已经删除：否
			//4项日志
			Date now=new Date();
			user.setCreatedUser(user.getUsername());
			user.setCreateTime(now);
			user.setModifiedUser(user.getUsername());
			user.setModifiedTime(now);
			//加密-1:获取随机的UUID作为盐值
			String salt=UUID.randomUUID().toString();
			//2：获取用户提交的原始密码
			String srcPassword=user.getPassword();
			//3：基于原始密码和盐值执行加密，获取通过MD5加密的密码
			String md5Password=getMd5Password(srcPassword, salt);
			//加密-4：将加密后的密码封装在user对象中
			user.setPassword(md5Password);
			//加密-5:将盐值封装在user对象中
			user.setSalt(salt);
			//执行注册
			addnew(user);
			//--返回注册的用户对象
			return user;
		}else{
			//否：用户名已被占用，则抛出DuplicateKeyException异常
			throw new DuplicateKeyException("注册失败!用户名("+user.getUsername()+")已被占用");
		}
	}
	/**
	 * 处理登录业务
	 */
	//处理异常的方式：
	//1.try..catch   处理
	//2.throw/throws 抛出
	@Override
	public User login(String username,String password) throws UserNotFoundException,PasswordNotMatchException{
		//根据参数username查询用户数据
		User data=findByUsername(username);
		//判断用户数据是否为null
		if(data==null){
			//是:为null,用户名不存在,则抛出UserNotFoundException
			throw new UserNotFoundException("登录失败！该用户名不存在");
		}else{
			//否:非null,根据用户名找到了数据,取出盐值
			String salt=data.getSalt();
			//对参数password执行加密
			String md5Password=getMd5Password(password, salt);
			//判断密码是否匹配
			if(data.getPassword().equals(md5Password)){
				//是：匹配,密码正确,则判断是否被删除
				if(data.getIsDelete()==1){
					//是：已被删除,则抛出PasswordNotMatchException或自定义"用户被删除异常"
					throw new UserNotFoundException("登录失败！该用户名不存在");
				}
				//否：没被删除,则登录成功,将第一步查询的用户数据中的盐值和密码设置为null
				data.setSalt(null);
				data.setPassword(null);
				//返回用户数据
				return data;
			}else{
				//否：不匹配，密码错误,则抛出PasswordNotMatchException
				throw new PasswordNotMatchException("登录失败！密码错误");
			}	
		}
	}
	/**
	 * 修改密码
	 * @param id 用户id
	 * @param oldPassword 原密码
	 * @param newPassword 新密码
	 * @throws UserNotFoundException
	 * @throws PasswordNotMatchException
	 * @throws UpdateException
	 */
	@Override
	public void changePassword(Integer id,String oldPassword,String newPassword) throws UserNotFoundException,PasswordNotMatchException,UpdateException{
		// 根据id查询用户数据
		User data = findById(id);
		// 判断查询结果是否为null
		if (data == null) {
			// 是：抛出异常：UserNotFoundException
			throw new UserNotFoundException("修改密码失败！您尝试访问的用户数据不存在！");
		}
		// 判断查询结果中的isDelete是否为1
		if (data.getIsDelete() == 1) {
			// 是：抛出异常：UserNotFoundException
			throw new UserNotFoundException("修改密码失败！您尝试访问的用户数据已经被删除！");
		}
		// 取出查询结果中的盐值
		String salt = data.getSalt();
		// 对参数oldPassword执行MD5加密
		String oldMd5Password = getMd5Password(oldPassword, salt);
		// 将加密结果与查询结果中的password对比是否匹配
		if (data.getPassword().equals(oldMd5Password)) {
			// 是：原密码正确，对参数newPassword执行MD5加密
			String newMd5Password= getMd5Password(newPassword, salt);
			// 获取当前时间
			Date now = new Date();
			// 更新密码
			updatePassword(id, newMd5Password, data.getUsername(), now);
		 } else {
			 // 否：原密码错误，抛出异常：PasswordNotMatchException
			throw new PasswordNotMatchException("修改密码失败！原密码错误！");
		 	}
	}
	@Override
	public void changInfo(User user) throws UserNotFoundException, UpdateException {
		// 根据user.getId()查询用户数据
		User data=findById(user.getId());
		// 判断数据是否为null
		if(data==null){
		// 是：抛出：UserNotFoundException
			throw new UserNotFoundException("修改个人资料失败！您尝试访问的用户数据不存在！");
		}
		// 判断is_delete是否为1
		if(data.getIsDelete()==1){	
			// 是：抛出：UserNotFoundException
			throw new UserNotFoundException("修改个人资料失败！您尝试访问的用户数据已经被删除！");
		}
		// 向参数对象中封装：
		// - modified_user > data.getUsername()
		// - modified_time > new Date()
		user.setModifiedUser(data.getUsername());
		user.setModifiedTime(new Date());
		// 执行修改：gender,phone,email,modified_user,modified_time
		updateInfo(user);
	}
	@Override
	public void changeAvatar(Integer id, String avatar) {
		//根据参数id查询用户数据
		User data=findById(id);
		//判断是否为null
		if(data==null){
			//是:UserNotFoundException
			throw new UserNotFoundException("更新头像失败！您尝试访问的用户数据不存在");
		}
		//判断isDelete==1
		if(data.getIsDelete()==1){
			//是：UserNotFoundException
			throw new UserNotFoundException("更新头像失败！您尝试访问的用户数据已经被删除！");
		}
		String modifiedUser = data.getUsername();
		Date modifiedTime = new Date();
		//执行更新头像
		updateAvatar(id, avatar, modifiedUser, modifiedTime);
	}
	@Override
	public User getById(Integer id){
		User data=findById(id);
		data.setPassword(null);
		data.setSalt(null);
		data.setIsDelete(null);
		return data;
	}
	/**
	 * 通过密码和盐值获取加密方法
	 * @param srcPassword 原始密码
	 * @param salt 盐值
	 * @return
	 */
	private String getMd5Password(String srcPassword,String salt){
		//【注意】以下加密规则中自行设计
		//盐值 拼接 原密码 拼接 盐值
		String str=salt+srcPassword+salt;
		//循环执行10次摘要运算
		for (int i = 0; i <10; i++) {
			str=DigestUtils.md5DigestAsHex(str.getBytes());
		}
		//返回摘要结果
		return str;
	}
	/*插入用户数据*/
	private Integer addnew(User user){
		Integer rows=userMapper.addnew(user);
		if(rows!=1){
			throw new InsertException("增加用户数据时出现未知错误！");
		}
		return null;
	}
	/**
	 * 更新密码
	 * @param id 用户的id
	 * @param password 新密码
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 */
	private void updatePassword(Integer id,String password,String modifiedUser,Date modifiedTime){
		Integer rows=userMapper.updatePassword(id, password, modifiedUser, modifiedTime);
		if(rows!=1){
			throw new UpdateException("更新密码时出现未知错误！");
		}
	}
	/**
	 *  更新用户的个人资料
	 * @param user 用户数据
	 */
	private void updateInfo(User user){
		//执行更新
		Integer rows=userMapper.updateInfo(user);
		// 判断返回值，出错时抛出“更新时的未知错误”
		if(rows!=1){
			throw new UpdateException("更新用户数据时出现未知错误！");
		}		
	}
	/* 根据用户名查询用户数据 */
	private User findByUsername(String username){	
		return userMapper.findByUsername(username);
	}
	/**
	 * 根据用户id查询用户数据
	 * @param id 用户id
	 * @return 匹配的用户数据，如果没有匹配的数据，则返回null
	 */
	private User findById(Integer id){	
		return userMapper.findById(id);
	}
	/**
	 * 更新头像
	 * @param id
	 * @param avatar
	 * @param modifiedUser
	 * @param modifiedTime
	 */
	private void updateAvatar(Integer id,String avatar,String modifiedUser,Date modifiedTime){
		Integer rows=userMapper.updateAvatar(id, avatar, modifiedUser, modifiedTime);
		if(rows!=1){
			throw new UpdateException("更新头像时出现未知错误");
		}
	}
	
}
