package com.blue.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.blue.bean.Product;
import com.blue.bean.User;
import com.blue.exception.PrivilegeException;
import com.blue.service.ProductService;
import com.blue.utils.DownloadUtils;
import com.blue.utils.PicUtils;
import com.blue.utils.UploadUtils;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Resource(name="productService")//从spring父容器取得此bean
	ProductService productService;
	
	@RequestMapping("/findall")
	public String findAll(Model model) throws Exception{
		// 1.查询所有商品
		List<Product> products = productService.findAll();
		model.addAttribute("ps", products);
		// 2.转发到page.jsp页面
		return "page";
	}
	
	@RequestMapping("/findbyid")
	public String findById( @RequestParam String id,Model model) throws Exception{
		// 1.查询所有商品
		Product product= productService.findById(id);
		model.addAttribute("p", product);
		// 2.转发到page.jsp页面
		return "productInfo";
	}
	
	@RequestMapping("/preaddproduct")
	public String preAddProduct(){
		return "addProduct";
	}
	
	@RequestMapping(value="/toaddproduct",method= RequestMethod.POST)
	public String toAddProduct( @ModelAttribute Product product,MultipartFile multipartFile,HttpServletRequest request,HttpSession session) {
		//将productd的id和图片路径信息放到map中，然后在利用beanutils工具放到bean中，进行持久化操作
		Map<String, String[]> map= new HashMap<String, String[]>();
		//得到真实名称
		String fileName= multipartFile.getOriginalFilename();
		// 得到随机名称
		String randomFileName= UploadUtils.generateRandonFileName(fileName);//multipartFile
		//得到随机目录
		String randomDir= UploadUtils.generateRandomDir(randomFileName);//	/4/8
		String path= request.getServletContext().getRealPath("/upload")+ randomDir ;// 
		String imgUrl= "/upload"+ randomDir+"/" +randomFileName;//图片路径
		
		File imageFile= new File(path, randomFileName);
		if (!imageFile.exists()) { // 目录不存在，创建
			imageFile.mkdirs();
		}
		try {
			multipartFile.transferTo(imageFile);
			map.put("imgurl", new String[]{imgUrl});
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//封装商品的id
		map.put("id", new String[]{UUID.randomUUID().toString()});
		try {
			BeanUtils.populate(product, map);//封装数据到product
			PicUtils picUtils= new PicUtils(request.getServletContext().getRealPath(product.getImgurl()));
			picUtils.resize(200, 200);
			//调用service中添加商品的方法
			User user= (User) session.getAttribute("user");
			productService.add(user, product);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return "redirect:/error";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/error";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "redirect:/error";
		}
		
		
		return "redirect:/index";
	}
	
	
	@RequestMapping("/downloaderror/{sign}")
	public @ResponseBody String ordersearcherror(@PathVariable("sign") String sign){
		if ("1".equals(sign)) {
			return "下载商品销售榜单的过程中失败";
		}
		return "下载商品销售榜单失败，可能是权限不足";
	}
	
	@RequestMapping("/success")
	public @ResponseBody String success(String sign){
		return "销售榜单下载成功";
	}
	
	/**
	 * 下载榜单
	 * @throws UnsupportedEncodingException 
	 * */
	@RequestMapping("/download")
	public String download(HttpSession session,Model model,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		User user= null;
		if (session==null || (user= (User) session.getAttribute("user"))==null) {
			return "redirect:/order/unloginerror";
		}
		
		//得到下载文件名称
		String fileName= new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").format(new Date()) + "销售榜单.csv";
		String agent= request.getHeader("User-agent");//客户端浏览器类型
		//设置下载文件的mimeType类型
		response.setContentType(request.getServletContext().getMimeType(fileName));
		//设置 Content-Dispostion
		response.setHeader("Content-disposition", "attachment;filename="+ DownloadUtils.getDownloadFileName(fileName, agent));
		
		//得到销售榜单信息
		List<Product> products= null;
		try {
			products = productService.findSell(user);
		} catch (PrivilegeException e) {
			e.printStackTrace();
			return "redirect:/product/downloaderror/0";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/product/downloaderror/0";
		}
		response.setCharacterEncoding("gbk");
		try {
			response.getWriter().println("商品名称,销售数量");
			for (Product product : products) {
				response.getWriter().println(product.getName()+ ","+ product.getTotalSaleNum());
				response.getWriter().flush();
			}
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/product/downloaderror/1";
		}
		
		return null;
	}
	
}
