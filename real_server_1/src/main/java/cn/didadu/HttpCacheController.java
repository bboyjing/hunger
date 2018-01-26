package cn.didadu;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.servlet.ServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangjing on 2017/8/25.
 */
@RestController
@RequestMapping
public class HttpCacheController {

    Cache<String, Long> lastModifiedCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS).build();

    @RequestMapping(path = "/last-modified", method = RequestMethod.GET)
    public ResponseEntity<String> lastModified(@RequestHeader(value = "If-Modified-Since", required = false) Date ifModifiedSince) throws ExecutionException {
        DateFormat gmtDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);

        // 文档最后修改时间，去掉毫秒值
        long lastModifiedMillis = getLastModified() / 1000 * 1000;

        // 当前系统时间，去掉毫秒值
        long now = System.currentTimeMillis() / 1000 * 1000;

        // 文档可以被缓存多久，单位秒
        long maxAge = 20;

        MultiValueMap<String, String> headers = new HttpHeaders();
        // 当前时间
        headers.add("Date", gmtDateFormat.format(new Date(now)));
        // 过期时间 http1.0支持
        headers.add("Expires", gmtDateFormat.format(new Date(now + maxAge * 1000)));
        // 文档生存时间 http1.1支持
        headers.add("Cache-Control", "max-age=" + maxAge);

        // 判断内容是否修改了
        if (ifModifiedSince != null && ifModifiedSince.getTime() == lastModifiedMillis) {
            return new ResponseEntity(headers, HttpStatus.NOT_MODIFIED);
        } else {
            // 文档修改时间
            headers.add("Last-Modified", gmtDateFormat.format(lastModifiedMillis));
            String body = "<a href = ''>点击跳转当前链接</a>";
            //String body = "Message from real server 1.";
            return new ResponseEntity(body  , headers, HttpStatus.OK);
        }
    }

    public long getLastModified() throws ExecutionException {
        return lastModifiedCache.get("lastModified", () -> System.currentTimeMillis());
    }

    @RequestMapping(path = "/etag", method = RequestMethod.GET)
    public ResponseEntity<String> etag(@RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) throws ExecutionException {
        // 当前系统时间

        long now = System.currentTimeMillis();

        // 文档可以在浏览器上缓存多久
        long maxAge = 10;

        //String body = "Message from real server 1.";
        String body = "<a href = 'http://localhost:8081/last-modified'>点击跳转到/last-modified</a>";

        String etag = "W/\"" + MD5Util.MD5(body) + "\"";
        if (StringUtils.equals(ifNoneMatch, etag)) {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

        DateFormat gmtDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        MultiValueMap<String, String> headers = new HttpHeaders();
        // Etag http 1.1支持
        headers.add("Etag", etag);
        // 当前时间
        headers.add("Date", gmtDateFormat.format(new Date(now)));
        // 文档生存时间 http 1.1支持
        headers.add("Cache-Control", "max-age=" + maxAge);
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(path = "/cache_nginx", method = RequestMethod.GET)
    public String cache_nginx() {
        return "from real server 1.";
    }

}
