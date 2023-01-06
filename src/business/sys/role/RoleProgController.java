package business.sys.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import business.biz.FormTagManager;
import business.sys.role.domain.RoleDomain;
import common.base.BaseController;
import common.util.CommUtils;

/**
 * @클래스명		: RoleProgController
 * @최초작성자	: 2017.02.23 ntarget
 * @변경자 		: 2017.02.23 ntarget
 * @설명 		: 관리자 - 권한별 프로그램 관리
 */

@Controller
@SuppressWarnings("all")
public class RoleProgController extends BaseController {

	@Autowired
	private RoleService  roleService;

    @Autowired
    private RoleProgService  roleProgService;

    /**
     * 권한별 프로그램리스트 조회
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/board/listRoleByProg.do")
    public String listRoleByProg(HttpServletRequest request, ModelMap model)
    throws Exception {
    	String method 			= getMethodName(new Throwable());
    	RoleDomain roleDomain	= new RoleDomain();

    	Map paramMap = getParameterMap(request, true);

    	SetMappingValues(paramMap, method);

    	BeanUtils.copyProperties(roleDomain, paramMap);
    	//-------------------- Default Setting End -----------------------//

		// 롤리스트(콤보)
		List roleList = roleService.listComboRoleAll(paramMap);
		model.addAttribute("roleList", FormTagManager.listToMapCombo(roleList, "comboValue", "comboText", "N"));

    	// 권한 적용된 프로그램리스트
    	List list = new ArrayList();
    	// 권한 미적용된 프로그램리스트
    	List notList = new ArrayList();

    	if (!CommUtils.nvlTrim((String)paramMap.get("roleId")).equals("")) {
    		list 	= roleProgService.listRoleByProg(paramMap);
    		notList = roleProgService.listNotRoleByProg(paramMap);
    	}

    	// Return Values
    	model.addAttribute("model", 	roleDomain);
    	model.addAttribute("list", 		list);
    	model.addAttribute("notList", 	notList);

    	return "admin/listRoleByProg";
    }

    /**
     * 권한별 프로그램 저장
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/board/saveRoleByProg.do")
    public ModelAndView saveRoleByProg(HttpServletRequest request, ModelMap model)
    throws Exception {
    	String method 			= getMethodName(new Throwable());
    	RoleDomain roleDomain	= new RoleDomain();

    	Map paramMap = getParameterMap(request, true);

    	SetMappingValues(paramMap, method);

    	BeanUtils.copyProperties(roleDomain, paramMap);
    	//-------------------- Default Setting End -----------------------//

    	int cnt = roleProgService.saveRoleByProg(paramMap);

    	if (cnt > 0) {
    		resultFlag(message.getMessage("prompt.success"));
    	} else {
    		resultFlag("0");
    	}

    	Map returnMap = new HashMap();
    	returnMap.put("roleId",		paramMap.get("roleId"));
    	return new ModelAndView("redirect:/board/listRoleByProg.do", returnMap);
    }


	// ******************************************************************************************************//
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    Default Values Setting     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	// ******************************************************************************************************//
	private void SetMappingValues(Map paramMap, String method) {
		if (method.equalsIgnoreCase("listRoleByProg")) {
			paramMap.put("roleId",		CommUtils.nvlTrim((String)paramMap.get("roleId")));
		}
		if (method.equalsIgnoreCase("saveRoleByProg")) {
			paramMap.put("roleId",  	CommUtils.nvlTrim((String)paramMap.get("roleId")));
		}
	}
}