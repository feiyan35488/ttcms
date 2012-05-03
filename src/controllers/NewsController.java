package controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.Context;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import utils.CV;
import utils.PluginUtil;
import utils.form.PageForm;
import domains.Comment;
import domains.News;

public class NewsController {

	@Ok(">>:/news/list")
	public void index(){
	}
	/**
	 * params: offset,max
	 * @return
	 */
	public Object list(@Param("offset")int offset ,@Param("max") int max ) {
		PageForm<News> pf = PageForm.getPaper(dao, News.class,Cnd.orderBy().desc("id"),null, offset, max);
		for(News news : pf.getResults()){
			dao.fetchLinks(news, "tags");
			dao.fetchLinks(news, "categorys");
		}
		Context ctx = Lang.context();
		ctx.set("obj", pf);
		PluginUtil.getAllCount(dao,ctx);
		return ctx;
	}
	/**
	 * params: offset,max,tag
	 * @return
	 */
	public Object listByTag(@Param("offset")int offset , @Param("max")int max,@Param("id")int id) {
		if(id == 0){
			return CV.redirect("/news/list","标签不能为空");
		}
		PageForm<News> pf = PageForm.getPaper(dao, News.class,Cnd.format("id in (select news_id from t_news_tag where tag_id = %d) order by id desc",id ),Cnd.format("id in (select news_id from t_news_tag where tag_id = %d)",id ), offset, max);
		for(News news : pf.getResults()){
			dao.fetchLinks(news, "tags");
			dao.fetchLinks(news, "categorys");
		}
		Context ctx = Lang.context();
		ctx.set("obj", pf);
		ctx.set("tagId", id);
		PluginUtil.getAllCount(dao,ctx);
		return ctx;
	}
	/**
	 * params: offset,max,tag
	 * @return
	 */
	public Object  listByMonth(@Param("offset")int offset ,@Param("max")int max,@Param("month")String month) {
		if(Strings.isEmpty(month)){
			return CV.redirect("/news/list","日期归档不能为空");
		}
		PageForm<News> pf = PageForm.getPaper(dao, News.class,Cnd.where("concat(year(create_time),'-',month(create_time))","=", month).desc("id"),Cnd.where("concat(year(create_time),'-',month(create_time))","=", month), offset, max);
		for(News news : pf.getResults()){
			dao.fetchLinks(news, "tags");
			dao.fetchLinks(news, "categorys");
		}
		Context ctx = Lang.context();
		ctx.set("obj", pf);
		ctx.set("month", month);
		PluginUtil.getAllCount(dao,ctx);
		return ctx;
	}
	/**
	 * params: offset,max,category
	 * @return
	 */
	public Object  listByCategory(@Param("offset")int offset , @Param("max")int max,@Param("id")int id) {
		if(id == 0){
			return CV.redirect("/news/list","分类不能为空");
		}
		PageForm<News> pf = PageForm.getPaper(dao, News.class,Cnd.format("id in (select news_id from t_news_tag where category_id = %d) order by id desc",id ),Cnd.format("id in (select news_id from t_news_tag where tag_id = %d)",id ), offset, max);
		for(News news : pf.getResults()){
			dao.fetchLinks(news, "tags");
			dao.fetchLinks(news, "categorys");
		}
		Context ctx = Lang.context();
		ctx.set("obj", pf);
		ctx.set("catId", id);
		PluginUtil.getAllCount(dao,ctx);
		return ctx;
	}
	/**
	 * params: offset,max,keyword
	 * @return
	 */
	public Object search(@Param("offset")int offset , @Param("max")int max,@Param("p")String p) {
		if(Strings.isEmpty(p)){
			return CV.redirect("/news/list","搜索字段不能为空");
		}
		PageForm<News> pf = PageForm.getPaper(dao, News.class,Cnd.where("title","like","%"+p+"%").or("content", "like", "%"+p+"%").desc("id"),Cnd.where("title","like","%"+p+"%").or("content", "like", "%"+p+"%"), offset, max);
		for(News news : pf.getResults()){
			dao.fetchLinks(news, "tags");
			dao.fetchLinks(news, "categorys");
		}
		Context ctx = Lang.context();
		ctx.set("obj", pf);
		ctx.set("p", p);
		PluginUtil.getAllCount(dao,ctx);
		return ctx;
	}
	public Object show(@Param("id")long id){
		News news = dao.fetch(News.class,id);
		if(news == null){
			return CV.redirect("/news/list", "此文章不存在");
		}else{
			dao.fetchLinks(news, null);
			Context ctx = Lang.context();
			ctx.set("obj", news);
			PluginUtil.getAllCount(dao, ctx);
			return ctx;
		}
	}
	@Ok("raw")
	public Object saveComment(HttpServletRequest req,@Param("username")String username,@Param("code")String code,@Param("content")String content,@Param("newsId")long newsId){
		if(Strings.isEmpty(username)){
			username = req.getRemoteHost();
		}
		if(Strings.isEmpty(code)){
			return  "{result:false,msg:'暗号不能为空'}";
		}
		if(newsId ==0){
			return  "{result:false,msg:'你丫干毛呢'}";
		}
		if(! "宝塔镇河妖".equals(code)){
			return  "{result:false,msg:'真笨，暗号都猜不对'}";
		}
		Comment comment = new Comment();
		comment.setUsername(username);
		comment.setCreateTime(new Date());
		comment.setNewsId(newsId);
		comment.setContent(content);
		dao.insert(comment);
		return  "{result:true,msg:'评论插入成功'}";
	}
	private Dao dao;
	public void setDao(Dao dao){
		this.dao = dao;
	}
}
