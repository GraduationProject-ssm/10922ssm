package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.QiuzhixinxiEntity;
import com.entity.view.QiuzhixinxiView;

import com.service.QiuzhixinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 求职信息
 * 后端接口
 * @author 
 * @email 
 * @date 2021-02-28 23:34:02
 */
@RestController
@RequestMapping("/qiuzhixinxi")
public class QiuzhixinxiController {
    @Autowired
    private QiuzhixinxiService qiuzhixinxiService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,QiuzhixinxiEntity qiuzhixinxi, HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("qiye")) {
			qiuzhixinxi.setQiyehao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("xuesheng")) {
			qiuzhixinxi.setXueshenghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<QiuzhixinxiEntity> ew = new EntityWrapper<QiuzhixinxiEntity>();
    	PageUtils page = qiuzhixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, qiuzhixinxi), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,QiuzhixinxiEntity qiuzhixinxi, HttpServletRequest request){
        EntityWrapper<QiuzhixinxiEntity> ew = new EntityWrapper<QiuzhixinxiEntity>();
    	PageUtils page = qiuzhixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, qiuzhixinxi), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( QiuzhixinxiEntity qiuzhixinxi){
       	EntityWrapper<QiuzhixinxiEntity> ew = new EntityWrapper<QiuzhixinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( qiuzhixinxi, "qiuzhixinxi")); 
        return R.ok().put("data", qiuzhixinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(QiuzhixinxiEntity qiuzhixinxi){
        EntityWrapper< QiuzhixinxiEntity> ew = new EntityWrapper< QiuzhixinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( qiuzhixinxi, "qiuzhixinxi")); 
		QiuzhixinxiView qiuzhixinxiView =  qiuzhixinxiService.selectView(ew);
		return R.ok("查询求职信息成功").put("data", qiuzhixinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        QiuzhixinxiEntity qiuzhixinxi = qiuzhixinxiService.selectById(id);
        return R.ok().put("data", qiuzhixinxi);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        QiuzhixinxiEntity qiuzhixinxi = qiuzhixinxiService.selectById(id);
        return R.ok().put("data", qiuzhixinxi);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody QiuzhixinxiEntity qiuzhixinxi, HttpServletRequest request){
    	qiuzhixinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(qiuzhixinxi);

        qiuzhixinxiService.insert(qiuzhixinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody QiuzhixinxiEntity qiuzhixinxi, HttpServletRequest request){
    	qiuzhixinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(qiuzhixinxi);

        qiuzhixinxiService.insert(qiuzhixinxi);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody QiuzhixinxiEntity qiuzhixinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(qiuzhixinxi);
        qiuzhixinxiService.updateById(qiuzhixinxi);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        qiuzhixinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<QiuzhixinxiEntity> wrapper = new EntityWrapper<QiuzhixinxiEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("qiye")) {
			wrapper.eq("qiyehao", (String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("xuesheng")) {
			wrapper.eq("xueshenghao", (String)request.getSession().getAttribute("username"));
		}

		int count = qiuzhixinxiService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	


}
