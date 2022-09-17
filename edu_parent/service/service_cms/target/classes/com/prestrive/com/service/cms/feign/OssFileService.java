package com.prestrive.com.service.cms.feign;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.cms.feign.fallback.OssFileServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "service-oss",fallback = OssFileServiceFallback.class)
public interface OssFileService {

    @GetMapping("/admin/oss/file/test")
    R test();

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);

}
