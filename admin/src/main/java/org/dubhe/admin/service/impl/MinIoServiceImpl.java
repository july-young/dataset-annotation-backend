
package org.dubhe.admin.service.impl;

import org.dubhe.admin.service.MinIoService;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.utils.RsaEncrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description minIo服务 实现类
 * @date 2022-06-08
 */
@Service
public class MinIoServiceImpl implements MinIoService {

    /**
     * minIO公钥
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * minIO私钥
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 加密字符串
     */
    @Value("${minio.url}")
    private String url;

    @Override
    public Map<String, String> getMinIOInfo() {
        try {
            Map<String, String> keyPair = RsaEncrypt.genKeyPair();
            String publicKey = RsaEncrypt.getPublicKey(keyPair);
            String privateKey = RsaEncrypt.getPrivateKey(keyPair);
            return new HashMap<String, String>(MagicNumConstant.FOUR) {{
                put("url", RsaEncrypt.encrypt(url, publicKey));
                put("accessKey", RsaEncrypt.encrypt(accessKey, publicKey));
                put("secretKey", RsaEncrypt.encrypt(secretKey, publicKey));
                put("privateKey", privateKey);
            }};
        } catch (Exception e) {
            throw new BusinessException("MinIo info empty");
        }
    }
}
