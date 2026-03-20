import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import com.heima.wemedia.WemediaApplication;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.impl.WmNewsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {
    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private GreenImageScan greenImageScan;

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testScanText() throws Exception {
       Map map=greenTextScan.greeTextScan("你到底在干什么啊");
       System.out.println(map);
    }

    @Test
    public void testScanImage() throws Exception {
       byte[] bytes=fileStorageService.downLoadFile("https://heima-leadnews.oss-cn-beijing.aliyuncs.com/20230522/1684745327332.png");
        Map map=greenImageScan.imageScan(Arrays.asList(bytes));
        System.out.println(map);
    }


}
