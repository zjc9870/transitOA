package com.expect.admin.factory.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.UserService;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.factory.WordXmlFactory;
import com.expect.admin.service.ContractService;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.utils.StringUtil;
import com.expect.admin.utils.WordXmlUtil;

import freemarker.template.TemplateException;
import sun.misc.BASE64Encoder;

@Service("jthtFactory")
public class JthtFactory implements WordXmlFactory {

	private final String TEMPLATE_NAME = "jtht2.ftl";
	@Autowired
	private ContractService contractService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;


	
	@Override
	public byte[] create(String wjid) throws IOException, TemplateException {
		ContractVo contractVo = contractService.getContractById(wjid);
		if(contractVo == null) return null;
		List<LcrzbVo> lcrzbVoList = contractVo.getLcrzList(); 
		Map<String,Object> dataMap = new HashMap<>();
		
		setJbxx(contractVo, dataMap);//设置基本信息
		setLcxx(lcrzbVoList, dataMap);//设置流程信息
		
		byte[] content =  WordXmlUtil.create(dataMap, TEMPLATE_NAME) ;
		return content;
	}

	/**
	 * 设置显示流程信息
	 * @param lcrzbVoList
	 * @param dataMap
	 */
	private void setLcxx(List<LcrzbVo> lcrzbVoList, Map<String, Object> dataMap) {
		if(lcrzbVoList == null || lcrzbVoList.isEmpty()) return;

		for (LcrzbVo lcrzbVo : lcrzbVoList) {
			if(StringUtil.equals(lcrzbVo.getLcjd(),"集团合同发起")||StringUtil.equals(lcrzbVo.getLcjd(),"合同发起部门")){
				String imagePath = getImageStrByLcrzbVo(lcrzbVo);
				if (!imagePath.equals("签名附件没有上传")){
					dataMap.put("image1",imagePath);
				}


				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "部门审核")){
				dataMap.put("bmshjg", lcrzbVo.getCljg());
				dataMap.put("bmshr", lcrzbVo.getUserName());
				dataMap.put("bmshsj", lcrzbVo.getClsj());
				dataMap.put("bmshyj",lcrzbVo.getMessage());
				dataMap.put("bmshsj",lcrzbVo.getClsj());

				String imagePath = getImageStrByLcrzbVo(lcrzbVo);
				if (!imagePath.equals("签名附件没有上传")){
					dataMap.put("image2",imagePath);
				}
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "部门负责人审核")||StringUtil.equals(lcrzbVo.getLcjd(),"公司总经理审核")) {
				dataMap.put("bmfzrshjg", lcrzbVo.getCljg());
				dataMap.put("bmfzrshr", lcrzbVo.getUserName());
				dataMap.put("bmfzrshsj", lcrzbVo.getClsj());
				dataMap.put("bmfzrshyj",lcrzbVo.getMessage());
				dataMap.put("bmfzrshsj",lcrzbVo.getClsj());

				String imagePath = getImageStrByLcrzbVo(lcrzbVo);
				if (!imagePath.equals("签名附件没有上传")){
					dataMap.put("image3",imagePath);
				}
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "法律意见")){//法务意见
				dataMap.put("fwshjg", lcrzbVo.getCljg());
				dataMap.put("fwshr", lcrzbVo.getUserName());
				dataMap.put("fwshsj", lcrzbVo.getClsj());
				dataMap.put("fwshyj",lcrzbVo.getMessage());
				dataMap.put("fwshsj",lcrzbVo.getClsj());

				String imagePath = getImageStrByLcrzbVo(lcrzbVo);
				if (!imagePath.equals("签名附件没有上传")){
					dataMap.put("image4",imagePath);
				}
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "集团资产管理部审核")){//资产管理部意见
				dataMap.put("zcglbshjg", lcrzbVo.getCljg());
				dataMap.put("zcglbshr", lcrzbVo.getUserName());
				dataMap.put("zcglbshsj", lcrzbVo.getClsj());
				dataMap.put("zcglbshyj",lcrzbVo.getMessage());
				dataMap.put("zcglbshsj",lcrzbVo.getClsj());

				String imagePath = getImageStrByLcrzbVo(lcrzbVo);
				if (!imagePath.equals("签名附件没有上传")){
					dataMap.put("image5",imagePath);
				}
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "分管负责人意见")){//分管负责人意见
				dataMap.put("fgfzrshjg", lcrzbVo.getCljg());
				dataMap.put("fgfzrshr", lcrzbVo.getUserName());
				dataMap.put("fgfzrshsj", lcrzbVo.getClsj());
				dataMap.put("fgfzrshyj",lcrzbVo.getMessage());
				dataMap.put("fgfzrshsj",lcrzbVo.getClsj());

				String imagePath = getImageStrByLcrzbVo(lcrzbVo);
				if (!imagePath.equals("签名附件没有上传")){
					dataMap.put("image6",imagePath);
				}
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "集团负责人审核")){//负责人意见
				dataMap.put("fzrshjg", lcrzbVo.getCljg());
				dataMap.put("fzrshr", lcrzbVo.getUserName());
				dataMap.put("fzrshsj", lcrzbVo.getClsj());
				dataMap.put("fzrshyj",lcrzbVo.getMessage());
				dataMap.put("fzrshsj",lcrzbVo.getClsj());

				String imagePath = getImageStrByLcrzbVo(lcrzbVo);
				if (!imagePath.equals("签名附件没有上传")){
					dataMap.put("image7",imagePath);
				}
				continue;
			}

		}
	}

	/**
	 * 设置基本信息
	 * @param contractVo
	 * @param dataMap
	 */
	private void setJbxx(ContractVo contractVo, Map<String, Object> dataMap) {
		dataMap.put("sbd", contractVo.getSbd());
		dataMap.put("htbt", contractVo.getHtbt());
		dataMap.put("bh", contractVo.getBh());
		dataMap.put("htnr", contractVo.getHtnr());
		dataMap.put("nhtr", contractVo.getUserName());
		dataMap.put("nqdrq", contractVo.getNqdrq());
		dataMap.put("qx", contractVo.getQx());
		User superUser=userRepository.findOne("1");
		String superUserImage = getImageStrByUser(superUser);
		dataMap.put("image1",superUserImage);
		dataMap.put("image2",superUserImage);
		dataMap.put("image3",superUserImage);
		dataMap.put("image4",superUserImage);
		dataMap.put("image5",superUserImage);
		dataMap.put("image6",superUserImage);
		dataMap.put("image7",superUserImage);

//		String imagePath = getImageStrByContractVo(contractVo);
//		if (!imagePath.equals("签名附件没有上传")){
//			dataMap.put("image1",imagePath);
//		}

	}

	@Override
	public String getFileName(String wjid) {
		ContractVo contractVo = contractService.getContractById(wjid);
		return contractVo.getHtbt() + contractVo.getBh()+"号.doc";
	}

	private String getImageStrByLcrzbVo(LcrzbVo lcrzbVo) {
		User user =lcrzbVo.getUser();
		return getImageStrByUser(user);

	}
//	private String getImageStrByContractVo(ContractVo contractVo){
//		User user =contractVo.getNhtr();
//		return getImageStrByUser(user);
//	}
	public String getImageStrByUser(User user){
		List<AttachmentVo> attachmentVos = userService.getQmAttachmentByUser(user);
		String imgFile="";
		int size=attachmentVos.size();
		if (attachmentVos !=null && attachmentVos.size()>0){
			imgFile=attachmentVos.get(size-1).getPath()+"/"+attachmentVos.get(size-1).getId();
		}
		if(imgFile==""){
			return "签名附件没有上传";
		}
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);//将图片路径用Base64编码
	}

}
