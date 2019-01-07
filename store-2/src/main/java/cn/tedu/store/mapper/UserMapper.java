package cn.tedu.store.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import cn.tedu.store.entity.User;
/**
 * 处理用户数据的持久层
 * @author UID-ECD
 *
 */
public interface UserMapper {
	/**
	 * 插入用户数据
	 * @param user 用户数据
	 * @return 受影响的行数
	 */
	Integer addnew(User user);
	/**
	 * 根据用户名查询用户数据
	 * @param username 用户名
	 * @return 匹配的用户数据，如果没有匹配的数据，则返回null;
	 */
	User findByUsername(String username);
	/**
	 * 
	 * @param id
	 * @param password
	 * @param modifiedUser
	 * @param modifiedTime
	 * @return
	 */
	Integer updatePassword(@Param("id") Integer id,@Param("password") String password,@Param("modifiedUser") String modifiedUser,@Param("modifiedTime") Date modifiedTime);
	/**
	 * 根据id查询用户数据
	 * @param id 用户id
	 * @return
	 */
	User findById(Integer id);
	/**
	 * 修改用户资料(不含用户名、密码和头像)
	 * @param user 用户资料
	 * @return 受影响行数
	 */
	Integer updateInfo(User user);
	/**
	 *更新用户头像
	 * @param id 用户id
	 * @param avatar 头像路径
	 * @param modifiedUser 修改人
	 * @param modifiedTime 修改时间
	 * @return 受影响行数
	 */
	Integer updateAvatar(@Param("id") Integer id,@Param("avatar") String avatar,@Param("modifiedUser") String modifiedUser,@Param("modifiedTime") Date modifiedTime);
}
