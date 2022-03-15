import com.crowd.util.CrowdUtil;
import com.crowd.util.ResultEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestFile {


    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream("name.txt");
        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOss("http://oss-cn-shanghai.aliyuncs.com", "LTAI5tL1PaxuauPPdZU4QiFK",
                "C1W79Ytf2HTkXbS75sABfk7cG0j9Nl", fileInputStream, "crowd-for-lcc", "http://crowd-for-lcc.oss-cn-shanghai.aliyuncs.com",
                "name.txt");
        System.out.println(resultEntity);
    }

}
