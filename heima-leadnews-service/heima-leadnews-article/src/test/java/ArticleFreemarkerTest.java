import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.mapper.ApAtricleContentMapper;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.jsoup.helper.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {
    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private ApAtricleContentMapper apAtricleContentMapper;
    @Test
    public void createStaticUrTest() throws Exception{
        //1.获取文章内容
        ApArticleContent apArticleContent=apAtricleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId,1390536764510310401L));
        if(apArticleContent!=null && StringUtils.isNotBlank(apArticleContent.getContent())){
            //2.文章内容通过freemarker生成html文件
            StringWriter sw=new StringWriter();
            Templates template=configuration.getTemplate("article.ftl");
            Map<String,Object> map=new HashMap<>();
            params.put("content", JSONArray.parseArray(apArticleContent.getContent()));

            template.process(params,out);
            InputStream is=new ByteArrayInputStream(sw.toString().getBytes());

            //3.把html文件上传到minio中
            StringWriter path=fileStorageService.uploadHtmlFile("",apArticleContent.getArticleId()+"html",is);

            //4.修改ap_article表，保存static_url字段
            ApArticle apArticle=new ApArticle();
            apArticle.setId(apArticleContent.getArticleId());
            apArticle.setStaticUrl(path);
            apArticleMapper.updateById(apArticle);
        }
    }
}
