package util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class AliyunClientUtil {

    public static DefaultAcsClient aliyunClient(String accessKeyID, String accessKeySecret, String regionId, String productCode, String domain) throws Exception{

        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyID,accessKeySecret);
        DefaultProfile.addEndpoint(regionId, regionId, productCode, domain);
        // 初始化client
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }


}
