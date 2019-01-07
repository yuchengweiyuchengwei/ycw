package cn.tedu.store.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.store.controller.exception.FileEmptyException;
import cn.tedu.store.controller.exception.FileSizeOutOfLimitException;
import cn.tedu.store.controller.exception.FileTypeNotSupportException;
import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.util.ResponseResult;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{
	/**
	 * 上传文件夹的名称
	 */
	private static final String UPLOAD_DIR_NAME="upload";
	/**
	 * 上传文件的最大大小
	 */
	private static final long FILE_MAX_SIZE=5*1024*1024;
	/**
	 * 允许上传的文件类型
	 */
	private static final List<String> FILE_CONTENT_TYPES=new ArrayList<>();
	/**
	 * 初始化允许上传的文件类型的集合
	 */
	static{
		FILE_CONTENT_TYPES.add("image/jpeg");
		FILE_CONTENT_TYPES.add("image/png");
		FILE_CONTENT_TYPES.add("image/jpg");
	}
	@Autowired
	private IUserService userService;
	/*
	 * 请求路径：/user/reg.do
	 * 请求方式:POST
	 * 请求参数：User
	 * 响应数据(成功时)：无
	 */
	@PostMapping("/reg.do")
	public ResponseResult<Void> handleReg(User user){
		userService.reg(user);
		return new ResponseResult<Void>(SUCCESS);	
	}
	@PostMapping("/login.do")
	public ResponseResult<Void> handleLogin(@RequestParam("username") String username,@RequestParam("password") String password,HttpSession session){
		//执行登录
		User user=userService.login(username, password);
		//将相关信息存入到Session
		session.setAttribute("id", user.getId());
		session.setAttribute("username",user.getUsername());
		//返回
		return new ResponseResult<Void>(SUCCESS);
	}
	/*
	 * 请求路径：/user/password.do
	 * 请求方式:GET>POST
	 * 请求参数：old_password(*),new_password(*),
	 * HttpSession
	 * 响应数据(成功时)：无
	 */
	@RequestMapping("/password.do")
	public ResponseResult<Void> changePassword(@RequestParam("old_password") String oldPassword,@RequestParam("new_password") String newPassword,HttpSession session){
		//获取当前登录用户的id
		Integer id =Integer.valueOf(session.getAttribute("id").toString());
		//执行修改密码
		userService.changePassword(id, oldPassword, newPassword);
		//返回
		return new ResponseResult<Void>(SUCCESS);
	}
	/*
	 * 请求路径：/user/info.do
	 * 请求方式:GET/POST
	 * 请求参数：无，HttpSession
	 * HttpSession
	 * 响应数据(成功时)：ResponseResult<User>
	 * 是否拦截：是，登录拦截，无需修改配置
	 */
	@RequestMapping("/info.do")
	public ResponseResult<User> getInfo(HttpSession session){
		// 获取当前登录的用户的id
		Integer id=getIdTromSession(session);
		//执行查询，获取用户数据
		User user=userService.getById(id);
		//返回
		return new ResponseResult<User>(SUCCESS,user);
	}
	/*
	 *  请求路径：/user/change_info.do
	 *  请求类型：POST
	 *  请求参数：User, HttpSession
	 *  响应数据：ResponseResult<Void>
	 *  是否拦截：是，登录拦截，无需修改配置
	 */
	@PostMapping("/change_info.do")	
	public ResponseResult<Void> changeInfo(User user,HttpSession session){
		//获取当前登录的用户的id
		Integer id=getIdTromSession(session);
		//将id封装到参数user中，因为user是用户提交的数据，并不包含id
		user.setId(id);
		//执行修改
		userService.changInfo(user);
		//返回
		return new ResponseResult<>(SUCCESS);
	}
	/*
	 * 请求路径：/user/upload.do
	 * 请求方式:GET>POST
	 * 请求参数：
	 * HttpSession
	 * 响应数据(成功时)：无
	 */
	@PostMapping("/upload.do")
	public ResponseResult<String> handleUpload(HttpSession session,@RequestParam("file") MultipartFile file) throws FileUploadException{
		//TODO 检查是否存在上传文件>file.isEmpty()
		if(file.isEmpty()){
			//抛出异常：文件不允许为空
			throw new FileEmptyException("上传失败！没有选择上传的文件，或选中的文件为空");
		}
		//TODO 检查文件大小>file.getSize()
		if(file.getSize()>FILE_MAX_SIZE){
			//抛出异常：文件大小超出限制
			throw new FileSizeOutOfLimitException("上传失败！上传的文件超出限制");
		}
		//TODO 检查文件类型>file.getContentType()
		if(!FILE_CONTENT_TYPES.contains(file.getContentType())){
			//抛出异常：文件类型限制
			throw new FileTypeNotSupportException("上传失败！文件类型不符");
		}
		//确定上传文件夹的路径>session.getServletContext.getRealPath()>exists()>mkdirs()
		String parentPath=session.getServletContext().getRealPath(UPLOAD_DIR_NAME);
		File parent=new File(parentPath);
		if(!parent.exists()){
			parent.mkdirs();
		}
		//确定文件名>getOriginalFileName()
		String originalFileName=file.getOriginalFilename();
		int beginIndex=originalFileName.lastIndexOf(".");
		String suffix=originalFileName.substring(beginIndex);
		String fileName=System.currentTimeMillis()+""+(new Random().nextInt(900000)+10000000)+suffix;
		//确定文件
		File dest=new File(parent,fileName);
		//执行保存文件
		try {
			file.transferTo(dest);
			System.err.println("上传完成!");
		} catch (IllegalStateException e) {
			//抛出异常：上传失败
			throw new FileUploadException("上传失败！");
		} catch (IOException e) {		
			//抛出异常：上传失败 
			throw new FileUploadException("上传失败！");
		}
		//获取当前用户id
		Integer id=getIdTromSession(session);
		//更新头像数据
		String avatar="/"+ UPLOAD_DIR_NAME + "/" + fileName;
		userService.changeAvatar(id, avatar);
		//返回
		ResponseResult<String> rr=new ResponseResult<>();
		rr.setState(SUCCESS);
		rr.setData(avatar);
		return rr;
	}
}
