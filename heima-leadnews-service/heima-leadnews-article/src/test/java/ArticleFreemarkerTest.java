import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
@Slf4j
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
    private ApArticleContentMapper apArticleContentMapper;

    @Test
    public void createStaticUrTest() throws Exception{
        //1.获取文章内容
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(
                Wrappers.<ApArticleContent>lambdaQuery()
                        .eq(ApArticleContent::getArticleId, 1390536764510310401L)
        );

        if (apArticleContent != null && StringUtils.isNotBlank(apArticleContent.getContent())) {
            // 2. 文章内容通过freemarker生成html文件
            StringWriter sw = new StringWriter();
            Template template = configuration.getTemplate("article.ftl");

            Map<String, Object> params = new HashMap<>();
            String content = apArticleContent.getContent();
            params.put("content", JSONArray.parseArray(content));

            template.process(params, sw);

            // 3. 把html文件上传到minio中
            try (InputStream is = new ByteArrayInputStream(sw.toString().getBytes())) {
                String filename = apArticleContent.getArticleId() + ".html";
                String path = fileStorageService.uploadHtmlFile("", filename, is);

                // 4. 修改ap_article表，保存static_url字段
                ApArticle apArticle = new ApArticle();
                apArticle.setId(apArticleContent.getArticleId());
                apArticle.setStaticUrl(path);
                apArticleMapper.updateById(apArticle);
            } catch (IOException e) {
                log.error("上传HTML文件失败", e);
                throw new RuntimeException("上传HTML文件失败", e);
            }
        }
    }
}
